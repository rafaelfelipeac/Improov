package com.rafaelfelipeac.mountains.ui.fragments.otherGoalForm

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
import com.rafaelfelipeac.mountains.extension.nextRepetitionDate
import com.rafaelfelipeac.mountains.extension.setRepetitionLastDate
import com.rafaelfelipeac.mountains.extension.visible
import com.rafaelfelipeac.mountains.models.Goal
import com.rafaelfelipeac.mountains.models.PeriodType
import com.rafaelfelipeac.mountains.models.RepetitionType
import com.rafaelfelipeac.mountains.ui.activities.MainActivity
import com.rafaelfelipeac.mountains.ui.base.BaseFragment
import com.rafaelfelipeac.mountains.ui.fragments.goalForm.GoalFormFragmentArgs
import kotlinx.android.synthetic.main.fragment_other_goal_form.*

class OtherGoalFormFragment : BaseFragment() {

    private var goal: Goal = Goal()
    private var goalId: Long? = null
    private var goals: List<Goal>? = null

    private lateinit var viewModel: OtherGoalFormViewModel

    private var bottomSheetTip: BottomSheetBehavior<*>? = null
    private var bottomSheetTipClose: ConstraintLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        goalId = arguments?.let { GoalFormFragmentArgs.fromBundle(it).goalId }

        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        (activity as MainActivity).supportActionBar?.title = getString(R.string.fragment_title_goal_form)
        (activity as MainActivity).toolbar.inflateMenu(R.menu.menu_save)

        viewModel = ViewModelProviders.of(this).get(OtherGoalFormViewModel::class.java)

        goalId?.let { viewModel.init(it) }

        return inflater.inflate(R.layout.fragment_other_goal_form, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()

        (activity as MainActivity).openToolbar()
        (activity as MainActivity).closeBottomSheetFAB()

        setRadioRepetition()

        setDropdown()

        goalForm_help3.setOnClickListener {
            (activity as MainActivity).setupBottomSheetTipsTwo()
            setupBottomSheetTip()
            (activity as MainActivity).openBottomSheetTips()
        }
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
                    val goalToSave = updateOrCreateGoal()

                    viewModel.saveGoal(goalToSave)

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

    private fun updateOrCreateGoal(): Goal  {
        goal.name = other_goalForm_goal_name.text.toString()
        goal.repetitionWeekDays = getWeekDaysSelected()
        goal.userId = user.userId

        val repetitionType = getRepetitionTypeSelected()

        if (repetitionType != RepetitionType.REP_NONE) {
            goal.repetition = true
            goal.repetitionType = repetitionType

            when (repetitionType) {
                RepetitionType.REP1-> {}
                RepetitionType.REP2 -> {}
                RepetitionType.REP3 -> {
                    goal.repetitionPeriodType = getRepetitionPeriodTypeSelected()
                    goal.repetitionPeriodTotal = goal_form_days_custom_1.text.toString().toInt()
                    goal.setRepetitionLastDate()
                }
                RepetitionType.REP4 -> {
                    goal.repetitionPeriodType = PeriodType.PER_CUSTOM
                    goal.repetitionPeriodTotal = 1
                    goal.repetitionPeriodDaysBetween = goal_form_add_days.text.toString().toInt()
                    goal.setRepetitionLastDate()
                }
                else -> { TODO() }
            }

            goal.nextRepetitionDate()
        }

        return goal
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

    private fun getRepetitionTypeSelected(): RepetitionType {
        if (radioButton1.isChecked)         return RepetitionType.REP1
        if (radioButton2.isChecked)         return RepetitionType.REP2
        if (radioButton3.isChecked)         return RepetitionType.REP3
        if (radioButton4.isChecked)         return RepetitionType.REP4

        return RepetitionType.REP_NONE
    }

    private fun getRepetitionPeriodTypeSelected(): PeriodType {
        return when (periods_spinner.selectedItem.toString()) {
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
        periods_spinner.adapter = adapter
    }

    private fun setRadioRepetition() {
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

        viewModel.getGoal()?.observe(this, Observer { goal ->
            this.goal = goal

            setupGoal()
        })

        viewModel.getGoals()?.observe(this, Observer { goals ->
            this.goals = goals.filter { it.userId == user.userId }
        })

        viewModel.goalIdInserted.observe(this, Observer { goalIdForm ->
            val action = OtherGoalFormFragmentDirections.actionNavigationOtherGoalFormToNavigationOtherGoal()
            action.goalNew = true
            navController.navigate(action)
        })
    }

    private fun setupGoal() {
        when(goal.repetitionType) {
            RepetitionType.REP1 -> { radioButton1.isChecked = true }
            RepetitionType.REP2 -> {
                radioButton2.isChecked = true

                block_of_radius2.visible()

                weekDay1.isChecked = goal.repetitionWeekDays[0]
                weekDay2.isChecked = goal.repetitionWeekDays[1]
                weekDay3.isChecked = goal.repetitionWeekDays[2]
                weekDay4.isChecked = goal.repetitionWeekDays[3]
                weekDay5.isChecked = goal.repetitionWeekDays[4]
                weekDay6.isChecked = goal.repetitionWeekDays[5]
                weekDay7.isChecked = goal.repetitionWeekDays[6]
            }
            RepetitionType.REP3 -> {
                radioButton3.isChecked = true

                goal_form_days_custom_1.setText(goal.repetitionPeriodTotal.toString())

                periods_spinner.setSelection(when (goal.repetitionPeriodType) {
                    PeriodType.PER_WEEK     -> 0
                    PeriodType.PER_MONTH    -> 1
                    PeriodType.PER_YEAR     -> 2
                    PeriodType.PER_CUSTOM -> TODO()
                    PeriodType.PER_NONE -> TODO()
                })

            }
            RepetitionType.REP4 -> {
                radioButton4.isChecked = true

                goal_form_add_days.setText(goal.repetitionPeriodDaysBetween.toString())
            }
            RepetitionType.REP_NONE -> TODO()
        }
    }

    override fun onStart() {
        super.onStart()

        hideNavigation()
    }
}
