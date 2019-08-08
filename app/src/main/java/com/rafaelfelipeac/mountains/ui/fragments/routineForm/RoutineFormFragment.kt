package com.rafaelfelipeac.mountains.ui.fragments.routineForm

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
import com.rafaelfelipeac.mountains.extension.nextRoutineDate
import com.rafaelfelipeac.mountains.extension.setRoutineLastDate
import com.rafaelfelipeac.mountains.extension.visible
import com.rafaelfelipeac.mountains.models.*
import com.rafaelfelipeac.mountains.ui.activities.MainActivity
import com.rafaelfelipeac.mountains.ui.base.BaseFragment
import com.rafaelfelipeac.mountains.ui.fragments.routine.RoutineFragmentArgs
import kotlinx.android.synthetic.main.fragment_routine_form.*

class RoutineFormFragment : BaseFragment() {

    private var routine = Routine()
    private var routineId: Long? = null
    private var routines: List<Routine>? = null

    private lateinit var viewModel: RoutineFormViewModel

    private var bottomSheetTip: BottomSheetBehavior<*>? = null
    private var bottomSheetTipClose: ConstraintLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        routineId = arguments?.let { RoutineFragmentArgs.fromBundle(it).routineId }

        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        (activity as MainActivity).supportActionBar?.title = "Nova rotina"
        (activity as MainActivity).toolbar.inflateMenu(R.menu.menu_save)

        viewModel = ViewModelProviders.of(this).get(RoutineFormViewModel::class.java)

        routineId?.let { viewModel.init(it) }

        return inflater.inflate(R.layout.fragment_routine_form, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()

        (activity as MainActivity).openToolbar()
        (activity as MainActivity).closeBottomSheetFAB()

        setRadioRoutine()

        setDropdown()

        routine_form_help3.setOnClickListener {
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
                    val routineToSave = updateOrCreateRoutine()

                    viewModel.saveRoutine(routineToSave)

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

    private fun updateOrCreateRoutine(): Routine  {
        routine.name = routine_form_name.text.toString()
        routine.weekDays = getWeekDaysSelected()
        routine.userId = user.userId

        val routineType = getRoutineTypeSelected()

        if (routineType != RoutineType.ROUT_NONE) {
            routine.type = routineType

            when (routineType) {
                RoutineType.ROUT_1-> {}
                RoutineType.ROUT_2 -> {}
                RoutineType.ROUT_3 -> {
                    routine.periodType = getRoutinePeriodTypeSelected()
                    routine.periodTotal = routine_form_days_custom_1.text.toString().toInt()
                    routine.setRoutineLastDate()
                }
                RoutineType.ROUT_4 -> {
                    routine.periodType = PeriodType.PER_CUSTOM
                    routine.periodTotal = 1
                    routine.periodDaysBetween = routine_form_add_days.text.toString().toInt()
                    routine.setRoutineLastDate()
                }
                else -> { TODO() }
            }

            routine.nextRoutineDate()
        }

        return routine
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

    private fun getRoutineTypeSelected(): RoutineType {
        if (radioButton1.isChecked)         return RoutineType.ROUT_1
        if (radioButton2.isChecked)         return RoutineType.ROUT_2
        if (radioButton3.isChecked)         return RoutineType.ROUT_3
        if (radioButton4.isChecked)         return RoutineType.ROUT_4

        return RoutineType.ROUT_NONE
    }

    private fun getRoutinePeriodTypeSelected(): PeriodType {
        return when (routine_form_periods_spinner.selectedItem.toString()) {
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
        routine_form_periods_spinner.adapter = adapter
    }

    private fun setRadioRoutine() {
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

        viewModel.getRoutine()?.observe(this, Observer { routine ->
            this.routine = routine as Routine

            setupRoutine()
        })

        viewModel.getRoutines()?.observe(this, Observer { goals ->
            this.routines = goals.filter { it.userId == user.userId }
        })

        viewModel.routineIdInserted.observe(this, Observer { goalId ->
            val action = RoutineFormFragmentDirections.actionNavigationRoutineFormToNavigationRoutine(goalId)
            action.routineNew = true
            navController.navigate(action)
        })
    }

    private fun setupRoutine() {
        when(routine.type) {
            RoutineType.ROUT_1 -> { radioButton1.isChecked = true }
            RoutineType.ROUT_2 -> {
                radioButton2.isChecked = true

                block_of_radius2.visible()

                weekDay1.isChecked = routine.weekDays[0]
                weekDay2.isChecked = routine.weekDays[1]
                weekDay3.isChecked = routine.weekDays[2]
                weekDay4.isChecked = routine.weekDays[3]
                weekDay5.isChecked = routine.weekDays[4]
                weekDay6.isChecked = routine.weekDays[5]
                weekDay7.isChecked = routine.weekDays[6]
            }
            RoutineType.ROUT_3 -> {
                radioButton3.isChecked = true

                routine_form_days_custom_1.setText(routine.periodTotal.toString())

                routine_form_periods_spinner.setSelection(when (routine.periodType) {
                    PeriodType.PER_WEEK     -> 0
                    PeriodType.PER_MONTH    -> 1
                    PeriodType.PER_YEAR     -> 2
                    PeriodType.PER_CUSTOM -> TODO()
                    PeriodType.PER_NONE -> TODO()
                })

            }
            RoutineType.ROUT_4 -> {
                radioButton4.isChecked = true

                routine_form_add_days.setText(routine.periodDaysBetween.toString())
            }
            RoutineType.ROUT_NONE -> TODO()
        }
    }
}
