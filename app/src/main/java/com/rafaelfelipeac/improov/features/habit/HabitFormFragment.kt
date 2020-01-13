package com.rafaelfelipeac.improov.features.habit

import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.rafaelfelipeac.improov.R
import com.rafaelfelipeac.improov.core.extension.gone
import com.rafaelfelipeac.improov.core.extension.nextHabitDate
import com.rafaelfelipeac.improov.core.extension.setHabitLastDate
import com.rafaelfelipeac.improov.core.extension.visible
import com.rafaelfelipeac.improov.core.platform.base.BaseFragment
import com.rafaelfelipeac.improov.features.commons.Habit
import com.rafaelfelipeac.improov.features.commons.HabitType
import com.rafaelfelipeac.improov.features.commons.PeriodType
import com.rafaelfelipeac.improov.features.main.MainActivity
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
        (activity as MainActivity).supportActionBar?.title = getString(R.string.habit_form_title)
        (activity as MainActivity).toolbar.inflateMenu(R.menu.menu_save)

        hideNavigation()

        if (habitId != 0L) { habitId?.let { habitFormViewModel.init(it) } }

        return inflater.inflate(R.layout.fragment_habit_form, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()

        (activity as MainActivity).openToolbar()

        setRadioHabit()

        setDropdown()

        habit_form_help.setOnClickListener {
            (activity as MainActivity).setupBottomSheetTipsTwo()
            setupBottomSheetTip()
            (activity as MainActivity).openBottomSheetTips()
        }

        habit_form_option_1.setOnClickListener { radioButtonChecked(1) }
        habit_form_option_2.setOnClickListener { radioButtonChecked(2) ;  habit_form_week.visible()}
        habit_form_option_3.setOnClickListener { radioButtonChecked(3) }
        habit_form_option_4.setOnClickListener { radioButtonChecked(4) }

        habit_form_period_days.setOnFocusChangeListener { _, hasFocus -> if (hasFocus) { radioButtonChecked(3) } }

        habit_form_periods_spinner.setOnTouchListener { _, _ -> radioButtonChecked(3) ; false }

        habit_form_add_days.setOnFocusChangeListener { _, hasFocus -> if (hasFocus) { radioButtonChecked(4) } }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_save, menu)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_save -> {
//                if (verifyIfFieldsAreEmpty()) {
//                    showSnackBar(getString(R.string.message_some_empty_value))
//                } else if (getGoalTypeSelected() == GoalType.GOAL_NONE) {
//                    showSnackBar(getString(R.string.message_empty_type_goal))
//                } else if (!validateDivideAndConquerValues()) {
//                    showSnackBar(getString(R.string.message_gold_silver_bronze_order))
//                } else if (verifyIfCounterValuesAreEmpty()) {
//                    showSnackBar(getString(R.string.message_empty_counter))
//                } else {
                val habitToSave = updateOrCreateHabit()

                habitFormViewModel.saveHabit(habitToSave)

                verifyFistTimeSaving()

                return true
                // }
            }
        }

        return false
    }

    private fun radioButtonChecked(pos: Int) {
        when (pos) {
            1 -> {
                habit_form_radio_everyday.isChecked = true
                habit_form_radio_weekdays.isChecked = false
                habit_form_radio_period.isChecked = false
                habit_form_radio_custom.isChecked = false
            }
            2 -> {
                habit_form_radio_everyday.isChecked = false
                habit_form_radio_weekdays.isChecked = true
                habit_form_radio_period.isChecked = false
                habit_form_radio_custom.isChecked = false
            }
            3 -> {
                habit_form_radio_everyday.isChecked = false
                habit_form_radio_weekdays.isChecked = false
                habit_form_radio_period.isChecked = true
                habit_form_radio_custom.isChecked = false
            }
            4 -> {
                habit_form_radio_everyday.isChecked = false
                habit_form_radio_weekdays.isChecked = false
                habit_form_radio_period.isChecked = false
                habit_form_radio_custom.isChecked = true
            }
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

    private fun updateOrCreateHabit(): Habit {
        habit.name = habit_form_name.text.toString()
        habit.weekDays = getWeekDaysSelected()

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
                    habit.periodTotal = habit_form_period_days.text.toString().toInt()
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

        weekDays.add(habit_form_week_sunday.isChecked)
        weekDays.add(habit_form_week_monday.isChecked)
        weekDays.add(habit_form_week_tuesday.isChecked)
        weekDays.add(habit_form_week_wednesday.isChecked)
        weekDays.add(habit_form_week_thursday.isChecked)
        weekDays.add(habit_form_week_friday.isChecked)
        weekDays.add(habit_form_week_saturday.isChecked)

        return weekDays
    }

    private fun getHabitTypeSelected(): HabitType {
        if (habit_form_radio_everyday.isChecked) return HabitType.HAB_EVERYDAY
        if (habit_form_radio_weekdays.isChecked) return HabitType.HAB_WEEKDAYS
        if (habit_form_radio_period.isChecked) return HabitType.HAB_PERIOD
        if (habit_form_radio_custom.isChecked) return HabitType.HAB_CUSTOM

        return HabitType.HAB_NONE
    }

    private fun getHabitPeriodTypeSelected(): PeriodType {
        return when (habit_form_periods_spinner.selectedItem.toString()) {
            context?.getString(R.string.habit_form_period_type_week) -> {
                PeriodType.PER_WEEK
            }
            context?.getString(R.string.habit_form_period_type_month) -> {
                PeriodType.PER_MONTH
            }
            context?.getString(R.string.habit_form_period_type_year) -> {
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
        habit_form_radio_everyday.setOnClickListener {
            if (habit_form_radio_everyday.isChecked) {
                radioButtonChecked(1)

                habit_form_week.gone()
            }
        }

        habit_form_radio_weekdays.setOnClickListener {
            if (habit_form_radio_weekdays.isChecked) {
                radioButtonChecked(2)

                habit_form_week.visible()
            }
        }

        habit_form_radio_period.setOnClickListener {
            if (habit_form_radio_period.isChecked) {
                radioButtonChecked(3)

                habit_form_week.gone()
            }
        }

        habit_form_radio_custom.setOnClickListener {
            if (habit_form_radio_custom.isChecked) {
                radioButtonChecked(4)

                habit_form_week.gone()
            }
        }
    }

    private fun observeViewModel() {
        habitFormViewModel.getHabit()?.observe(this, Observer { habit ->
            this.habit = habit as Habit

            setupHabit()
        })

        habitFormViewModel.getHabits()?.observe(this, Observer { goals ->
            this.habits = goals
        })

        habitFormViewModel.habitIdInserted.observe(this, Observer {
            navController.navigateUp()
        })
    }

    private fun setupHabit() {
        habit_form_name.setText(habit.name)

        when (habit.type) {
            HabitType.HAB_EVERYDAY -> {
                habit_form_radio_everyday.isChecked = true
            }
            HabitType.HAB_WEEKDAYS -> {
                habit_form_radio_weekdays.isChecked = true

                habit_form_week.visible()

                habit_form_week_sunday.isChecked = habit.weekDays[0]
                habit_form_week_monday.isChecked = habit.weekDays[1]
                habit_form_week_tuesday.isChecked = habit.weekDays[2]
                habit_form_week_wednesday.isChecked = habit.weekDays[3]
                habit_form_week_thursday.isChecked = habit.weekDays[4]
                habit_form_week_friday.isChecked = habit.weekDays[5]
                habit_form_week_saturday.isChecked = habit.weekDays[6]
            }
            HabitType.HAB_PERIOD -> {
                habit_form_radio_period.isChecked = true

                habit_form_period_days.setText(habit.periodTotal.toString())

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
                habit_form_radio_custom.isChecked = true

                habit_form_add_days.setText(habit.periodDaysBetween.toString())
            }
            HabitType.HAB_NONE -> TODO()
        }
    }

    private fun verifyFistTimeSaving() {
        if (preferences.fistTimeAdd) {
            preferences.fistTimeAdd = false
            preferences.fistTimeList = true
        }
    }
}
