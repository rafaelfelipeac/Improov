package com.rafaelfelipeac.mountains.ui.fragments.habitForm

import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.rafaelfelipeac.mountains.R
import com.rafaelfelipeac.mountains.extension.gone
import com.rafaelfelipeac.mountains.extension.nextHabitDate
import com.rafaelfelipeac.mountains.extension.setHabitLastDate
import com.rafaelfelipeac.mountains.extension.visible
import com.rafaelfelipeac.mountains.models.*
import com.rafaelfelipeac.mountains.ui.activities.MainActivity
import com.rafaelfelipeac.mountains.ui.base.BaseFragment
import com.rafaelfelipeac.mountains.ui.fragments.habit.HabitFragmentArgs
import kotlinx.android.synthetic.main.fragment_habit_form.*

class HabitFormFragment : BaseFragment() {

    private var habit = Habit()
    private var habitId: Long? = null
    private var habits: List<Habit>? = null

    private lateinit var viewModel: HabitFormViewModel

    private var bottomSheetTip: BottomSheetBehavior<*>? = null
    private var bottomSheetTipClose: ConstraintLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        habitId = arguments?.let { HabitFragmentArgs.fromBundle(it).habitId }

        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        (activity as MainActivity).supportActionBar?.title = "Novo hábito"
        (activity as MainActivity).toolbar.inflateMenu(R.menu.menu_save)

        viewModel = ViewModelProviders.of(this).get(HabitFormViewModel::class.java)

        habitId?.let { viewModel.init(it) }

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
//                } else if (getGoalTypeSelected() == GoalType.INVALID) {
//                    showSnackBar(getString(R.string.message_empty_type_goal))
//                } else if (!validateMountainsValues()) {
//                    showSnackBar(getString(R.string.message_gold_silver_bronze_order))
//                } else if (verifyIfIncOrDecValuesAreEmpty()) {
//                    showSnackBar(getString(R.string.message_empty_inc_dec))
//                } else {
                    val habitToSave = updateOrCreateHabit()

                    viewModel.saveHabit(habitToSave)

                    return true
               // }
            }
        }

        return false
    }

    private fun setupBottomSheetTip() {
        bottomSheetTip = (activity as MainActivity).bottomSheetTip
        bottomSheetTipClose = (activity as MainActivity).bottomSheetTipClose

        bottomSheetTipClose?.setOnClickListener {
            hideSoftKeyboard(view!!, activity)
            (activity as MainActivity).closeBottomSheetTips()
        }
    }

    private fun updateOrCreateHabit(): Habit  {
        habit.name = habit_form_name.text.toString()
        habit.weekDays = getWeekDaysSelected()
        habit.userId = user.userId

        val habitType = gethabitTypeSelected()

        if (habitType != HabitType.HABIT_NONE) {
            habit.type = habitType

            when (habitType) {
                HabitType.HABIT_1-> {}
                HabitType.HABIT_2 -> {}
                HabitType.HABIT_3 -> {
                    habit.periodType = gethabitPeriodTypeSelected()
                    habit.periodTotal = habit_form_days_custom_1.text.toString().toInt()
                    habit.setHabitLastDate()
                }
                HabitType.HABIT_4 -> {
                    habit.periodType = PeriodType.PER_CUSTOM
                    habit.periodTotal = 1
                    habit.periodDaysBetween = habit_form_add_days.text.toString().toInt()
                    habit.setHabitLastDate()
                }
                else -> { TODO() }
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

    private fun gethabitTypeSelected(): HabitType {
        if (radioButton1.isChecked)         return HabitType.HABIT_1
        if (radioButton2.isChecked)         return HabitType.HABIT_2
        if (radioButton3.isChecked)         return HabitType.HABIT_3
        if (radioButton4.isChecked)         return HabitType.HABIT_4

        return HabitType.HABIT_NONE
    }

    private fun gethabitPeriodTypeSelected(): PeriodType {
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
                radioButton2.isChecked = false
                radioButton3.isChecked = false
                radioButton4.isChecked = false

                block_of_radius2.gone()
            }
        }

        radioButton2.setOnClickListener {
            if (radioButton2.isChecked) {
                radioButton1.isChecked = false
                radioButton3.isChecked = false
                radioButton4.isChecked = false

                block_of_radius2.visible()
            }
        }

        radioButton3.setOnClickListener {
            if (radioButton3.isChecked) {
                radioButton1.isChecked = false
                radioButton2.isChecked = false
                radioButton4.isChecked = false

                block_of_radius2.gone()
            }
        }

        radioButton4.setOnClickListener {
            if (radioButton4.isChecked) {
                radioButton1.isChecked = false
                radioButton2.isChecked = false
                radioButton3.isChecked = false

                block_of_radius2.gone()
            }
        }
    }

    private fun observeViewModel() {
        viewModel.user?.observe(this, Observer { user ->
            (activity as MainActivity).user = user
        })

        viewModel.getHabit()?.observe(this, Observer { habit ->
            this.habit = habit as Habit

            setupHabit()
        })

        viewModel.getHabits()?.observe(this, Observer { goals ->
            this.habits = goals.filter { it.userId == user.userId }
        })

        viewModel.habitIdInserted.observe(this, Observer { goalId ->
            val action = HabitFormFragmentDirections.actionNavigationHabitFormToNavigationHabit(goalId)
            action.habitNew = true
            navController.navigate(action)
        })
    }

    private fun setupHabit() {
        when(habit.type) {
            HabitType.HABIT_1 -> { radioButton1.isChecked = true }
            HabitType.HABIT_2 -> {
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
            HabitType.HABIT_3 -> {
                radioButton3.isChecked = true

                habit_form_days_custom_1.setText(habit.periodTotal.toString())

                habit_form_periods_spinner.setSelection(when (habit.periodType) {
                    PeriodType.PER_WEEK     -> 0
                    PeriodType.PER_MONTH    -> 1
                    PeriodType.PER_YEAR     -> 2
                    PeriodType.PER_CUSTOM -> TODO()
                    PeriodType.PER_NONE -> TODO()
                })

            }
            HabitType.HABIT_4 -> {
                radioButton4.isChecked = true

                habit_form_add_days.setText(habit.periodDaysBetween.toString())
            }
            HabitType.HABIT_NONE -> TODO()
        }
    }
}
