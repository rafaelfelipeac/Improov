package com.rafaelfelipeac.readmore.ui.fragments

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.*
import com.rafaelfelipeac.readmore.R
import com.rafaelfelipeac.readmore.app.App
import com.rafaelfelipeac.readmore.database.AppDatabase
import com.rafaelfelipeac.readmore.database.GoalDAO
import com.rafaelfelipeac.readmore.models.Goal
import com.rafaelfelipeac.readmore.ui.activities.MainActivity
import com.rafaelfelipeac.readmore.ui.base.BaseFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_goal_form.*

class GoalFormFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        (activity as MainActivity).supportActionBar?.title = "GoalForm"

        (activity as MainActivity).toolbar.inflateMenu(R.menu.menu_save)

        return inflater.inflate(R.layout.fragment_goal_form, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_save, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            R.id.menu_goal_save -> {
                saveGoal()
                hideSoftKeyboard(activity!!)
                return true
            }
        }

        return false
    }

    private fun saveGoal() {
        val goal = Goal(
            goalForm_goal_name.text.toString(),
            goalForm_goal_total.text.toString().toInt(),
            0,
            "",
            "")


        goalDAO?.insert(goal)

        var goalsAdded = goalDAO?.getAll()
    }
}
