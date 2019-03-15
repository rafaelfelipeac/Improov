package com.rafaelfelipeac.domore.ui.fragments.goalform

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import com.rafaelfelipeac.domore.R
import com.rafaelfelipeac.domore.extension.getNumberInRightFormat
import com.rafaelfelipeac.domore.models.Goal
import com.rafaelfelipeac.domore.ui.activities.MainActivity
import com.rafaelfelipeac.domore.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_goal_form.*

class GoalFormFragment : BaseFragment() {

    private var viewModel: GoalFormViewModel?= null
    private var goalType = 0

    private var goal: Goal? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        (activity as MainActivity).supportActionBar?.title = "Nova meta"
        (activity as MainActivity).toolbar!!.inflateMenu(R.menu.menu_save)

        viewModel = ViewModelProviders.of(this).get(GoalFormViewModel::class.java)

        return inflater.inflate(R.layout.fragment_goal_form, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        goal = arguments?.let { GoalFormFragmentArgs.fromBundle(it).goal }

        setHasOptionsMenu(true)
    }

    override fun onStart() {
        super.onStart()

        hideNavigation()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (goal != null) {
            setupGoal()
        }

        setSwitchs()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_save, menu)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_goal_save -> {
                if (emptyFields()) {
                    showSnackBar(getString(R.string.message_some_empty_value))
                } else if (!validateTrophiesValues()) {
                    showSnackBar(getString(R.string.message_gold_silver_bronze_order))
                } else if (emptyIncOrDec()) {
                    showSnackBar(getString(R.string.message_empty_inc_dec))
                } else {
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
        }

        return false
    }

    override fun onResume() {
        super.onResume()

        (activity as MainActivity).closeBottomSheetAddItem()
    }

    private fun emptyIncOrDec() =
        getType() == 2 && (goalForm_goal_inc_value.text.toString().isEmpty() ||
                goalForm_goal_dec_value.text.toString().isEmpty())

    fun setSwitchs() {
        radioButtonLista.setOnClickListener {
            if (radioButtonLista.isChecked) {
                radioButtonIncDec.isChecked = false
                radioButtonTotal.isChecked = false

                goalForm_goal_inc_dev.visibility = View.GONE

                goalType = 1
            }
        }

        radioButtonIncDec.setOnClickListener {
            if (radioButtonIncDec.isChecked) {
                radioButtonLista.isChecked = false
                radioButtonTotal.isChecked = false

                goalForm_goal_inc_dev.visibility = View.VISIBLE

                goalType = 2
            } else
                goalForm_goal_inc_dev.visibility = View.GONE
        }

        radioButtonTotal.setOnClickListener {
            if (radioButtonTotal.isChecked) {
                radioButtonIncDec.isChecked = false
                radioButtonLista.isChecked = false

                goalForm_goal_inc_dev.visibility = View.GONE

                goalType = 3
            }
        }

        form_goal_switch_trophies.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                isTrophies(true)
            } else {
                isTrophies(false)
            }
        }
    }

    private fun isTrophies(isTrophy: Boolean) {
        goal?.trophies = isTrophy

        if (isTrophy) {
            form_goal_editText_medal.setText("")

            form_goal_trophies.visibility = View.VISIBLE
            form_goal_medal.visibility = View.INVISIBLE
        } else {
            form_goal_editText_bronze.setText("")
            form_goal_editText_silver.setText("")
            form_goal_editText_gold.setText("")

            form_goal_trophies.visibility = View.INVISIBLE
            form_goal_medal.visibility = View.VISIBLE
        }
    }

//    private fun isCheckedInSwitch(switch: Switch, isChecked: Boolean = false) {
//        when(switch) {
//            switchLista -> {
//                switchTotalIncDec.isChecked = false
//                switchTotalValor.isChecked = false
//
//                goalForm_goal_inc_dev.visibility = View.GONE
//
//                goalType = 1
//            }
//            switchTotalIncDec -> {
//                switchLista.isChecked = false
//                switchTotalValor.isChecked = false
//
//                if (isChecked) goalForm_goal_inc_dev.visibility = View.VISIBLE
//                else goalForm_goal_inc_dev.visibility = View.GONE
//
//                goalType = 2
//            }
//            switchTotalValor -> {
//                switchLista.isChecked = false
//                switchTotalIncDec.isChecked = false
//
//                goalForm_goal_inc_dev.visibility = View.GONE
//
//                goalType = 3
//            }
//        }
//    }

    private fun validateTrophiesValues() : Boolean {
        return try {
            val gold = form_goal_editText_gold.text!!.toString().toFloat()
            val silver = form_goal_editText_silver.text.toString().toFloat()
            val bronze = form_goal_editText_bronze.text.toString().toFloat()

            ((gold > silver) && (silver > bronze))
        } catch (e: Exception) {
            if (form_goal_editText_medal.text!!.toString().isNotEmpty())
                return true
            false
        }
    }

    private fun emptyFields(): Boolean {
        val nameEmpty = goalForm_goal_name.text!!.toString().isEmpty()
        val medalEmpty = form_goal_editText_medal.text!!.toString().isEmpty()
        val trophiesEmpty =
                    form_goal_editText_bronze.text!!.toString().isEmpty() ||
                    form_goal_editText_silver.text!!.toString().isEmpty() ||
                    form_goal_editText_gold.text!!.toString().isEmpty()

        if ((medalEmpty && trophiesEmpty) || nameEmpty)
            return true
        return false
    }

    private fun getNewGoal(): Goal {
        val order =
            if (goalDAO?.getAll()?.size == 0) 0
            else goalDAO?.getAll()!![goalDAO!!.getAll().size-1].order + 1

        val goal = Goal(
            name =  goalForm_goal_name.text.toString(),
            value = 0F,
            type = goalType,
            done = false,
            order = order,
            trophies = form_goal_switch_trophies.isChecked,
            createdDate = getCurrentTime()
        )

        if (goal.trophies) {
            goal.bronzeValue = form_goal_editText_bronze.text.toString().toFloat()
            goal.silverValue = form_goal_editText_silver.text.toString().toFloat()
            goal.goldValue = form_goal_editText_gold.text.toString().toFloat()
        } else {
            goal.medalValue = form_goal_editText_medal.text.toString().toFloat()
        }

        if (goal.type == 2) {
            goal.incrementValue = goalForm_goal_inc_value.text.toString().toFloat()
            goal.decrementValue = goalForm_goal_dec_value.text.toString().toFloat()
        }

        return goal
    }

    private fun getUpdateGoal() : Goal {
        goal?.name = goalForm_goal_name.text.toString()
        goal?.trophies = form_goal_switch_trophies.isChecked
        goal?.type = getType()
        goal?.updatedDate = getCurrentTime()

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
        if (radioButtonLista.isChecked)     return 1
        if (radioButtonIncDec.isChecked)    return 2
        if (radioButtonTotal.isChecked)     return 3

        return -1
    }

    private fun setupGoal() {
        if (goal?.trophies!!) {
            form_goal_trophies.visibility = View.VISIBLE
            form_goal_medal.visibility = View.INVISIBLE

            form_goal_editText_bronze.setText(goal?.bronzeValue?.getNumberInRightFormat())
            form_goal_editText_silver.setText(goal?.silverValue?.getNumberInRightFormat())
            form_goal_editText_gold.setText(goal?.goldValue?.getNumberInRightFormat())

            form_goal_switch_trophies.isChecked = true
        } else {
            form_goal_editText_medal.setText(goal?.medalValue?.getNumberInRightFormat())
        }

        goalForm_goal_name.setText(goal?.name)

        when(goal?.type) {
            1 -> {radioButtonLista.isChecked = true}
            2 -> {
                radioButtonIncDec.isChecked = true

                goalForm_goal_inc_dev.visibility = View.VISIBLE

                goalForm_goal_inc_value.setText(goal?.incrementValue?.getNumberInRightFormat())
                goalForm_goal_dec_value.setText(goal?.decrementValue?.getNumberInRightFormat())
            }
            3 -> {radioButtonTotal.isChecked = true}
        }
    }
}
