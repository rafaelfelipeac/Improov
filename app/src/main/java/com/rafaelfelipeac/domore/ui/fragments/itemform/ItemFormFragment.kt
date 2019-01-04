package com.rafaelfelipeac.domore.ui.fragments.itemform

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.*
import com.rafaelfelipeac.domore.R
import com.rafaelfelipeac.domore.models.Goal
import com.rafaelfelipeac.domore.models.Item
import com.rafaelfelipeac.domore.ui.activities.MainActivity
import com.rafaelfelipeac.domore.ui.base.BaseFragment
import kotlinx.android.synthetic.main.activity_main.*

class ItemFormFragment : BaseFragment() {

    private var goal: Goal? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        (activity as MainActivity).toolbar.inflateMenu(R.menu.menu_save)
        (activity as MainActivity).supportActionBar?.title = "GoalsForm"

        return inflater.inflate(R.layout.fragment_item_form, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        goal = ItemFormFragmentArgs.fromBundle(arguments!!).itemGoal

        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_save, menu)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            R.id.menu_goal_save -> {
                val order =
                    if (itemDAO?.getAll()?.none { it.goalId == goal?.goalId && it.goalId != 0L }!!) { 1 }
                    else {
                        itemDAO?.getAll()
                            ?.filter { it.goalId == goal?.goalId && it.goalId != 0L }!![itemDAO!!.getAll()
                             .filter { it.goalId == goal?.goalId && it.goalId != 0L }.size-1].order + 1
                    }

                val item = Item(
                    goalId = goal?.goalId!!,
                    title = "a$order",
                    desc = "",
                    author = "",
                    order = order)

                itemDAO?.insert(item)

                val action =
                    ItemFormFragmentDirections.actionItemFormFragmentToGoalFragment(goal!!)
                navController.navigate(action)

                return true
            }
        }

        return false
    }
}
