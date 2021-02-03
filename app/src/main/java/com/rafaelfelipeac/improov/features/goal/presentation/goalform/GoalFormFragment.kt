package com.rafaelfelipeac.improov.features.goal.presentation.goalform

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.lifecycleScope
import com.rafaelfelipeac.improov.R
import com.rafaelfelipeac.improov.core.extension.resetValue
import com.rafaelfelipeac.improov.core.extension.visible
import com.rafaelfelipeac.improov.core.extension.gone
import com.rafaelfelipeac.improov.core.extension.invisible
import com.rafaelfelipeac.improov.core.extension.focusOnEmptyOrZero
import com.rafaelfelipeac.improov.core.extension.isEmptyOrZero
import com.rafaelfelipeac.improov.core.extension.toFloat
import com.rafaelfelipeac.improov.core.extension.viewBinding
import com.rafaelfelipeac.improov.core.extension.getNumberInExhibitionFormat
import com.rafaelfelipeac.improov.core.extension.isNotEmpty
import com.rafaelfelipeac.improov.core.platform.base.BaseFragment
import com.rafaelfelipeac.improov.databinding.FragmentGoalFormBinding
import com.rafaelfelipeac.improov.features.commons.data.enums.GoalType
import com.rafaelfelipeac.improov.features.commons.domain.model.Goal
import com.rafaelfelipeac.improov.features.dialog.DialogOneButton
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@Suppress("TooManyFunctions")
class GoalFormFragment : BaseFragment() {

    private var goal: Goal = Goal()
    private var goalId: Long = 0L

    private var firstTimeAdd = false

    private var goalsSize: Int = 0

    private val viewModel by lazy { viewModelProvider.goalFormViewModel() }

    private var binding by viewBinding<FragmentGoalFormBinding>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        goalId = arguments?.let { GoalFormFragmentArgs.fromBundle(it).goalId } ?: 0L
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        setScreen()

        return FragmentGoalFormBinding.inflate(inflater, container, false).run {
            binding = this
            binding.root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        goalId.let {
            if (it > 0L) {
                viewModel.setGoalId(goalId)
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
        return when (item.itemId) {
            R.id.menuSave -> {
                checkIfCanSaveGoal()
            }
            else -> false
        }
    }

    private fun setScreen() {
        if (goalId == 0L) {
            setTitle(getString(R.string.goal_form_title_new))
        } else {
            setTitle(getString(R.string.goal_form_title_update))
        }

        showBackArrow()
        hasMenu()
        hideNavigation()
    }

    private fun setupLayout() {
        binding.goalFormDivideAndConquerHelp.setOnClickListener {
            hideSoftKeyboard()
            setupBottomSheetTipsDivideAndConquer()
            setupBottomSheetTip()
            showBottomSheetTips()
        }

        binding.goalFormTypeHelp.setOnClickListener {
            hideSoftKeyboard()
            setupBottomSheetTipsGoalType()
            setupBottomSheetTip()
            showBottomSheetTips()
        }

        binding.goalFormSingleValue.setOnEditorActionListener() { _, actionId, _ ->
            when (actionId) {
                EditorInfo.IME_ACTION_DONE -> {
                    checkIfCanSaveGoal()
                }
                else -> false
            }
        }

        binding.goalFormGoldValue.setOnEditorActionListener() { _, actionId, _ ->
            when (actionId) {
                EditorInfo.IME_ACTION_DONE -> {
                    checkIfCanSaveGoal()
                }
                else -> false
            }
        }

        binding.goalFormGoalCounterIncValue.setOnEditorActionListener() { _, actionId, _ ->
            when (actionId) {
                EditorInfo.IME_ACTION_DONE -> {
                    checkIfCanSaveGoal()
                }
                else -> false
            }
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.savedGoal.collect {
                navController.navigate(GoalFormFragmentDirections.goalFormToGoal(it))
            }
        }

        lifecycleScope.launch {
            viewModel.goal.collect {
                goal = it

                setupGoal()
            }
        }

        lifecycleScope.launch {
            viewModel.goals.collect {
                goalsSize = it.size
            }
        }

        lifecycleScope.launch {
            viewModel.firstTimeAdd.collect {
                firstTimeAdd = it
            }
        }
    }

    private fun setSwitchImproov() {
        binding.goalFormSwitchDivideAndConquer.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                isDivideAndConquer(true)
            } else {
                isDivideAndConquer(false)
            }
        }
    }

    private fun setRadioButtonType() {
        binding.goalFormRadioTypeList.setOnClickListener {
            if (goal.type != GoalType.GOAL_NONE && goal.type != GoalType.GOAL_LIST) {
                showDialogNoGoalTypeChange()
            } else if (binding.goalFormRadioTypeList.isChecked) {
                binding.goalFormRadioTypeCounter.isChecked = false
                binding.goalFormRadioTypeFinal.isChecked = false

                binding.goalFormGoalCounter.gone()
            }
        }

        binding.goalFormRadioTypeCounter.setOnClickListener {
            if (goal.type != GoalType.GOAL_NONE && goal.type != GoalType.GOAL_COUNTER) {
                showDialogNoGoalTypeChange()
            } else if (binding.goalFormRadioTypeCounter.isChecked) {
                binding.goalFormRadioTypeList.isChecked = false
                binding.goalFormRadioTypeFinal.isChecked = false

                binding.goalFormGoalCounter.visible()
            }
        }

        binding.goalFormRadioTypeFinal.setOnClickListener {
            if (goal.type != GoalType.GOAL_NONE && goal.type != GoalType.GOAL_FINAL) {
                showDialogNoGoalTypeChange()
            } else if (binding.goalFormRadioTypeFinal.isChecked) {
                binding.goalFormRadioTypeCounter.isChecked = false
                binding.goalFormRadioTypeList.isChecked = false

                binding.goalFormGoalCounter.gone()
            }
        }
    }

    private fun isDivideAndConquer(isDivideAndConquer: Boolean) {
        goal.divideAndConquer = isDivideAndConquer

        if (isDivideAndConquer) {
            binding.goalFormSingleValue.resetValue()

            binding.goalFormDivideAndConquer.visible()
            binding.goalFormSingle.invisible()
        } else {
            binding.goalFormBronzeValue.resetValue()
            binding.goalFormSilverValue.resetValue()
            binding.goalFormGoldValue.resetValue()

            binding.goalFormDivideAndConquer.gone()
            binding.goalFormSingle.visible()
        }
    }

    private fun checkIfCanSaveGoal(): Boolean {
        when {
            checkIfAnyFieldsAreEmptyOrZero() -> {
            }
            getGoalTypeSelected() == GoalType.GOAL_NONE -> {
                showSnackBar(getString(R.string.goal_form_empty_type_goal))

                hideSoftKeyboard()
                binding.goalFormTypeTitle.isFocusableInTouchMode = true
                binding.goalFormTypeTitle.requestFocus()
            }
            binding.goalFormSwitchDivideAndConquer.isChecked && !validateDivideAndConquerValues() -> {
                showSnackBar(getString(R.string.goal_form_gold_silver_bronze_order))

                binding.goalFormBronzeValue.requestFocus()
            }
            else -> {
                val goalToSave = updateOrCreateGoal()

                viewModel.saveGoal(goalToSave)

                verifyFirstTimeSaving()

                return true
            }
        }

        return false
    }

    private fun updateOrCreateGoal(): Goal {
        goal.name = binding.goalFormGoalName.text.toString()
        goal.divideAndConquer = binding.goalFormSwitchDivideAndConquer.isChecked

        if (goal.goalId == 0L) {
            goal.createdDate = getCurrentTime()
            goal.value = 0F
            goal.done = false
            goal.type = getGoalTypeSelected()

            goal.order = if (goalsSize == 0) 0 else goalsSize + 1
        } else {
            goal.updatedDate = getCurrentTime()
        }

        if (getGoalTypeSelected() == GoalType.GOAL_COUNTER) {
            goal.incrementValue = binding.goalFormGoalCounterIncValue.toFloat()
            goal.decrementValue = binding.goalFormGoalCounterDecValue.toFloat()
        }

        if (goal.divideAndConquer) {
            goal.bronzeValue = binding.goalFormBronzeValue.toFloat()
            goal.silverValue = binding.goalFormSilverValue.toFloat()
            goal.goldValue = binding.goalFormGoldValue.toFloat()
        } else {
            goal.singleValue = binding.goalFormSingleValue.toFloat()
        }

        return goal
    }

    private fun getGoalTypeSelected(): GoalType {
        return when {
            binding.goalFormRadioTypeList.isChecked -> {
                GoalType.GOAL_LIST
            }
            binding.goalFormRadioTypeCounter.isChecked -> {
                GoalType.GOAL_COUNTER
            }
            binding.goalFormRadioTypeFinal.isChecked -> {
                GoalType.GOAL_FINAL
            }
            else -> {
                GoalType.GOAL_NONE
            }
        }
    }

    private fun setupGoal() {
        binding.goalFormGoalName.setText(goal.name)

        if (goal.divideAndConquer) {
            binding.goalFormDivideAndConquer.visible()
            binding.goalFormSingle.invisible()

            binding.goalFormBronzeValue.setText(goal.bronzeValue.getNumberInExhibitionFormat())
            binding.goalFormSilverValue.setText(goal.silverValue.getNumberInExhibitionFormat())
            binding.goalFormGoldValue.setText(goal.goldValue.getNumberInExhibitionFormat())

            binding.goalFormSwitchDivideAndConquer.isChecked = true
        } else {
            binding.goalFormSingleValue.setText(goal.singleValue.getNumberInExhibitionFormat())
        }

        setupGoalType()
    }

    private fun setupGoalType() {
        when (goal.type) {
            GoalType.GOAL_LIST -> {
                binding.goalFormRadioTypeList.isChecked = true
                binding.goalFormRadioTypeCounter.isChecked = false
                binding.goalFormRadioTypeFinal.isChecked = false
            }
            GoalType.GOAL_COUNTER -> {
                binding.goalFormRadioTypeCounter.isChecked = true
                binding.goalFormRadioTypeList.isChecked = false
                binding.goalFormRadioTypeFinal.isChecked = false

                binding.goalFormGoalCounter.visible()

                binding.goalFormGoalCounterIncValue.setText(goal.incrementValue.getNumberInExhibitionFormat())
                binding.goalFormGoalCounterDecValue.setText(goal.decrementValue.getNumberInExhibitionFormat())
            }
            GoalType.GOAL_FINAL -> {
                binding.goalFormRadioTypeFinal.isChecked = true
                binding.goalFormRadioTypeList.isChecked = false
                binding.goalFormRadioTypeCounter.isChecked = false
            }
            else -> {
            }
        }
    }

    private fun verifyFirstTimeSaving() {
        if (firstTimeAdd) {
            viewModel.saveFirstTimeAdd(false)
            viewModel.saveFirstTimeList(true)
        }
    }

    private fun showDialogNoGoalTypeChange() {
        val dialog = DialogOneButton(getString(R.string.goal_form_goal_dialog_no_goal_type_change))

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
            binding.goalFormGoalName.isEmptyOrZero() -> {
                binding.goalFormGoalName.focusOnEmptyOrZero(this)
                true
            }
            else -> false
        }
    }

    private fun checkIfValuesFieldsAreEmptyOrZero(): Boolean {
        return when {
            binding.goalFormSingleValue.isEmptyOrZero() && !goal.divideAndConquer -> {
                binding.goalFormSingleValue.focusOnEmptyOrZero(this)
                true
            }
            binding.goalFormBronzeValue.isEmptyOrZero() && goal.divideAndConquer -> {
                binding.goalFormBronzeValue.focusOnEmptyOrZero(this)
                true
            }
            binding.goalFormSilverValue.isEmptyOrZero() && goal.divideAndConquer -> {
                binding.goalFormSilverValue.focusOnEmptyOrZero(this)
                true
            }
            binding.goalFormGoldValue.isEmptyOrZero() && goal.divideAndConquer -> {
                binding.goalFormGoldValue.focusOnEmptyOrZero(this)
                true
            }
            else -> false
        }
    }

    private fun checkIfCounterFieldsAreEmptyOrZero(): Boolean {
        return when {
            binding.goalFormGoalCounterDecValue.isEmptyOrZero() &&
                    (goal.type == GoalType.GOAL_COUNTER ||
                            getGoalTypeSelected() == GoalType.GOAL_COUNTER) -> {
                binding.goalFormGoalCounterDecValue.focusOnEmptyOrZero(this)
                true
            }
            binding.goalFormGoalCounterIncValue.isEmptyOrZero() &&
                    (goal.type == GoalType.GOAL_COUNTER ||
                            getGoalTypeSelected() == GoalType.GOAL_COUNTER) -> {
                binding.goalFormGoalCounterIncValue.focusOnEmptyOrZero(this)
                true
            }
            else -> false
        }
    }

    private fun validateDivideAndConquerValues(): Boolean {
        return try {
            val gold = binding.goalFormGoldValue.toFloat()
            val silver = binding.goalFormSilverValue.toFloat()
            val bronze = binding.goalFormBronzeValue.toFloat()

            ((gold > silver) && (silver > bronze))
        } catch (e: NumberFormatException) {
            return binding.goalFormSingleValue.isNotEmpty()
        }
    }
}
