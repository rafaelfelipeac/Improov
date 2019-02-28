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
import com.rafaelfelipeac.domore.ui.activities.MainActivity
import com.rafaelfelipeac.domore.ui.adapter.GoalsAdapter
import com.rafaelfelipeac.domore.ui.adapter.ItemsAdapter
import com.rafaelfelipeac.domore.ui.base.BaseFragment
import com.rafaelfelipeac.domore.ui.helper.SwipeAndDragHelperGoal
import com.rafaelfelipeac.domore.ui.helper.SwipeAndDragHelperItem
import kotlinx.android.synthetic.main.fragment_goal.*
import kotlinx.android.synthetic.main.fragment_goals.*

class GoalsFragment : BaseFragment() {

    private var goalsAdapter = GoalsAdapter(this)

    private var viewModel: GoalsViewModel?= null

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

        hideSoftKeyboard(activity!!)
        showNavigation()

        fabScrollListener()

        fab_goals.setOnClickListener {
            navController.navigate(R.id.action_navigation_goals_to_goalFormFragment)
        }

        setupListAndAdapter()
    }

    fun setupListAndAdapter() {
        goalsAdapter.setItems(viewModel?.getGoals()!!.sortedBy { it.order })

        goalsAdapter.clickListener = {
            val action =
                GoalsFragmentDirections.actionNavigationGoalsToGoalFragment(it)
            navController.navigate(action)
        }

        goals_list.layoutManager = LinearLayoutManager(
            context,
            RecyclerView.VERTICAL,
            false
        )

        val swipeAndDragHelper = SwipeAndDragHelperGoal(goalsAdapter)
        val touchHelper = ItemTouchHelper(swipeAndDragHelper)

        goalsAdapter.touchHelper = touchHelper

        goals_list.adapter = goalsAdapter

        touchHelper.attachToRecyclerView(goals_list)
    }

    fun setItems() {
        val goalsList = goalDAO?.getAll()

        goalsAdapter.setItems(goalsList
            ?.sortedBy { it.order }!!)

        val swipeAndDragHelper = SwipeAndDragHelperItem(goalsAdapter)
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
}
