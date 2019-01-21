package com.rafaelfelipeac.domore.ui.fragments.goalform

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.*
import android.widget.Switch
import com.rafaelfelipeac.domore.R
import com.rafaelfelipeac.domore.models.Goal
import com.rafaelfelipeac.domore.ui.activities.MainActivity
import com.rafaelfelipeac.domore.ui.base.BaseFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_goal_form.*

class GoalFormFragment : BaseFragment() {

    private var viewModel: GoalFormViewModel?= null
    private var goalType = 0

    private var goal: Goal? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        (activity as MainActivity).supportActionBar?.title = "Nova meta"
        (activity as MainActivity).toolbar.inflateMenu(R.menu.menu_save)

        viewModel = ViewModelProviders.of(this).get(GoalFormViewModel::class.java)

        return inflater.inflate(R.layout.fragment_goal_form, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        goal = arguments?.let { GoalFormFragmentArgs.fromBundle(it).goal }

        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (goal != null) {
            setupGoal()
        }

        switchLista.setOnCheckedChangeListener { _, _ -> // option 1
            isCheckedInSwitch(switchLista)
        }

        switchTotalIncDec.setOnCheckedChangeListener { _, _ -> // option 2
            isCheckedInSwitch(switchTotalIncDec)
        }

        switchTotalValor.setOnCheckedChangeListener { _, _ -> // option 3
            isCheckedInSwitch(switchTotalValor)
        }

        form_goal_switch_trophies.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                goal?.trophies = true

                form_goal_trophies.visibility = View.VISIBLE
                form_goal_medal.visibility = View.INVISIBLE
            } else {
                goal?.trophies = false

                form_goal_trophies.visibility = View.INVISIBLE
                form_goal_medal.visibility = View.VISIBLE
            }
        }
    }

    private fun isCheckedInSwitch(switch: Switch) {
        when(switch) {
            switchLista -> {
                switchTotalIncDec.isChecked = false
                switchTotalValor.isChecked = false

                goalForm_goal_inc_dev.visibility = View.GONE

                goalType = 1
            }
            switchTotalIncDec -> {
                switchLista.isChecked = false
                switchTotalValor.isChecked = false

                goalForm_goal_inc_dev.visibility = View.VISIBLE

                goalType = 2
            }
            switchTotalValor -> {
                switchLista.isChecked = false
                switchTotalIncDec.isChecked = false

                goalForm_goal_inc_dev.visibility = View.GONE

                goalType = 3
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_save, menu)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            R.id.menu_goal_save -> {
                if (goal == null) {

                    val goalToSave = getNewGoal()
                    viewModel?.saveGoal(goalToSave)

                    val goal = goalDAO?.getAll()?.last() // with ID now

                    val action =
                        GoalFormFragmentDirections.actionGoalFormFragmentToGoalFragment(goal!!)
                    navController.navigate(action)

                    return true
                } else {

                    val goalToUpdate = getUpdateGoal()

                    viewModel?.updateGoal(goalToUpdate)

                    val action =
                        GoalFormFragmentDirections.actionGoalFormFragmentToGoalFragment(goalToUpdate)
                    navController.navigate(action)

                    return true
                }
            }
        }

        return false
    }

    private fun getNewGoal(): Goal {
        return Goal(
            name =
                if (goalForm_goal_name.text!!.isNotEmpty()) goalForm_goal_name.text.toString()
                else "abc" ,
            medalValue =
                if (form_goal_editText_medal.text!!.isNotEmpty()) form_goal_editText_medal.text.toString().toFloat()
                else 100F,
            finalValue = goalForm_goal_final_value.text.toString().toFloat(),
            value = 0F,
            initialDate = "",
            finalDate = "",
            type = goalType,
            done = false,
            order =
                if (goalDAO?.getAll()!!.isEmpty()) { 1 }
                else { goalDAO?.getAll()!![goalDAO!!.getAll().size-1].order + 1 },
            trophies = form_goal_switch_trophies.isChecked,
            bronzeValue =
                if (form_goal_editText_bronze.text!!.isNotEmpty()) form_goal_editText_bronze.text.toString().toFloat()
                else 100F,
            silverValue =
                if (form_goal_editText_silver.text!!.isNotEmpty()) form_goal_editText_silver.text.toString().toFloat()
                else 100F,
            goldValue =
                if (form_goal_editText_gold.text!!.isNotEmpty()) form_goal_editText_gold.text.toString().toFloat()
                else 100F,
            incrementValue =
                if (goalForm_goal_inc_value!!.text!!.isNotEmpty()) goalForm_goal_inc_value.text.toString().toFloat()
                else 0F,
            decrementValue =
                if (goalForm_goal_dec_value!!.text!!.isNotEmpty()) goalForm_goal_dec_value.text.toString().toFloat()
                else 0F)
    }

    private fun getUpdateGoal() : Goal {
        goal?.name = goalForm_goal_name.text.toString()
        goal?.trophies = form_goal_switch_trophies.isChecked
        goal?.type = getType()
        goal?.finalValue = goalForm_goal_final_value.text.toString().toFloat()

        if (getType() == 2) {
            goal?.incrementValue = goalForm_goal_inc_value.text.toString().toFloat()
            goal?.decrementValue = goalForm_goal_dec_value.text.toString().toFloat()
        }

        if (goal?.trophies!!) {
            goal?.bronzeValue = form_goal_editText_bronze.text.toString().toFloat()
            goal?.silverValue = form_goal_editText_silver.text.toString().toFloat()
            goal?.goldValue = form_goal_editText_gold.text.toString().toFloat()
        } else {
            goal?.medalValue = form_goal_editText_medal.text.toString().toFloat()
        }

        return goal!!
    }

    private fun getType(): Int {
        if (switchLista.isChecked)          return 1
        if (switchTotalIncDec.isChecked)    return 2
        if (switchTotalValor.isChecked)     return 3

        return -1
    }

    private fun setupGoal() {
        if (goal?.trophies!!) {
            form_goal_trophies.visibility = View.VISIBLE
            form_goal_medal.visibility = View.INVISIBLE

            form_goal_editText_bronze.setText(goal?.bronzeValue.toString())
            form_goal_editText_silver.setText(goal?.silverValue.toString())
            form_goal_editText_gold.setText(goal?.goldValue.toString())

            form_goal_switch_trophies.isChecked = true
        } else {
            form_goal_editText_medal.setText(goal?.medalValue.toString())
        }

        goalForm_goal_name.setText(goal?.name)
        goalForm_goal_final_value.setText(goal?.finalValue.toString())

        when(goal?.type) {
            1 -> {switchLista.isChecked = true}
            2 -> {
                switchTotalIncDec.isChecked = true

                goalForm_goal_inc_dev.visibility = View.VISIBLE

                goalForm_goal_inc_value.setText(goal?.incrementValue!!.toString())
                goalForm_goal_dec_value.setText(goal?.decrementValue!!.toString())
            }
            3 -> {switchTotalValor.isChecked = true}
        }
    }
}
