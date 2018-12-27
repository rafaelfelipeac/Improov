package com.rafaelfelipeac.readmore.ui.fragments.goal

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rafaelfelipeac.readmore.R
import com.rafaelfelipeac.readmore.models.Goal
import com.rafaelfelipeac.readmore.models.Item
import com.rafaelfelipeac.readmore.ui.activities.MainActivity
import com.rafaelfelipeac.readmore.ui.adapter.ItemsAdapter
import com.rafaelfelipeac.readmore.ui.base.BaseFragment
import com.rafaelfelipeac.readmore.ui.helper.SwipeAndDragHelper
import kotlinx.android.synthetic.main.fragment_goal.*
import javax.inject.Inject

class GoalFragment : BaseFragment() {

    @Inject
    lateinit var adapter: ItemsAdapter

    var goal: Goal? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        injector.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        (activity as MainActivity).supportActionBar?.title = "Meta"

        return inflater.inflate(R.layout.fragment_goal, container, false)
    }

    override fun onStart() {
        super.onStart()

        goal = GoalFragmentArgs.fromBundle(arguments!!).goalArgument

        when(goal?.type) {
            1 -> {goal_items_list.visibility = View.VISIBLE}
            2 -> {goal_btn_dec_inc.visibility = View.VISIBLE}
            3 -> {goal_btn_total.visibility = View.VISIBLE}
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hideSoftKeyboard(activity!!)

        adapter.setItems(listOf(
            Item(goalId =  1, title =  "g1", desc =  "", author = ""),
            Item(goalId =  1, title =  "g2", desc =  "", author = ""),
            Item(goalId =  1, title =  "g3", desc =  "", author = ""),
            Item(goalId =  1, title =  "g4", desc =  "", author = ""),
            Item(goalId =  1, title =  "g5", desc =  "", author = ""),
            Item(goalId =  1, title =  "g6", desc =  "", author = ""),
            Item(goalId =  1, title =  "g7", desc =  "", author = ""),
            Item(goalId =  1, title =  "g8", desc =  "", author = ""),
            Item(goalId =  1, title =  "g9", desc =  "", author = ""),
            Item(goalId =  1, title =  "g10", desc =  "", author = "")
        ))

        adapter.clickListener = {
            navController.navigate(R.id.action_goalFragment_to_bookFragment)
        }

        goal_items_list.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        val swipeAndDragHelper = SwipeAndDragHelper(adapter)
        val touchHelper = ItemTouchHelper(swipeAndDragHelper)

        adapter.setTouchHelper(touchHelper)

        goal_items_list.adapter = adapter

        touchHelper.attachToRecyclerView(goal_items_list)
    }
}
