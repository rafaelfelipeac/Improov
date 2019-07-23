package com.rafaelfelipeac.mountains.ui.fragments.today

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rafaelfelipeac.mountains.R
import com.rafaelfelipeac.mountains.extension.getPercentage
import com.rafaelfelipeac.mountains.models.Goal
import com.rafaelfelipeac.mountains.ui.activities.MainActivity
import com.rafaelfelipeac.mountains.ui.adapter.GoalsAdapter
import com.rafaelfelipeac.mountains.ui.base.BaseFragment
import com.rafaelfelipeac.mountains.ui.helper.SwipeAndDragHelperGoal
import kotlinx.android.synthetic.main.fragment_today.*

class TodayFragment : BaseFragment() {

    private var goalsDelayedAdapter = GoalsAdapter(this, false)
    private var goalsTodayAdapter = GoalsAdapter(this, false)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_today, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

        fab.setOnClickListener {
            navController.navigate(R.id.action_navigation_today_to_navigation_goalForm)
        }

        showNavigation()

        setItemsDelayed()
        setItemsToday()
    }

    override fun onResume() {
        super.onResume()

        (activity as MainActivity).closeToolbar()
    }

    private fun setItemsDelayed() {
        val goals = listOf(Goal(name = "1"), Goal(name = "2"), Goal(name = "3"), Goal(name = "4"), Goal(name = "5"))

        goals.sortedBy { it.order }.let { goalsDelayedAdapter.setItems(it) }

        goalsDelayedAdapter.clickListener = {
            val action = TodayFragmentDirections.actionNavigationTodayToNavigationGoal(it)
            navController.navigate(action)
        }

        val swipeAndDragHelper = SwipeAndDragHelperGoal(goalsDelayedAdapter)
        val touchHelper = ItemTouchHelper(swipeAndDragHelper)

        goalsDelayedAdapter.touchHelper = touchHelper

        today_delayed_list.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        today_delayed_list.adapter = goalsDelayedAdapter

        touchHelper.attachToRecyclerView(today_delayed_list)
    }

    private fun setItemsToday() {
        val goals = listOf(Goal(name = "1"), Goal(name = "2"), Goal(name = "3"), Goal(name = "4"), Goal(name = "5"))

        goals.sortedBy { it.order }.let { goalsTodayAdapter.setItems(it) }

        goalsTodayAdapter.clickListener = {
            val action = TodayFragmentDirections.actionNavigationTodayToNavigationGoal(it)
            navController.navigate(action)
        }

        val swipeAndDragHelper = SwipeAndDragHelperGoal(goalsTodayAdapter)
        val touchHelper = ItemTouchHelper(swipeAndDragHelper)

        goalsTodayAdapter.touchHelper = touchHelper

        today_today_list.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        today_today_list.adapter = goalsTodayAdapter

        touchHelper.attachToRecyclerView(today_today_list)
    }

    fun onViewSwiped(position: Int, direction: Int, holder: RecyclerView.ViewHolder, items: MutableList<Goal>) {
        val goal = items[position]

        when (direction) {
            ItemTouchHelper.RIGHT -> {
                if (goal.done || goal.getPercentage() >= 100) {
                    //doneOrUndoneGoal(goal)
                } else {
                    //setupItems()
                    //openBottomSheetDoneGoal(goal, ::doneOrUndoneGoal)
                }
            }
            ItemTouchHelper.LEFT -> {
                goal.archived = true
                goal.archiveDate = getCurrentTime()

                //viewModel.saveGoal(goal)
//
//                showSnackBarWithAction(
//                    holder.itemView,
//                    getString(R.string.goals_fragment_resolved_goal_message),
//                    goal as Any,
//                    ::archiveGoal
//                )
//
//                setupItems()
            }
        }
    }
}
