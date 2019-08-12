package com.rafaelfelipeac.mountains.features.habit.presentation

import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.rafaelfelipeac.mountains.R
import com.rafaelfelipeac.mountains.core.extension.gone
import com.rafaelfelipeac.mountains.core.extension.nextHabitDate
import com.rafaelfelipeac.mountains.core.extension.setHabitLastDate
import com.rafaelfelipeac.mountains.core.extension.visible
import com.rafaelfelipeac.mountains.core.platform.BaseFragment
import com.rafaelfelipeac.mountains.features.habit.Habit
import com.rafaelfelipeac.mountains.features.habit.HabitType
import com.rafaelfelipeac.mountains.features.habit.PeriodType
import com.rafaelfelipeac.mountains.features.main.MainActivity
import kotlinx.android.synthetic.main.fragment_habit_form.*

class HabitFormFragment : BaseFragment() {

    private var habit = Habit()
    private var habitId: Long? = null
    private var habits: List<Habit>? = null

    private var bottomSheetTip: BottomSheetBehavior<*>? = null
    private var bottomSheetTipClose: ConstraintLayout? = null

    private val habitFormViewModel by lazy { viewModelFactory.get<HabitFormViewModel>(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        injector.inject(this)

        habitId = arguments?.let { HabitFragmentArgs.fromBundle(it).habitId }

        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        (activity as MainActivity).supportActionBar?.title = "Novo hábito"
        (activity as MainActivity).toolbar.inflateMenu(R.menu.menu_save)

        habitId?.let { habitFormViewModel.init(it) }

        return inflater.inflate(R.layout.fragment_habit_form, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()

        (activity as MainActivity).openToolbar()
        (activity as MainActivity).closeBottomSheetFAB()

        setRadioHabit()

        setDropdown()

        habit_form_help3.setOnClickListener {
            (activity as MainActivity).setupBottomSheetTipsTwo()
            setupBottomSheetTip()
            (activity as MainActivity).openBottomSheetTips()
        }

        habit_form_option_1.setOnClickListener { radioButtonChecked(1) }

        habit_form_option_2.setOnClickListener { radioButtonChecked(2) ;  block_of_radius2.visible()}

        habit_form_option_3.setOnClickListener { radioButtonChecked(3) }

        habit_form_option_4.setOnClickListener { radioButtonChecked(4) }

        habit_form_days_custom_1.setOnFocusChangeListener { _, hasFocus -> if (hasFocus) { radioButtonChecked(3) } }

        habit_form_periods_spinner.setOnTouchListener { _, _ -> radioButtonChecked(3) ; false }

        habit_form_add_days.setOnFocusChangeListener { _, hasFocus -> if (hasFocus) { radioButtonChecked(4) } }
    }

    override fun onStart() {
        super.onStart()

        hideNavigation()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_save, menu)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_goal_save -> {
//                if (verifyIfFieldsAreEmpty()) {
//                    showSnackBar(getString(R.string.message_some_empty_value))
//                } else if (getGoalTypeSelected() == GoalType.GOAL_NONE) {
//                    showSnackBar(getString(R.string.message_empty_type_goal))
//                } else if (!validateMountainsValues()) {
//                    showSnackBar(getString(R.string.message_gold_silver_bronze_order))
//                } else if (verifyIfIncOrDecValuesAreEmpty()) {
//                    showSnackBar(getString(R.string.message_empty_inc_dec))
//                } else {
                val habitToSave = updateOrCreateHabit()

                habitFormViewModel.saveHabit(habitToSave)

                return true
                // }
            }
        }

        return false
    }

    private fun radioButtonChecked(pos: Int) {
        when (pos) {
            1 -> {
                radioButton1.isChecked = true
                radioButton2.isChecked = false
                radioButton3.isChecked = false
                radioButton4.isChecked = false
            }
            2 -> {
                radioButton1.isChecked = false
                radioButton2.isChecked = true
                radioButton3.isChecked = false
                radioButton4.isChecked = false
            }
            3 -> {
                radioButton1.isChecked = false
                radioButton2.isChecked = false
                radioButton3.isChecked = true
                radioButton4.isChecked = false
            }
            4 -> {
                radioButton1.isChecked = false
                radioButton2.isChecked = false
                radioButton3.isChecked = false
                radioButton4.isChecked = true
            }
        }
    }

    private fun setupBottomSheetTip() {
        bottomSheetTip = (activity as MainActivity).bottomSheetTip
        bottomSheetTipClose = (activity as MainActivity).bottomSheetTipClose

        bottomSheetTipClose?.setOnClickListener {
            hideSoftKeyboard(view!!, activity)
            (activity as MainActivity).closeBottomSheetTips()
        }
    }

    private fun updateOrCreateHabit(): Habit {
        habit.name = habit_form_name.text.toString()
        habit.weekDays = getWeekDaysSelected()
        habit.userId = user.userId

        val habitType = getHabitTypeSelected()

        if (habitType != HabitType.HAB_NONE) {
            habit.type = habitType

            when (habitType) {
                HabitType.HAB_EVERYDAY -> {
                }
                HabitType.HAB_WEEKDAYS -> {
                }
                HabitType.HAB_PERIOD -> {
                    habit.periodType = getHabitPeriodTypeSelected()
                    habit.periodTotal = habit_form_days_custom_1.text.toString().toInt()
                    habit.setHabitLastDate()
                }
                HabitType.HAB_CUSTOM -> {
                    habit.periodType = PeriodType.PER_CUSTOM
                    habit.periodTotal = 1
                    habit.periodDaysBetween = habit_form_add_days.text.toString().toInt()
                    habit.setHabitLastDate()
                }
                else -> {
                    TODO()
                }
            }

            habit.nextHabitDate()
        }

        return habit
    }

    private fun getWeekDaysSelected(): MutableList<Boolean> {
        val weekDays = mutableListOf<Boolean>()

        weekDays.add(weekDay1.isChecked)
        weekDays.add(weekDay2.isChecked)
        weekDays.add(weekDay3.isChecked)
        weekDays.add(weekDay4.isChecked)
        weekDays.add(weekDay5.isChecked)
        weekDays.add(weekDay6.isChecked)
        weekDays.add(weekDay7.isChecked)

        return weekDays
    }

    private fun getHabitTypeSelected(): HabitType {
        if (radioButton1.isChecked) return HabitType.HAB_EVERYDAY
        if (radioButton2.isChecked) return HabitType.HAB_WEEKDAYS
        if (radioButton3.isChecked) return HabitType.HAB_PERIOD
        if (radioButton4.isChecked) return HabitType.HAB_CUSTOM

        return HabitType.HAB_NONE
    }

    private fun getHabitPeriodTypeSelected(): PeriodType {
        return when (habit_form_periods_spinner.selectedItem.toString()) {
            "semana" -> {
                PeriodType.PER_WEEK
            }
            "mês" -> {
                PeriodType.PER_MONTH
            }
            "ano" -> {
                PeriodType.PER_YEAR
            }
            else ->
                PeriodType.PER_NONE
        }
    }

    private fun setDropdown() {
        val adapter = ArrayAdapter.createFromResource(context!!, R.array.periods_array, R.layout.spinner_item)
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
        habit_form_periods_spinner.adapter = adapter
    }

    private fun setRadioHabit() {
        radioButton1.setOnClickListener {
            if (radioButton1.isChecked) {
                radioButtonChecked(1)

                block_of_radius2.gone()
            }
        }

        radioButton2.setOnClickListener {
            if (radioButton2.isChecked) {
                radioButtonChecked(2)

                block_of_radius2.visible()
            }
        }

        radioButton3.setOnClickListener {
            if (radioButton3.isChecked) {
                radioButtonChecked(3)

                block_of_radius2.gone()
            }
        }

        radioButton4.setOnClickListener {
            if (radioButton4.isChecked) {
                radioButtonChecked(4)

                block_of_radius2.gone()
            }
        }
    }

    private fun observeViewModel() {
        habitFormViewModel.user?.observe(this, Observer { user ->
            (activity as MainActivity).user = user
        })

        habitFormViewModel.getHabit()?.observe(this, Observer { habit ->
            this.habit = habit as Habit

            setupHabit()
        })

        habitFormViewModel.getHabits()?.observe(this, Observer { goals ->
            this.habits = goals.filter { it.userId == user.userId }
        })

        habitFormViewModel.habitIdInserted.observe(this, Observer { goalId ->
            val action =
                HabitFormFragmentDirections.actionNavigationHabitFormToNavigationHabit(
                    goalId
                )
            action.habitNew = true
            navController.navigate(action)
        })
    }

    private fun setupHabit() {
        habit_form_name.setText(habit.name)

        when (habit.type) {
            HabitType.HAB_EVERYDAY -> {
                radioButton1.isChecked = true
            }
            HabitType.HAB_WEEKDAYS -> {
                radioButton2.isChecked = true

                block_of_radius2.visible()

                weekDay1.isChecked = habit.weekDays[0]
                weekDay2.isChecked = habit.weekDays[1]
                weekDay3.isChecked = habit.weekDays[2]
                weekDay4.isChecked = habit.weekDays[3]
                weekDay5.isChecked = habit.weekDays[4]
                weekDay6.isChecked = habit.weekDays[5]
                weekDay7.isChecked = habit.weekDays[6]
            }
            HabitType.HAB_PERIOD -> {
                radioButton3.isChecked = true

                habit_form_days_custom_1.setText(habit.periodTotal.toString())

                habit_form_periods_spinner.setSelection(
                    when (habit.periodType) {
                        PeriodType.PER_WEEK -> 0
                        PeriodType.PER_MONTH -> 1
                        PeriodType.PER_YEAR -> 2
                        PeriodType.PER_CUSTOM -> TODO()
                        PeriodType.PER_NONE -> TODO()
                    }
                )

            }
            HabitType.HAB_CUSTOM -> {
                radioButton4.isChecked = true

                habit_form_add_days.setText(habit.periodDaysBetween.toString())
            }
            HabitType.HAB_NONE -> TODO()
        }
    }
}
