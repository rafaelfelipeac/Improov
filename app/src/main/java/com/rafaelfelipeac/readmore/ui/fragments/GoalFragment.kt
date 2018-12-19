package com.rafaelfelipeac.readmore.ui.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rafaelfelipeac.readmore.R
import com.rafaelfelipeac.readmore.models.Book
import com.rafaelfelipeac.readmore.models.Goal
import com.rafaelfelipeac.readmore.ui.activities.MainActivity
import com.rafaelfelipeac.readmore.ui.adapter.BooksAdapter
import com.rafaelfelipeac.readmore.ui.adapter.GoalsAdapter
import com.rafaelfelipeac.readmore.ui.base.BaseFragment
import com.rafaelfelipeac.readmore.ui.helper.SwipeAndDragHelper
import kotlinx.android.synthetic.main.fragment_goal.*
import kotlinx.android.synthetic.main.fragment_goals.*
import javax.inject.Inject

class GoalFragment : BaseFragment() {

    @Inject
    lateinit var adapter: BooksAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        injector.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        (activity as MainActivity).supportActionBar?.title = "Goal"

        return inflater.inflate(R.layout.fragment_goal, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter.setItems(listOf(
            Book("g1", "", ""),
            Book("g2",  "", ""),
            Book("g3",  "", ""),
            Book("g4",  "", ""),
            Book("g5",  "", ""),
            Book("g6",  "", ""),
            Book("g7",  "", ""),
            Book("g8",  "", ""),
            Book("g9",  "", ""),
            Book("g10",  "", "")
        ))

//        adapter.clickListener = {
//            navController.navigate(R.id.action_navigation_metas_to_goalFragment)
//        }

        goal_books_list.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        val swipeAndDragHelper = SwipeAndDragHelper(adapter)
        val touchHelper = ItemTouchHelper(swipeAndDragHelper)

        adapter.setTouchHelper(touchHelper)

        goal_books_list.adapter = adapter

        touchHelper.attachToRecyclerView(goal_books_list)
    }
}
