package com.rafaelfelipeac.improov.features.goal.presentation.goalform

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.MenuItem
import com.rafaelfelipeac.improov.R
import com.rafaelfelipeac.improov.core.extension.observe
import com.rafaelfelipeac.improov.core.extension.resetValue
import com.rafaelfelipeac.improov.core.extension.visible
import com.rafaelfelipeac.improov.core.extension.gone
import com.rafaelfelipeac.improov.core.extension.invisible
import com.rafaelfelipeac.improov.core.extension.fieldIsEmptyOrZero
import com.rafaelfelipeac.improov.core.extension.checkIfFieldIsEmptyOrZero
import com.rafaelfelipeac.improov.core.extension.toFloat
import com.rafaelfelipeac.improov.core.extension.getNumberInRightFormat
import com.rafaelfelipeac.improov.core.extension.isNotEmpty
import com.rafaelfelipeac.improov.core.platform.base.BaseFragment
import com.rafaelfelipeac.improov.features.commons.DialogOneButton
import com.rafaelfelipeac.improov.features.goal.data.enums.GoalType
import com.rafaelfelipeac.improov.features.goal.domain.model.Goal
import kotlinx.android.synthetic.main.fragment_goal_form.*

@Suppress("TooManyFunctions")
class GoalFormFragment : BaseFragment() {

    private var goal: Goal = Goal()
    private var goalId: Long? = null

    private var firstTimeAdd = false

    private var goalsSize: Int? = null

    private val viewModel by lazy { viewModelFactory.get<GoalFormViewModel>(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        injector.inject(this)

        goalId = arguments?.let { GoalFormFragmentArgs.fromBundle(it).goalId }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        setScreen()

        return inflater.inflate(R.layout.fragment_goal_form, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        goalId.let {
            if (it!! > 0L) {
                viewModel.setGoalId(goalId!!)
            }
        }
        viewModel.loadData()

        setRadioButtonType()
        setSwitchImproov()

        setupLayout()
        observeViewModel()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_save, menu)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuSave -> {
                when {
                    checkIfAnyFieldsAreEmptyOrZero() -> { }
                    getGoalTypeSelected() == GoalType.GOAL_NONE -> {
                        showSnackBar(getString(R.string.goal_form_empty_type_goal))

                        hideSoftKeyboard()
                        goalFormTypeTitle.isFocusableInTouchMode = true
                        goalFormTypeTitle.requestFocus()
                    }
                    !validateDivideAndConquerValues() -> {
                        showSnackBar(getString(R.string.goal_form_gold_silver_bronze_order))

                        goalFormBronzeValue.requestFocus()
                    }
                    else -> {
                        val goalToSave = updateOrCreateGoal()

                        viewModel.saveGoal(goalToSave)

                        verifyFistTimeSaving()

                        return true
                    }
                }
            }
        }

        return false
    }

    private fun setScreen() {
        if (goalId!! == 0L) {
            setTitle(getString(R.string.goal_form_title_new))
        } else {
            setTitle(getString(R.string.goal_form_title_update))
        }

        showBackArrow()
        hasMenu()
        hideNavigation()
    }

    private fun setupLayout() {
        goalFormDivideAndConquerHelp.setOnClickListener {
            hideSoftKeyboard()
            setupBottomSheetTipsDivideAndConquer()
            setupBottomSheetTip()
            showBottomSheetTips()
        }

        goalFormTypeHelp.setOnClickListener {
            hideSoftKeyboard()
            setupBottomSheetTipsGoalType()
            setupBottomSheetTip()
            showBottomSheetTips()
        }
    }

    private fun observeViewModel() {
        viewModel.savedGoal.observe(this) {
            navController.navigateUp()
        }

        viewModel.goal.observe(this) {
            goal = it

            setupGoal()
        }

        viewModel.goals.observe(this) {
            goalsSize = it.size
        }

        viewModel.firstTimeAdd.observe(this) {
            firstTimeAdd = it
        }
    }

    private fun setSwitchImproov() {
        goalFormSwitchDivideAndConquer.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                isDivideAndConquer(true)
            } else {
                isDivideAndConquer(false)
            }
        }
    }

    private fun setRadioButtonType() {
        goalFormRadioTypeList.setOnClickListener {
            if (goal.type != GoalType.GOAL_NONE && goal.type != GoalType.GOAL_LIST) {
                showDialogNoGoalTypeChange()
            } else if (goalFormRadioTypeList.isChecked) {
                goalFormRadioTypeCounter.isChecked = false
                goalFormRadioTypeTotal.isChecked = false

                goalFormGoalCounter.gone()
            }
        }

        goalFormRadioTypeCounter.setOnClickListener {
            if (goal.type != GoalType.GOAL_NONE && goal.type != GoalType.GOAL_COUNTER) {
                showDialogNoGoalTypeChange()
            } else if (goalFormRadioTypeCounter.isChecked) {
                goalFormRadioTypeList.isChecked = false
                goalFormRadioTypeTotal.isChecked = false

                goalFormGoalCounter.visible()
            }
        }

        goalFormRadioTypeTotal.setOnClickListener {
            if (goal.type != GoalType.GOAL_NONE && goal.type != GoalType.GOAL_FINAL) {
                showDialogNoGoalTypeChange()
            } else if (goalFormRadioTypeTotal.isChecked) {
                goalFormRadioTypeCounter.isChecked = false
                goalFormRadioTypeList.isChecked = false

                goalFormGoalCounter.gone()
            }
        }
    }

    private fun isDivideAndConquer(isDivideAndConquer: Boolean) {
        goal.divideAndConquer = isDivideAndConquer

        if (isDivideAndConquer) {
            goalFormSingleValue.resetValue()

            goalFormDivideAndConquer.visible()
            goalFormSingle.invisible()
        } else {
            goalFormBronzeValue.resetValue()
            goalFormSilverValue.resetValue()
            goalFormGoldValue.resetValue()

            goalFormDivideAndConquer.invisible()
            goalFormSingle.visible()
        }
    }

    private fun updateOrCreateGoal(): Goal {
        goal.name = goalFormGoalName.text.toString()
        goal.divideAndConquer = goalFormSwitchDivideAndConquer.isChecked

        if (goal.goalId == 0L) {
            goal.createdDate = getCurrentTime()
            goal.value = 0F
            goal.done = false
            goal.type = getGoalTypeSelected()

            goal.order =
                if (goalsSize == null || goalsSize == 0) {
                    0
                } else {
                    goalsSize!! + 1
                }
        } else {
            goal.updatedDate = getCurrentTime()
        }

        if (getGoalTypeSelected() == GoalType.GOAL_COUNTER) {
            goal.incrementValue = goalFormGoalCounterIncValue.toFloat()
            goal.decrementValue = goalFormGoalCounterDecValue.toFloat()
        }

        if (goal.divideAndConquer) {
            goal.bronzeValue = goalFormBronzeValue.toFloat()
            goal.silverValue = goalFormSilverValue.toFloat()
            goal.goldValue = goalFormGoldValue.toFloat()
        } else {
            goal.singleValue = goalFormSingleValue.toFloat()
        }

        return goal
    }

    private fun getGoalTypeSelected(): GoalType {
        return when {
            goalFormRadioTypeList.isChecked -> {
                GoalType.GOAL_LIST
            }
            goalFormRadioTypeCounter.isChecked -> {
                GoalType.GOAL_COUNTER
            }
            goalFormRadioTypeTotal.isChecked -> {
                GoalType.GOAL_FINAL
            }
            else -> {
                GoalType.GOAL_NONE
            }
        }
    }

    private fun setupGoal() {
        goalFormGoalName.setText(goal.name)

        if (goal.divideAndConquer) {
            goalFormDivideAndConquer.visible()
            goalFormSingle.invisible()

            goalFormBronzeValue.setText(goal.bronzeValue.getNumberInRightFormat())
            goalFormSilverValue.setText(goal.silverValue.getNumberInRightFormat())
            goalFormGoldValue.setText(goal.goldValue.getNumberInRightFormat())

            goalFormSwitchDivideAndConquer.isChecked = true
        } else {
            goalFormSingleValue.setText(goal.singleValue.getNumberInRightFormat())
        }

        setupGoalType()
    }

    private fun setupGoalType() {
        when (goal.type) {
            GoalType.GOAL_LIST -> {
                goalFormRadioTypeList.isChecked = true
                goalFormRadioTypeCounter.isChecked = false
                goalFormRadioTypeTotal.isChecked = false
            }
            GoalType.GOAL_COUNTER -> {
                goalFormRadioTypeCounter.isChecked = true
                goalFormRadioTypeList.isChecked = false
                goalFormRadioTypeTotal.isChecked = false

                goalFormGoalCounter.visible()

                goalFormGoalCounterIncValue.setText(goal.incrementValue.getNumberInRightFormat())
                goalFormGoalCounterDecValue.setText(goal.decrementValue.getNumberInRightFormat())
            }
            GoalType.GOAL_FINAL -> {
                goalFormRadioTypeTotal.isChecked = true
                goalFormRadioTypeList.isChecked = false
                goalFormRadioTypeCounter.isChecked = false
            }
            else -> { }
        }
    }

    private fun verifyFistTimeSaving() {
        if (firstTimeAdd) {
            viewModel.saveFirstTimeAdd(false)
            viewModel.saveFirstTimeList(true)
        }
    }

    private fun showDialogNoGoalTypeChange() {
        val dialog = DialogOneButton()

        dialog.setMessage(getString(R.string.goal_form_goal_dialog_no_goal_type_change))

        dialog.setOnClickListener(object : DialogOneButton.OnClickListener {
            override fun onOK() {
                setupGoalType()

                dialog.dismiss()
            }
        })

        dialog.show(parentFragmentManager, "")
    }

    private fun checkIfAnyFieldsAreEmptyOrZero(): Boolean {
        return when {
            checkIfNameFieldIsEmptyOrZero() || checkIfValuesFieldsAreEmptyOrZero() ||
                    checkIfCounterFieldsAreEmptyOrZero() -> {
                true
            }
            else -> false
        }
    }

    private fun checkIfNameFieldIsEmptyOrZero(): Boolean {
        return when {
            goalFormGoalName.checkIfFieldIsEmptyOrZero() -> {
                goalFormGoalName.fieldIsEmptyOrZero(this)
                true
            }
            else -> false
        }
    }

    private fun checkIfValuesFieldsAreEmptyOrZero(): Boolean {
        return when {
            goalFormSingleValue.checkIfFieldIsEmptyOrZero() && !goal.divideAndConquer -> {
                goalFormSingleValue.fieldIsEmptyOrZero(this)
                true
            }
            goalFormBronzeValue.checkIfFieldIsEmptyOrZero() && goal.divideAndConquer -> {
                goalFormBronzeValue.fieldIsEmptyOrZero(this)
                true
            }
            goalFormSilverValue.checkIfFieldIsEmptyOrZero() && goal.divideAndConquer -> {
                goalFormSilverValue.fieldIsEmptyOrZero(this)
                true
            }
            goalFormGoldValue.checkIfFieldIsEmptyOrZero() && goal.divideAndConquer -> {
                goalFormGoldValue.fieldIsEmptyOrZero(this)
                true
            }
            else -> false
        }
    }

    private fun checkIfCounterFieldsAreEmptyOrZero(): Boolean {
        return when {
            goalFormGoalCounterDecValue.checkIfFieldIsEmptyOrZero() &&
                    (goal.type == GoalType.GOAL_COUNTER ||
                            getGoalTypeSelected() == GoalType.GOAL_COUNTER) -> {
                goalFormGoalCounterDecValue.fieldIsEmptyOrZero(this)
                true
            }
            goalFormGoalCounterIncValue.checkIfFieldIsEmptyOrZero() &&
                    (goal.type == GoalType.GOAL_COUNTER ||
                            getGoalTypeSelected() == GoalType.GOAL_COUNTER) -> {
                goalFormGoalCounterIncValue.fieldIsEmptyOrZero(this)
                true
            }
            else -> false
        }
    }

    private fun validateDivideAndConquerValues(): Boolean {
        return try {
            val gold = goalFormGoldValue.toFloat()
            val silver = goalFormSilverValue.toFloat()
            val bronze = goalFormBronzeValue.toFloat()

            ((gold > silver) && (silver > bronze))
        } catch (e: NumberFormatException) {
            return goalFormSingleValue.isNotEmpty()
        }
    }
}
