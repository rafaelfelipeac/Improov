package com.rafaelfelipeac.domore.ui.fragments.goals

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ItemTouchHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rafaelfelipeac.domore.R
import com.rafaelfelipeac.domore.app.App
import com.rafaelfelipeac.domore.extension.getPercentage
import com.rafaelfelipeac.domore.models.Goal
import com.rafaelfelipeac.domore.ui.activities.MainActivity
import com.rafaelfelipeac.domore.ui.adapter.GoalsAdapter
import com.rafaelfelipeac.domore.ui.base.BaseFragment
import com.rafaelfelipeac.domore.ui.helper.SwipeAndDragHelperGoal
import kotlinx.android.synthetic.main.fragment_goals.*

class GoalsFragment : BaseFragment() {

    private var goalsAdapter = GoalsAdapter(this)

    private lateinit var viewModel: GoalsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        injector.inject(this)

        setHasOptionsMenu(true)

        (activity as MainActivity).bottomNavigationVisible(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        (activity as MainActivity).supportActionBar?.title = "Metas"

        viewModel = ViewModelProviders.of(this).get(GoalsViewModel::class.java)

        return inflater.inflate(R.layout.fragment_goals, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity).closeBottomSheetDoneGoal()
        (activity as MainActivity).closeBottomSheetAddItem()

        showNavigation()

        fabScrollListener()

        fab_goals.setOnClickListener {
            navController.navigate(R.id.action_nav_goals_to_goalFormFragment)
        }

        setupItems()
    }

    override fun onResume() {
        super.onResume()

        (activity as MainActivity).closeBottomSheetAddItem()
    }

    private fun setupItems() {
        if (viewModel.getGoals().isNotEmpty()) {
            setItems()

            goalListVisible(true)
        } else {
            goalListVisible(false)
        }
    }

    private fun goalListVisible(visible: Boolean) {
        goals_list.visibility = if (visible) View.VISIBLE else View.INVISIBLE
        goals_placeholder.visibility = if (visible) View.INVISIBLE else View.VISIBLE
    }

    private fun setItems() {
        goalsAdapter.setItems(viewModel.getGoals().sortedBy { it.order })

        goalsAdapter.clickListener = {
            val action = GoalsFragmentDirections.actionNavGoalsToGoalFragment(it)
            navController.navigate(action)
        }

        //goals_list.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)

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

                goalDAO?.delete(goal)

                showSnackBarWithAction(holder.itemView, "Meta removida.", goal as Any, ::deleteGoal)

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

        App.database?.goalDAO()?.update(targetGoal)
        App.database?.goalDAO()?.update(otherGoal)

        items.removeAt(oldPosition)
        items.add(newPosition, targetGoal)

        function(oldPosition, newPosition)
    }

    private fun doneOrUndoneGoal(goal: Goal) {
        goal.done = !goal.done
        goal.undoneDate = getCurrentTime()

        goalDAO?.update(goal)

        setupItems()
    }

    private fun deleteGoal(goal: Any) {
        goalDAO?.insert(goal as Goal)

        setupItems()
    }
}
