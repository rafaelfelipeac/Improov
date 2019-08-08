package com.rafaelfelipeac.mountains.ui.fragments.repetitionForm

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
import com.rafaelfelipeac.mountains.models.*
import com.rafaelfelipeac.mountains.ui.activities.MainActivity
import com.rafaelfelipeac.mountains.ui.base.BaseFragment
import com.rafaelfelipeac.mountains.ui.fragments.repetition.RepetitionFragmentArgs
import kotlinx.android.synthetic.main.fragment_repetition_form.*

class RepetitionFormFragment : BaseFragment() {

    private var repetition = Repetition()
    private var repetitionId: Long? = null
    private var repetitions: List<Repetition>? = null

    private lateinit var viewModel: RepetitionFormViewModel

    private var bottomSheetTip: BottomSheetBehavior<*>? = null
    private var bottomSheetTipClose: ConstraintLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        repetitionId = arguments?.let { RepetitionFragmentArgs.fromBundle(it).repetitionId }

        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        (activity as MainActivity).supportActionBar?.title = getString(R.string.fragment_title_goal_form)
        (activity as MainActivity).toolbar.inflateMenu(R.menu.menu_save)

        viewModel = ViewModelProviders.of(this).get(RepetitionFormViewModel::class.java)

        repetitionId?.let { viewModel.init(it) }

        return inflater.inflate(R.layout.fragment_repetition_form, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()

        (activity as MainActivity).openToolbar()
        (activity as MainActivity).closeBottomSheetFAB()

        setRadioRepetition()

        setDropdown()

        repetition_form_help3.setOnClickListener {
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
                    val repetitionToSave = updateOrCreateRepetition()

                    viewModel.saveRepetition(repetitionToSave)

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

    private fun updateOrCreateRepetition(): Repetition  {
        repetition.name = repetition_form_name.text.toString()
        repetition.weekDays = getWeekDaysSelected()
        repetition.userId = user.userId

        val repetitionType = getRepetitionTypeSelected()

        if (repetitionType != RepetitionType.REP_NONE) {
            repetition.type = repetitionType

            when (repetitionType) {
                RepetitionType.REP1-> {}
                RepetitionType.REP2 -> {}
                RepetitionType.REP3 -> {
                    repetition.periodType = getRepetitionPeriodTypeSelected()
                    repetition.periodTotal = repetition_form_days_custom_1.text.toString().toInt()
                    repetition.setRepetitionLastDate()
                }
                RepetitionType.REP4 -> {
                    repetition.periodType = PeriodType.PER_CUSTOM
                    repetition.periodTotal = 1
                    repetition.periodDaysBetween = repetition_form_add_days.text.toString().toInt()
                    repetition.setRepetitionLastDate()
                }
                else -> { TODO() }
            }

            repetition.nextRepetitionDate()
        }

        return repetition
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
        return when (repetition_form_periods_spinner.selectedItem.toString()) {
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
        repetition_form_periods_spinner.adapter = adapter
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

        viewModel.getRepetition()?.observe(this, Observer { repetition ->
            this.repetition = repetition as Repetition

            setupRepetition()
        })

        viewModel.getRepetitions()?.observe(this, Observer { goals ->
            this.repetitions = goals.filter { it.userId == user.userId }
        })

        viewModel.repetitionIdInserted.observe(this, Observer { goalId ->
            val action = RepetitionFormFragmentDirections.actionNavigationRepetitionFormToNavigationRepetition(goalId)
            action.repetitionNew = true
            navController.navigate(action)
        })
    }

    private fun setupRepetition() {
        when(repetition.type) {
            RepetitionType.REP1 -> { radioButton1.isChecked = true }
            RepetitionType.REP2 -> {
                radioButton2.isChecked = true

                block_of_radius2.visible()

                weekDay1.isChecked = repetition.weekDays[0]
                weekDay2.isChecked = repetition.weekDays[1]
                weekDay3.isChecked = repetition.weekDays[2]
                weekDay4.isChecked = repetition.weekDays[3]
                weekDay5.isChecked = repetition.weekDays[4]
                weekDay6.isChecked = repetition.weekDays[5]
                weekDay7.isChecked = repetition.weekDays[6]
            }
            RepetitionType.REP3 -> {
                radioButton3.isChecked = true

                repetition_form_days_custom_1.setText(repetition.periodTotal.toString())

                repetition_form_periods_spinner.setSelection(when (repetition.periodType) {
                    PeriodType.PER_WEEK     -> 0
                    PeriodType.PER_MONTH    -> 1
                    PeriodType.PER_YEAR     -> 2
                    PeriodType.PER_CUSTOM -> TODO()
                    PeriodType.PER_NONE -> TODO()
                })

            }
            RepetitionType.REP4 -> {
                radioButton4.isChecked = true

                repetition_form_add_days.setText(repetition.periodDaysBetween.toString())
            }
            RepetitionType.REP_NONE -> TODO()
        }
    }
}
