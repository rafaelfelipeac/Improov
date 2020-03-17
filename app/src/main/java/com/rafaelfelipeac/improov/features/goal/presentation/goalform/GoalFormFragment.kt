package com.rafaelfelipeac.improov.features.goal.presentation.goalform

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.rafaelfelipeac.improov.R
import com.rafaelfelipeac.improov.core.extension.*
import com.rafaelfelipeac.improov.core.platform.base.BaseFragment
import com.rafaelfelipeac.improov.features.commons.DialogOneButton
import com.rafaelfelipeac.improov.features.goal.data.enums.GoalType
import com.rafaelfelipeac.improov.features.goal.domain.model.Goal
import com.rafaelfelipeac.improov.future.habit.Habit
import com.rafaelfelipeac.improov.features.goal.domain.model.Historic
import com.rafaelfelipeac.improov.features.goal.domain.model.Item
import com.rafaelfelipeac.improov.features.main.MainActivity
import kotlinx.android.synthetic.main.fragment_goal_form.*
import java.text.SimpleDateFormat
import java.util.*

class GoalFormFragment : BaseFragment() {

    private var goal: Goal =
        Goal()
    private var goalId: Long? = null
    private var goals: List<Goal> = listOf()
    private var items: List<Item> = listOf()
    private var history: List<Historic> = listOf()
    private var habits: List<Habit> = listOf()

    private var cal = Calendar.getInstance()

    private var bottomSheetTip: BottomSheetBehavior<*>? = null
    private var bottomSheetTipClose: ConstraintLayout? = null

    private val goalFormViewModel by lazy { viewModelFactory.get<GoalFormViewModel>(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        injector.inject(this)

        goalId = arguments?.let { GoalFormFragmentArgs.fromBundle(it).goalId }

        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        (activity as MainActivity).supportActionBar?.title = getString(R.string.goal_form_title)
        (activity as MainActivity).toolbar.inflateMenu(R.menu.menu_save)

        hideNavigation()

        if (goalId != 0L) { goalId?.let { goalFormViewModel.init(it) } }

        return inflater.inflate(R.layout.fragment_goal_form, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()

        setRadioButtonType()
        setSwitchImproov()

        (activity as MainActivity).openToolbar()

        goal_form_divide_and_conquest_help.setOnClickListener {
            hideSoftKeyboard()
            (activity as MainActivity).setupBottomSheetTipsOne()
            setupBottomSheetTip()
            (activity as MainActivity).openBottomSheetTips()
        }

        goal_form_type_help.setOnClickListener {
            hideSoftKeyboard()
            (activity as MainActivity).setupBottomSheetTipsTwo()
            setupBottomSheetTip()
            (activity as MainActivity).openBottomSheetTips()
        }

        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

//            val myFormat = getString(R.string.date_format_dmy)
//            val sdf = SimpleDateFormat(myFormat, Locale.US)
//            goal_form_set_date.text = sdf.format(cal.time)

            goal.date = cal.time
        }

//        goal_form_set_date.setOnClickListener {
//            val datePickerDialog = DatePickerDialog(context!!,
//                R.style.DialogTheme,
//                dateSetListener,
//                cal.get(Calendar.YEAR),
//                cal.get(Calendar.MONTH),
//                cal.get(Calendar.DAY_OF_MONTH))
//
//            datePickerDialog.show()
//
//            datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(resources.getColor(R.color.colorPrimary))
//            datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setBackgroundColor(resources.getColor(android.R.color.transparent))
//            datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(resources.getColor(R.color.colorPrimary))
//            datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setBackgroundColor(resources.getColor(android.R.color.transparent))
//        }
    }

    private fun setupBottomSheetTip() {
        bottomSheetTip = (activity as MainActivity).bottomSheetTip
        bottomSheetTipClose = (activity as MainActivity).bottomSheetTipClose

        bottomSheetTipClose?.setOnClickListener {
            hideSoftKeyboard()
            (activity as MainActivity).closeBottomSheetTips()
        }
    }

    private fun observeViewModel() {
        goalFormViewModel.getGoal()?.observe(this, Observer { goal ->
            this.goal = goal as Goal

            setupGoal()
        })

//        goalFormViewModel.getGoals()?.observe(this, Observer { goals ->
//            this.goals = goals
//        })

        goalFormViewModel.getHabits()?.observe(this, Observer { habits ->
            this.habits = habits
        })

//        goalFormViewModel.getItems()?.observe(this, Observer { items ->
//            this.items = items
//        })
//
//        goalFormViewModel.getHistory()?.observe(this, Observer { history ->
//            this.history = history
//        })

        goalFormViewModel.goalIdInserted.observe(this, Observer {
            navController.navigateUp()
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_save, menu)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_save -> {
                when {
                    checkIfAnyFieldsAreEmptyOrZero() -> { }
                    getGoalTypeSelected() == GoalType.GOAL_NONE -> {
                        showSnackBarLong(getString(R.string.goal_form_empty_type_goal))

                        hideSoftKeyboard()
                        goal_form_type_title.isFocusableInTouchMode = true
                        goal_form_type_title.requestFocus()
                    }
                    !validateDivideAndConquerValues() -> {
                        showSnackBarLong(getString(R.string.goal_form_gold_silver_bronze_order))

                        goal_form_bronze_value.requestFocus()
                    }
                    else -> {
                        val goalToSave = updateOrCreateGoal()

                        goalFormViewModel.saveGoal(goalToSave)

                        verifyFistTimeSaving()

                        return true
                    }
                }
            }
        }

        return false
    }

    private fun setSwitchImproov() {
        goal_form_switch_divide_and_conquer.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                isDivideAndConquer(true)
            } else {
                isDivideAndConquer(false)
            }
        }
    }

    private fun setRadioButtonType() {
        goal_form_radio_type_list.setOnClickListener {
            if (goal_form_radio_type_list.isChecked) {
                goal_form_radio_type_counter.isChecked = false
                goal_form_radio_type_total.isChecked = false

                goal_form_goal_counter.gone()

                if (goal.type != GoalType.GOAL_NONE && goal.type != GoalType.GOAL_LIST) {
                    showDialogOneButton()
                }
            }
        }

        goal_form_radio_type_counter.setOnClickListener {
            if (goal_form_radio_type_counter.isChecked) {
                goal_form_radio_type_list.isChecked = false
                goal_form_radio_type_total.isChecked = false

                goal_form_goal_counter.visible()

                if (goal.type != GoalType.GOAL_NONE && goal.type != GoalType.GOAL_COUNTER) {
                    showDialogOneButton()
                }
            }
        }

        goal_form_radio_type_total.setOnClickListener {
            if (goal_form_radio_type_total.isChecked) {
                goal_form_radio_type_counter.isChecked = false
                goal_form_radio_type_list.isChecked = false

                goal_form_goal_counter.gone()

                if (goal.type != GoalType.GOAL_NONE && goal.type != GoalType.GOAL_FINAL) {
                    showDialogOneButton()
                }
            }
        }
    }

    private fun isDivideAndConquer(isDivideAndConquer: Boolean) {
        goal.divideAndConquer = isDivideAndConquer

        if (isDivideAndConquer) {
            goal_form_single_value.resetValue()

            goal_form_divide_and_conquer.visible()
            goal_form_single.invisible()
        } else {
            goal_form_bronze_value.resetValue()
            goal_form_silver_value.resetValue()
            goal_form_gold_value.resetValue()

            goal_form_divide_and_conquer.invisible()
            goal_form_single.visible()
        }
    }

    private fun updateOrCreateGoal(): Goal {
        goal.name = goal_form_goal_name.text.toString()
        goal.divideAndConquer = goal_form_switch_divide_and_conquer.isChecked

        if (goal.goalId == 0L) {
            goal.createdDate = getCurrentTime()
            goal.value = 0F
            goal.done = false
            goal.type = getGoalTypeSelected()

            val order =
                if (goals.isEmpty() && habits.isEmpty()) 0
                else goals.size + habits.size + 1
            //else goals!![goals!!.size-1].order + 1

            goal.order = order
        } else {
            val oldGoalType = goal.type
            goal.type = getGoalTypeSelected()

            if (oldGoalType != goal.type) {
                items.forEach {
                    if (it.goalId == goal.goalId) {
                        goalFormViewModel.deleteItem(it)
                    }
                }

                history.forEach {
                    if (it.goalId == goal.goalId) {
                        goalFormViewModel.deleteHistoric(it)
                    }
                }
            }

            goal.updatedDate = getCurrentTime()
        }

        if (getGoalTypeSelected() == GoalType.GOAL_COUNTER) {
            goal.incrementValue = goal_form_goal_counter_inc_value.toFloat()
            goal.decrementValue = goal_form_goal_counter_dec_value.toFloat()
        }

        if (goal.divideAndConquer) {
            goal.bronzeValue = goal_form_bronze_value.toFloat()
            goal.silverValue = goal_form_silver_value.toFloat()
            goal.goldValue = goal_form_gold_value.toFloat()
        } else {
            goal.singleValue = goal_form_single_value.toFloat()
        }

        return goal
    }

    private fun getGoalTypeSelected(): GoalType {
        if (goal_form_radio_type_list.isChecked) return GoalType.GOAL_LIST
        if (goal_form_radio_type_counter.isChecked) return GoalType.GOAL_COUNTER
        if (goal_form_radio_type_total.isChecked) return GoalType.GOAL_FINAL

        return GoalType.GOAL_NONE
    }

    private fun setupGoal() {
        goal_form_goal_name.setText(goal.name)

        if (goal.date != null) {
            val myFormat = getString(R.string.date_format_dmy)
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            //goal_form_set_date.text = sdf.format(goal.date)

            cal.time = goal.date
        }

        if (goal.divideAndConquer) {
            goal_form_divide_and_conquer.visible()
            goal_form_single.invisible()

            goal_form_bronze_value.setText(goal.bronzeValue.getNumberInRightFormat())
            goal_form_silver_value.setText(goal.silverValue.getNumberInRightFormat())
            goal_form_gold_value.setText(goal.goldValue.getNumberInRightFormat())

            goal_form_switch_divide_and_conquer.isChecked = true
        } else {
            goal_form_single_value.setText(goal.singleValue.getNumberInRightFormat())
        }

        when (goal.type) {
            GoalType.GOAL_LIST -> {
                goal_form_radio_type_list.isChecked = true
            }
            GoalType.GOAL_COUNTER -> {
                goal_form_radio_type_counter.isChecked = true

                goal_form_goal_counter.visible()

                goal_form_goal_counter_inc_value.setText(goal.incrementValue.getNumberInRightFormat())
                goal_form_goal_counter_dec_value.setText(goal.decrementValue.getNumberInRightFormat())
            }
            GoalType.GOAL_FINAL -> {
                goal_form_radio_type_total.isChecked = true
            }
            else -> { }
        }
    }

    private fun verifyFistTimeSaving() {
        if (preferences.fistTimeAdd) {
            preferences.fistTimeAdd = false
            preferences.fistTimeList = true
        }
    }

    private fun showDialogOneButton() {
        val dialog = DialogOneButton()

        dialog.setMessage(getString(R.string.goal_form_goal_dialog_one_button_message))

        dialog.setOnClickListener(object : DialogOneButton.OnClickListener {
            override fun onOK() {
                dialog.dismiss()
            }
        })

        dialog.show(fragmentManager!!, "")
    }

    private fun checkIfAnyFieldsAreEmptyOrZero(): Boolean {
        return when {
            goal_form_goal_name.checkIfFieldIsEmptyOrZero() -> {
                goal_form_goal_name.fieldIsEmptyOrZero(this)
                true
            }
            goal_form_single_value.checkIfFieldIsEmptyOrZero() && !goal.divideAndConquer -> {
                goal_form_single_value.fieldIsEmptyOrZero(this)
                true
            }
            goal_form_bronze_value.checkIfFieldIsEmptyOrZero() && goal.divideAndConquer -> {
                goal_form_bronze_value.fieldIsEmptyOrZero(this)
                true
            }
            goal_form_silver_value.checkIfFieldIsEmptyOrZero() && goal.divideAndConquer -> {
                goal_form_silver_value.fieldIsEmptyOrZero(this)
                true
            }
            goal_form_gold_value.checkIfFieldIsEmptyOrZero() && goal.divideAndConquer -> {
                goal_form_gold_value.fieldIsEmptyOrZero(this)
                true
            }
            goal_form_goal_counter_dec_value.checkIfFieldIsEmptyOrZero() &&
                    (goal.type == GoalType.GOAL_COUNTER ||
                            getGoalTypeSelected() == GoalType.GOAL_COUNTER) -> {
                goal_form_goal_counter_dec_value.fieldIsEmptyOrZero(this)
                true
            }
            goal_form_goal_counter_inc_value.checkIfFieldIsEmptyOrZero() &&
                    (goal.type == GoalType.GOAL_COUNTER ||
                            getGoalTypeSelected() == GoalType.GOAL_COUNTER) -> {
                goal_form_goal_counter_inc_value.fieldIsEmptyOrZero(this)
                true
            }
            else -> false
        }
    }

    private fun validateDivideAndConquerValues(): Boolean {
        return try {
            val gold = goal_form_gold_value.toFloat()
            val silver = goal_form_silver_value.toFloat()
            val bronze = goal_form_bronze_value.toFloat()

            ((gold > silver) && (silver > bronze))
        } catch (e: Exception) {
            if (goal_form_single_value.isNotEmpty())
                return true
            false
        }
    }
}
