package com.rafaelfelipeac.domore.ui.fragments.goals

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rafaelfelipeac.domore.R
import com.rafaelfelipeac.domore.ui.activities.MainActivity
import com.rafaelfelipeac.domore.ui.adapter.GoalsAdapter
import com.rafaelfelipeac.domore.ui.base.BaseFragment
import com.rafaelfelipeac.domore.ui.helper.SwipeAndDragHelperGoal
import kotlinx.android.synthetic.main.fragment_goals.*
import javax.inject.Inject

class GoalsFragment : BaseFragment() {

    @Inject
    lateinit var adapter: GoalsAdapter

    private var viewModel: GoalsViewModel?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        injector.inject(this)

        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        (activity as MainActivity).supportActionBar?.title = "Metas"

        viewModel = ViewModelProviders.of(this).get(GoalsViewModel::class.java)

        return inflater.inflate(R.layout.fragment_goals, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hideSoftKeyboard(activity!!)
        showNavigation()

        fabScrollListener()

        fab_goals.setOnClickListener {
            navController.navigate(R.id.action_navigation_goals_to_goalFormFragment)
        }

        adapter.setItems(viewModel?.getGoals()!!.sortedBy { it.order })

        adapter.clickListener = {
            val action =
                GoalsFragmentDirections.actionNavigationGoalsToGoalFragment(it)
            navController.navigate(action)
        }

        goals_list.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        val swipeAndDragHelper = SwipeAndDragHelperGoal(adapter)
        val touchHelper = ItemTouchHelper(swipeAndDragHelper)

        adapter.setTouchHelper(touchHelper)

        goals_list.adapter = adapter

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
