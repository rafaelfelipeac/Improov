package com.rafaelfelipeac.mountains.ui.fragments.goalForm

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.rafaelfelipeac.mountains.R
import com.rafaelfelipeac.mountains.extension.*
import com.rafaelfelipeac.mountains.models.Goal
import com.rafaelfelipeac.mountains.models.GoalType
import com.rafaelfelipeac.mountains.models.Habit
import com.rafaelfelipeac.mountains.ui.activities.MainActivity
import com.rafaelfelipeac.mountains.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_goal_form.*
import java.text.SimpleDateFormat
import java.util.*

class GoalFormFragment : BaseFragment() {

    private var goal: Goal = Goal()
    private var goalId: Long? = null
    private var goals: List<Goal> = listOf()
    private var habits: List<Habit> = listOf()

    private var cal = Calendar.getInstance()

    private lateinit var viewModel: GoalFormViewModel

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

        viewModel = ViewModelProviders.of(this).get(GoalFormViewModel::class.java)

        goalId?.let { viewModel.init(it) }

        return inflater.inflate(R.layout.fragment_goal_form, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()

        setRadioButtonType()
        setSwitchMountains()

        (activity as MainActivity).openToolbar()
        (activity as MainActivity).closeBottomSheetFAB()

        goalForm_help.setOnClickListener {
            (activity as MainActivity).setupBottomSheetTipsOne()
            setupBottomSheetTip()
            (activity as MainActivity).openBottomSheetTips()
        }

        goalForm_help2.setOnClickListener {
            (activity as MainActivity).setupBottomSheetTipsTwo()
            setupBottomSheetTip()
            (activity as MainActivity).openBottomSheetTips()
        }

        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val myFormat = "dd/MM/yyyy"
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            goal_form_date.text = sdf.format(cal.time)

            goal.finalDate = cal.time
        }

        goal_form_date.setOnClickListener {
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
            hideSoftKeyboard(view!!, activity)
            (activity as MainActivity).closeBottomSheetTips()
        }
    }

    private fun observeViewModel() {
        viewModel.user?.observe(this, Observer { user ->
            (activity as MainActivity).user = user
        })

        viewModel.getGoal()?.observe(this, Observer { goal ->
            this.goal = goal as Goal

            setupGoal()
        })

        viewModel.getGoals()?.observe(this, Observer { goals ->
            this.goals = goals.filter { it.userId == user.userId }
        })

        viewModel.getHabits()?.observe(this, Observer { habits ->
            this.habits = habits.filter { it.userId == user.userId }
        })

        viewModel.goalIdInserted.observe(this, Observer { goalIdForm ->
            val action = GoalFormFragmentDirections.actionNavigationGoalFormToNavigationGoal(goalIdForm)
            action.goalNew = true
            navController.navigate(action)
        })
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
                if (verifyIfFieldsAreEmpty()) {
                    showSnackBar(getString(R.string.message_some_empty_value))
                } else if (getGoalTypeSelected() == GoalType.GOAL_NONE) {
                    showSnackBar(getString(R.string.message_empty_type_goal))
                } else if (!validateMountainsValues()) {
                    showSnackBar(getString(R.string.message_gold_silver_bronze_order))
                } else if (verifyIfIncOrDecValuesAreEmpty()) {
                    showSnackBar(getString(R.string.message_empty_inc_dec))
                } else {
                    val goalToSave = updateOrCreateGoal()

                    viewModel.saveGoal(goalToSave)

                    return true
                }
            }
        }

        return false
    }

    private fun verifyIfIncOrDecValuesAreEmpty() =
        getGoalTypeSelected() == GoalType.GOAL_COUNTER && (goalForm_goal_inc_value.isEmpty() || goalForm_goal_dec_value.isEmpty())

    private fun setSwitchMountains() {
        form_goal_switch_mountains.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                isDivideAndConquer(true)
            } else {
                isDivideAndConquer(false)
            }
        }
    }

    private fun setRadioButtonType() {
        radioButtonLista.setOnClickListener {
            if (radioButtonLista.isChecked) {
                radioButtonIncDec.isChecked = false
                radioButtonTotal.isChecked = false

                goalForm_goal_inc_dev.gone()
            }
        }

        radioButtonIncDec.setOnClickListener {
            if (radioButtonIncDec.isChecked) {
                radioButtonLista.isChecked = false
                radioButtonTotal.isChecked = false

                goalForm_goal_inc_dev.visible()
            }
        }

        radioButtonTotal.setOnClickListener {
            if (radioButtonTotal.isChecked) {
                radioButtonIncDec.isChecked = false
                radioButtonLista.isChecked = false

                goalForm_goal_inc_dev.gone()
            }
        }
    }

    private fun isDivideAndConquer(isDivideAndConquer: Boolean) {
        goal.divideAndConquer = isDivideAndConquer

        if (isDivideAndConquer) {
            form_goal_editText_single.resetValue()

            form_goal_mountains.visible()
            form_goal_single.invisible()
        } else {
            form_goal_editText_bronze.resetValue()
            form_goal_editText_silver.resetValue()
            form_goal_editText_gold.resetValue()

            form_goal_mountains.invisible()
            form_goal_single.visible()
        }
    }

    private fun validateMountainsValues(): Boolean {
        return try {
            val gold = form_goal_editText_gold.toFloat()
            val silver = form_goal_editText_silver.toFloat()
            val bronze = form_goal_editText_bronze.toFloat()

            ((gold > silver) && (silver > bronze))
        } catch (e: Exception) {
            if (form_goal_editText_single.isNotEmpty())
                return true
            false
        }
    }

    private fun verifyIfFieldsAreEmpty(): Boolean {
        val nameEmpty = goalForm_goal_name.isEmpty()
        val singleEmpty = form_goal_editText_single.isEmpty()
        val mountainsEmpty =
            form_goal_editText_bronze.isEmpty() ||
                    form_goal_editText_silver.isEmpty() ||
                    form_goal_editText_gold.isEmpty()

        if ((singleEmpty && mountainsEmpty) || nameEmpty)
            return true
        return false
    }

    private fun updateOrCreateGoal(): Goal {
        goal.name = goalForm_goal_name.text.toString()
        goal.divideAndConquer = form_goal_switch_mountains.isChecked
        goal.type = getGoalTypeSelected()

        if (goal.goalId == 0L) {
            goal.userId = user.userId
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
            goal.incrementValue = goalForm_goal_inc_value.toFloat()
            goal.decrementValue = goalForm_goal_dec_value.toFloat()
        }

        if (goal.divideAndConquer) {
            goal.bronzeValue = form_goal_editText_bronze.toFloat()
            goal.silverValue = form_goal_editText_silver.toFloat()
            goal.goldValue = form_goal_editText_gold.toFloat()
        } else {
            goal.singleValue = form_goal_editText_single.toFloat()
        }

        return goal
    }

    private fun getGoalTypeSelected(): GoalType {
        if (radioButtonLista.isChecked) return GoalType.GOAL_LIST
        if (radioButtonIncDec.isChecked) return GoalType.GOAL_COUNTER
        if (radioButtonTotal.isChecked) return GoalType.GOAL_FINAL

        return GoalType.GOAL_NONE
    }

    private fun setupGoal() {
        goalForm_goal_name.setText(goal.name)

        if (goal.finalDate != null) {
            val myFormat = "dd/MM/yyyy"
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            goal_form_date.text = sdf.format(goal.finalDate)

            cal.time = goal.finalDate
        }

        if (goal.divideAndConquer) {
            form_goal_mountains.visible()
            form_goal_single.invisible()

            form_goal_editText_bronze.setText(goal.bronzeValue.getNumberInRightFormat())
            form_goal_editText_silver.setText(goal.silverValue.getNumberInRightFormat())
            form_goal_editText_gold.setText(goal.goldValue.getNumberInRightFormat())

            form_goal_switch_mountains.isChecked = true
        } else {
            form_goal_editText_single.setText(goal.singleValue.getNumberInRightFormat())
        }

        when (goal.type) {
            GoalType.GOAL_LIST -> {
                radioButtonLista.isChecked = true
            }
            GoalType.GOAL_COUNTER -> {
                radioButtonIncDec.isChecked = true

                goalForm_goal_inc_dev.visible()

                goalForm_goal_inc_value.setText(goal.incrementValue.getNumberInRightFormat())
                goalForm_goal_dec_value.setText(goal.decrementValue.getNumberInRightFormat())
            }
            GoalType.GOAL_FINAL -> {
                radioButtonTotal.isChecked = true
            }
            else -> {
            }
        }
    }
}
