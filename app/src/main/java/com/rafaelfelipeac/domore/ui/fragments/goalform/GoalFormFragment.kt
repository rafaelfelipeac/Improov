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
import kotlinx.android.synthetic.main.fragment_goal.*
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

        if (goal != null) {
            setupGoal()
        }

        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

                goalType = 1
            }
            switchTotalIncDec -> {
                switchLista.isChecked = false
                switchTotalValor.isChecked = false

                goalType = 2
            }
            switchTotalValor -> {
                switchLista.isChecked = false
                switchTotalIncDec.isChecked = false

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
                val order =
                        if (goalDAO?.getAll()!!.isEmpty()) { 1 }
                        else { goalDAO?.getAll()!![goalDAO!!.getAll().size-1].order + 1 }

                viewModel?.saveGoal(
                    Goal(name =
                            if (goalForm_goal_name.text!!.isNotEmpty()) goalForm_goal_name.text.toString()
                            else "abc" ,
                        medalValue =
                            if (form_goal_editText_medal.text!!.isNotEmpty()) form_goal_editText_medal.text.toString().toFloat()
                            else 100F,
                        actualValue = 0F,
                        initialDate = "",
                        finalDate = "",
                        type = goalType,
                        done = false,
                        order = order,
                        trophies = form_goal_switch_trophies.isChecked,
                        bronzeValue =
                            if (form_goal_editText_bronze.text!!.isNotEmpty()) form_goal_editText_bronze.text.toString().toFloat()
                            else 100F,
                        silverValue =
                            if (form_goal_editText_silver.text!!.isNotEmpty()) form_goal_editText_silver.text.toString().toFloat()
                            else 100F,
                        goldValue =
                            if (form_goal_editText_gold.text!!.isNotEmpty()) form_goal_editText_gold.text.toString().toFloat()
                            else 100F)
                )

                val goal = goalDAO?.getAll()?.last()

                val action =
                    GoalFormFragmentDirections.actionGoalFormFragmentToGoalFragment(goal!!)
                navController.navigate(action)

                return true
            }
        }

        return false
    }

    private fun setupGoal() {

    }
}