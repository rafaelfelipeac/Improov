package com.rafaelfelipeac.mountains.features.goal.presentation.goalform

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.rafaelfelipeac.mountains.R
import com.rafaelfelipeac.mountains.core.extension.*
import com.rafaelfelipeac.mountains.core.platform.base.BaseFragment
import com.rafaelfelipeac.mountains.features.commons.Goal
import com.rafaelfelipeac.mountains.features.commons.GoalType
import com.rafaelfelipeac.mountains.features.commons.Habit
import com.rafaelfelipeac.mountains.features.main.MainActivity
import kotlinx.android.synthetic.main.fragment_goal_form.*
import java.text.SimpleDateFormat
import java.util.*

class GoalFormFragment : BaseFragment() {

    private var goal: Goal = Goal()
    private var goalId: Long? = null
    private var goals: List<Goal> = listOf()
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
        setSwitchMountains()

        (activity as MainActivity).openToolbar()

        goal_form_divide_and_conquest_help.setOnClickListener {
            (activity as MainActivity).setupBottomSheetTipsOne()
            setupBottomSheetTip()
            (activity as MainActivity).openBottomSheetTips()
        }

        goal_form_type_help.setOnClickListener {
            (activity as MainActivity).setupBottomSheetTipsTwo()
            setupBottomSheetTip()
            (activity as MainActivity).openBottomSheetTips()
        }

        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val myFormat = getString(R.string.date_format_dmy)
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            goal_form_set_date.text = sdf.format(cal.time)

            goal.finalDate = cal.time
        }

        goal_form_set_date.setOnClickListener {
            val datePickerDialog = DatePickerDialog(context!!,
                R.style.DialogTheme,
                dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH))

            datePickerDialog.show()

            datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(resources.getColor(R.color.colorPrimary))
            datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setBackgroundColor(resources.getColor(android.R.color.transparent))
            datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(resources.getColor(R.color.colorPrimary))
            datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setBackgroundColor(resources.getColor(android.R.color.transparent))
        }
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

        goalFormViewModel.getGoals()?.observe(this, Observer { goals ->
            this.goals = goals
        })

        goalFormViewModel.getHabits()?.observe(this, Observer { habits ->
            this.habits = habits
        })

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
                if (verifyIfFieldsAreEmpty()) {
                    showSnackBar(getString(R.string.goal_form_some_empty_value))
                } else if (getGoalTypeSelected() == GoalType.GOAL_NONE) {
                    showSnackBar(getString(R.string.goal_form_empty_type_goal))
                } else if (!validateMountainsValues()) {
                    showSnackBar(getString(R.string.goal_form_gold_silver_bronze_order))
                } else if (verifyIfIncOrDecValuesAreEmpty()) {
                    showSnackBar(getString(R.string.goal_form_empty_inc_dec))
                } else {
                    val goalToSave = updateOrCreateGoal()

                    goalFormViewModel.saveGoal(goalToSave)

                    verifyFistTimeSaving()

                    return true
                }
            }
        }

        return false
    }

    private fun verifyIfIncOrDecValuesAreEmpty() =
        getGoalTypeSelected() == GoalType.GOAL_COUNTER && (goal_form_goal_inc_value.isEmpty() || goal_form_goal_dec_value.isEmpty())

    private fun setSwitchMountains() {
        goal_form_switch_divide_and_conquest.setOnCheckedChangeListener { _, isChecked ->
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
                goal_form_radio_type_inc_dec.isChecked = false
                goal_form_radio_type_total.isChecked = false

                goal_form_goal_inc_dev.gone()
            }
        }

        goal_form_radio_type_inc_dec.setOnClickListener {
            if (goal_form_radio_type_inc_dec.isChecked) {
                goal_form_radio_type_list.isChecked = false
                goal_form_radio_type_total.isChecked = false

                goal_form_goal_inc_dev.visible()
            }
        }

        goal_form_radio_type_total.setOnClickListener {
            if (goal_form_radio_type_total.isChecked) {
                goal_form_radio_type_inc_dec.isChecked = false
                goal_form_radio_type_list.isChecked = false

                goal_form_goal_inc_dev.gone()
            }
        }
    }

    private fun isDivideAndConquer(isDivideAndConquer: Boolean) {
        goal.divideAndConquer = isDivideAndConquer

        if (isDivideAndConquer) {
            goal_form_single_value.resetValue()

            goal_form_mountains.visible()
            goal_form_single.invisible()
        } else {
            goal_form_editText_bronze.resetValue()
            goal_form_silver_value.resetValue()
            goal_form_gold_value.resetValue()

            goal_form_mountains.invisible()
            goal_form_single.visible()
        }
    }

    private fun validateMountainsValues(): Boolean {
        return try {
            val gold = goal_form_gold_value.toFloat()
            val silver = goal_form_silver_value.toFloat()
            val bronze = goal_form_editText_bronze.toFloat()

            ((gold > silver) && (silver > bronze))
        } catch (e: Exception) {
            if (goal_form_single_value.isNotEmpty())
                return true
            false
        }
    }

    private fun verifyIfFieldsAreEmpty(): Boolean {
        val nameEmpty = goal_form_goal_name.isEmpty()
        val singleEmpty = goal_form_single_value.isEmpty()
        val mountainsEmpty =
            goal_form_editText_bronze.isEmpty() ||
                    goal_form_silver_value.isEmpty() ||
                    goal_form_gold_value.isEmpty()

        if ((singleEmpty && mountainsEmpty) || nameEmpty)
            return true
        return false
    }

    private fun updateOrCreateGoal(): Goal {
        goal.name = goal_form_goal_name.text.toString()
        goal.divideAndConquer = goal_form_switch_divide_and_conquest.isChecked
        goal.type = getGoalTypeSelected()

        if (goal.goalId == 0L) {
            goal.createdDate = getCurrentTime()
            goal.value = 0F
            goal.done = false

            val order =
                if (goals.isEmpty() && habits.isEmpty()) 0
                else goals.size + habits.size + 1
            //else goals!![goals!!.size-1].order + 1

            goal.order = order
        } else
            goal.updatedDate = getCurrentTime()

        if (getGoalTypeSelected() == GoalType.GOAL_COUNTER) {
            goal.incrementValue = goal_form_goal_inc_value.toFloat()
            goal.decrementValue = goal_form_goal_dec_value.toFloat()
        }

        if (goal.divideAndConquer) {
            goal.bronzeValue = goal_form_editText_bronze.toFloat()
            goal.silverValue = goal_form_silver_value.toFloat()
            goal.goldValue = goal_form_gold_value.toFloat()
        } else {
            goal.singleValue = goal_form_single_value.toFloat()
        }

        return goal
    }

    private fun getGoalTypeSelected(): GoalType {
        if (goal_form_radio_type_list.isChecked) return GoalType.GOAL_LIST
        if (goal_form_radio_type_inc_dec.isChecked) return GoalType.GOAL_COUNTER
        if (goal_form_radio_type_total.isChecked) return GoalType.GOAL_FINAL

        return GoalType.GOAL_NONE
    }

    private fun setupGoal() {
        goal_form_goal_name.setText(goal.name)

        if (goal.finalDate != null) {
            val myFormat = getString(R.string.date_format_dmy)
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            goal_form_set_date.text = sdf.format(goal.finalDate)

            cal.time = goal.finalDate
        }

        if (goal.divideAndConquer) {
            goal_form_mountains.visible()
            goal_form_single.invisible()

            goal_form_editText_bronze.setText(goal.bronzeValue.getNumberInRightFormat())
            goal_form_silver_value.setText(goal.silverValue.getNumberInRightFormat())
            goal_form_gold_value.setText(goal.goldValue.getNumberInRightFormat())

            goal_form_switch_divide_and_conquest.isChecked = true
        } else {
            goal_form_single_value.setText(goal.singleValue.getNumberInRightFormat())
        }

        when (goal.type) {
            GoalType.GOAL_LIST -> {
                goal_form_radio_type_list.isChecked = true
            }
            GoalType.GOAL_COUNTER -> {
                goal_form_radio_type_inc_dec.isChecked = true

                goal_form_goal_inc_dev.visible()

                goal_form_goal_inc_value.setText(goal.incrementValue.getNumberInRightFormat())
                goal_form_goal_dec_value.setText(goal.decrementValue.getNumberInRightFormat())
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
}
