package com.rafaelfelipeac.mountains.ui.fragments.goalForm

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.rafaelfelipeac.mountains.R
import com.rafaelfelipeac.mountains.extension.*
import com.rafaelfelipeac.mountains.models.Goal
import com.rafaelfelipeac.mountains.ui.activities.MainActivity
import com.rafaelfelipeac.mountains.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_goal_form.*

class GoalFormFragment : BaseFragment() {

    private var goal: Goal = Goal()
    private var goalId: Long? = null
    private var goals: List<Goal>? = null

    private lateinit var viewModel: GoalFormViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        goalId = arguments?.let { GoalFormFragmentArgs.fromBundle(it).goalId }

        (activity as MainActivity).openToolbar()

        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        (activity as MainActivity).supportActionBar?.title = getString(R.string.fragment_title_goal_form)
        (activity as MainActivity).toolbar.inflateMenu(R.menu.menu_save)

        viewModel = ViewModelProviders.of(this).get(GoalFormViewModel::class.java)

        goalId?.let { viewModel.init(it) }

//        if (goalId != null) {
//            viewModel.init(goalId!!)
//        }

        return inflater.inflate(R.layout.fragment_goal_form, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()

        setRadioButtonType()
        setSwitchMountains()
    }

    private fun observeViewModel() {
        viewModel.getGoal()?.observe(this, Observer { goal ->
            this.goal = goal

            setupGoal()
        })

        viewModel.getGoals()?.observe(this, Observer { goals ->
            this.goals = goals.filter { it.userId == user.userId }
        })

        viewModel.goalIdInserted.observe(this, Observer { goalIdForm ->
            val action = GoalFormFragmentDirections.actionNavigationGoalFormToNavigationGoal(goalIdForm)
            navController.navigate(action)
        })
    }

    override fun onStart() {
        super.onStart()

        hideNavigation()
    }

    override fun onResume() {
        super.onResume()

        (activity as MainActivity).closeBottomSheetItem()
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
                } else if (getTypeSelected() == -1) {
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
        getTypeSelected() == 2 && (goalForm_goal_inc_value.isEmpty() || goalForm_goal_dec_value.isEmpty())

    private fun setSwitchMountains() {
        form_goal_switch_mountains.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                isMountains(true)
            } else {
                isMountains(false)
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

    private fun isMountains(isMountain: Boolean) {
        goal.mountains = isMountain

        if (isMountain) {
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

    private fun validateMountainsValues() : Boolean {
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
        goal.mountains = form_goal_switch_mountains.isChecked
        goal.type = getTypeSelected()

        if (goal.goalId == 0L) {
            goal.userId = user.userId
            goal.createdDate = getCurrentTime()
            goal.value = 0F
            goal.done = false


            val order =
                if (goals?.isEmpty()!!) 0
                else goals!![goals!!.size-1].order + 1

            goal.order = order
        }
        else
            goal.updatedDate = getCurrentTime()

        if (getTypeSelected() == 2) {
            goal.incrementValue = goalForm_goal_inc_value.toFloat()
            goal.decrementValue = goalForm_goal_dec_value.toFloat()
        }

        if (goal.mountains) {
            goal.bronzeValue = form_goal_editText_bronze.toFloat()
            goal.silverValue = form_goal_editText_silver.toFloat()
            goal.goldValue = form_goal_editText_gold.toFloat()
        } else {
            goal.singleValue = form_goal_editText_single.toFloat()
        }

        return goal
    }

    private fun getTypeSelected(): Int {
        if (radioButtonLista.isChecked)     return 1
        if (radioButtonIncDec.isChecked)    return 2
        if (radioButtonTotal.isChecked)     return 3

        return -1
    }

    private fun setupGoal() {
        goalForm_goal_name.setText(goal.name)

        if (goal.mountains) {
            form_goal_mountains.visible()
            form_goal_single.invisible()

            form_goal_editText_bronze.setText(goal.bronzeValue.getNumberInRightFormat())
            form_goal_editText_silver.setText(goal.silverValue.getNumberInRightFormat())
            form_goal_editText_gold.setText(goal.goldValue.getNumberInRightFormat())

            form_goal_switch_mountains.isChecked = true
        } else {
            form_goal_editText_single.setText(goal.singleValue.getNumberInRightFormat())
        }

        when(goal.type) {
            1 -> {radioButtonLista.isChecked = true}
            2 -> {
                radioButtonIncDec.isChecked = true

                goalForm_goal_inc_dev.visible()

                goalForm_goal_inc_value.setText(goal.incrementValue.getNumberInRightFormat())
                goalForm_goal_dec_value.setText(goal.decrementValue.getNumberInRightFormat())
            }
            3 -> {radioButtonTotal.isChecked = true}
        }
    }
}