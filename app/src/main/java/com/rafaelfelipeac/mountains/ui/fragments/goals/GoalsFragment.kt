package com.rafaelfelipeac.mountains.ui.fragments.goals

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rafaelfelipeac.mountains.R
import com.rafaelfelipeac.mountains.extension.getPercentage
import com.rafaelfelipeac.mountains.extension.invisible
import com.rafaelfelipeac.mountains.extension.visible
import com.rafaelfelipeac.mountains.models.Goal
import com.rafaelfelipeac.mountains.ui.activities.MainActivity
import com.rafaelfelipeac.mountains.ui.adapter.GoalsAdapter
import com.rafaelfelipeac.mountains.ui.base.BaseFragment
import com.rafaelfelipeac.mountains.ui.helper.SwipeAndDragHelperGoal
import kotlinx.android.synthetic.main.fragment_goals.*

class GoalsFragment : BaseFragment() {

    private var goalsAdapter = GoalsAdapter(this)

    var goals: List<Goal>? = null

    private lateinit var viewModel: GoalsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        injector.inject(this)

        (activity as MainActivity).closeToolbar()

        setHasOptionsMenu(true)

        (activity as MainActivity).bottomNavigationVisible(View.VISIBLE)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        (activity as MainActivity).supportActionBar?.title = getString(R.string.fragment_title_goals)

        viewModel = ViewModelProviders.of(this).get(GoalsViewModel::class.java)

        return inflater.inflate(R.layout.fragment_goals, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()

        (activity as MainActivity).closeBottomSheetDoneGoal()
        (activity as MainActivity).closeBottomSheetItem()

        showNavigation()
    }

    private fun observeViewModel() {
        viewModel.user?.observe(this, Observer { user ->
            (activity as MainActivity).user = user
        })

        viewModel.getGoals()?.observe(this, Observer { goals ->
            this.goals = goals.filter { it.userId == user.userId && !it.archived}

            setupItems()
        })
    }

    override fun onResume() {
        super.onResume()

        (activity as MainActivity).closeToolbar()

        (activity as MainActivity).closeBottomSheetItem()
    }

    private fun setupItems() {
        if (goals?.isNotEmpty()!!) {
            setItems()

            goals_list.visible()
            goals_placeholder.invisible()
        } else {
            goals_list.invisible()
            goals_placeholder.visible()
        }
    }

    private fun setItems() {
        goals
            ?.sortedBy { it.order }
            ?.let { goalsAdapter.setItems(it) }

        goalsAdapter.clickListener = {
            val action = GoalsFragmentDirections.actionNavigationGoalsToNavigationGoal(it)
            navController.navigate(action)
        }

        val swipeAndDragHelper = SwipeAndDragHelperGoal(goalsAdapter)
        val touchHelper = ItemTouchHelper(swipeAndDragHelper)

        goalsAdapter.touchHelper = touchHelper

        goals_list.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        goals_list.adapter = goalsAdapter

        touchHelper.attachToRecyclerView(goals_list)
    }

    fun onViewSwiped(position: Int, direction: Int, holder: RecyclerView.ViewHolder, items: MutableList<Goal>) {
        val goal = items[position]

        when (direction) {
            ItemTouchHelper.RIGHT -> {
                if (goal.done || goal.getPercentage() >= 100) {
                    doneOrUndoneGoal(goal)
                } else {
                    setupItems()
                    (activity as MainActivity).openBottomSheetDoneGoal(goal, ::doneOrUndoneGoal)
                }
            }
            ItemTouchHelper.LEFT -> {
                goal.archived = true
                goal.archiveDate = getCurrentTime()

                viewModel.saveGoal(goal)

                showSnackBarWithAction(
                    holder.itemView,
                    getString(R.string.goals_fragment_resolved_goal_message),
                    goal as Any,
                    ::archiveGoal
                )

                setupItems()
            }
        }
    }

    fun onViewMoved(
        oldPosition: Int, newPosition: Int, items: MutableList<Goal>,
        function: (oldPosition: Int, newPosition: Int) -> Unit
    ) {
        val targetGoal = items[oldPosition]
        val otherGoal = items[newPosition]

        targetGoal.order = newPosition
        otherGoal.order = oldPosition

        viewModel.saveGoal(targetGoal)
        viewModel.saveGoal(otherGoal)

        items.removeAt(oldPosition)
        items.add(newPosition, targetGoal)

        function(oldPosition, newPosition)
    }

    private fun doneOrUndoneGoal(goal: Goal) {
        goal.done = !goal.done
        goal.undoneDate = getCurrentTime()

        viewModel.saveGoal(goal)
    }

    private fun archiveGoal(goal: Any) {
        (goal as Goal).archived = false
        viewModel.saveGoal(goal)
    }
}
