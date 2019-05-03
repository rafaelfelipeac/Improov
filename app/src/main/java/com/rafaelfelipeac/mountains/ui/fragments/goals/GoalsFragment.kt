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

    private lateinit var viewModel: GoalsViewModel

    var goals: List<Goal>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        injector.inject(this)

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

        fab_goals.setOnClickListener {
            navController.navigate(R.id.action_nav_goals_to_goalFormFragment)
        }
    }

    private fun observeViewModel() {
        viewModel.getGoals()?.observe(this, Observer { goals ->
            this.goals = goals
            setupItems()
        })
    }

    override fun onStart() {
        super.onStart()

        fabScrollListener()
    }

    override fun onResume() {
        super.onResume()

        fabScrollListener()

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
        goalsAdapter.setItems(goals?.sortedBy { it.order }!!)

        goalsAdapter.clickListener = {
            val action = GoalsFragmentDirections.actionNavGoalsToGoalFragment(it)
            navController.navigate(action)
        }

        val swipeAndDragHelper = SwipeAndDragHelperGoal(goalsAdapter)
        val touchHelper = ItemTouchHelper(swipeAndDragHelper)

        goalsAdapter.touchHelper = touchHelper

        goals_list.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        goals_list.adapter = goalsAdapter

        touchHelper.attachToRecyclerView(goals_list)
    }

    private fun fabScrollListener() {
        goals_list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0 || dy < 0 && fab_goals.isShown) fab_goals.hide()
                super.onScrolled(recyclerView, dx, dy)
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) fab_goals.show()
                super.onScrollStateChanged(recyclerView, newState)
            }
        })
    }

    fun onViewSwiped(position: Int, direction: Int, holder: RecyclerView.ViewHolder, items: MutableList<Goal>) {
        val goal = items[position]

        when(direction) {
            ItemTouchHelper.RIGHT -> {
                if (goal.done || goal.getPercentage() >= 100) {
                    doneOrUndoneGoal(goal)
                } else {
                    setupItems()
                    (activity as MainActivity).openBottomSheetDoneGoal(goal, ::doneOrUndoneGoal)
                }
            }
            ItemTouchHelper.LEFT -> {
                goal.deleteDate = getCurrentTime()

                viewModel.deleteGoal(goal)

                showSnackBarWithAction(holder.itemView, getString(R.string.goals_fragment_resolved_goal_message), goal as Any, ::deleteGoal)

                setupItems()
            }
        }
    }

    fun onViewMoved(oldPosition: Int, newPosition: Int, items: MutableList<Goal>,
                    function: (oldPosition: Int, newPosition: Int) -> Unit) {
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

        setupItems()
    }

    private fun deleteGoal(goal: Any) {
        viewModel.saveGoal(goal as Goal)

        setupItems()
    }
}
