package com.rafaelfelipeac.domore.ui.fragments.goal

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rafaelfelipeac.domore.R
import com.rafaelfelipeac.domore.models.Goal
import com.rafaelfelipeac.domore.models.Item
import com.rafaelfelipeac.domore.ui.activities.MainActivity
import com.rafaelfelipeac.domore.ui.adapter.ItemsAdapter
import com.rafaelfelipeac.domore.ui.base.BaseFragment
import com.rafaelfelipeac.domore.ui.helper.SwipeAndDragHelperItem
import kotlinx.android.synthetic.main.fragment_goal.*
import javax.inject.Inject
import android.text.Editable
import android.text.TextWatcher

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

        hideNavigation()

        goal = GoalFragmentArgs.fromBundle(arguments!!).goalArgument

        setupGoal()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hideSoftKeyboard(activity!!)

        goal_btn_inc.setOnClickListener {
            goal_inc_dec_total.text = (goal_inc_dec_total.text.toString().toInt() + 1).toString()
            goal?.actualValue = goal_inc_dec_total.text.toString().toInt()
            goalDAO?.update(goal!!)
        }

        goal_btn_dec.setOnClickListener {
            goal_inc_dec_total.text = (goal_inc_dec_total.text.toString().toInt() - 1).toString()
            goal?.actualValue = goal_inc_dec_total.text.toString().toInt()
            goalDAO?.update(goal!!)
        }

        goal_total_total.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isNotEmpty()) {
                    goal?.actualValue = s.toString().toInt()
                    goalDAO?.update(goal!!)
                }
            }
        })

        adapter.setItems(
            listOf(
                Item(goalId = 1, title = "g1", desc = "", author = ""),
                Item(goalId = 1, title = "g2", desc = "", author = ""),
                Item(goalId = 1, title = "g3", desc = "", author = ""),
                Item(goalId = 1, title = "g4", desc = "", author = ""),
                Item(goalId = 1, title = "g5", desc = "", author = ""),
                Item(goalId = 1, title = "g6", desc = "", author = ""),
                Item(goalId = 1, title = "g7", desc = "", author = ""),
                Item(goalId = 1, title = "g8", desc = "", author = ""),
                Item(goalId = 1, title = "g9", desc = "", author = ""),
                Item(goalId = 1, title = "g10", desc = "", author = "")
            )
        )

        adapter.clickListener = {
            navController.navigate(R.id.action_goalFragment_to_bookFragment)
        }

        goal_items_list.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        val swipeAndDragHelper = SwipeAndDragHelperItem(adapter)
        val touchHelper = ItemTouchHelper(swipeAndDragHelper)

        adapter.setTouchHelper(touchHelper)

        goal_items_list.adapter = adapter

        touchHelper.attachToRecyclerView(goal_items_list)
    }

    private fun setupGoal() {

        goal_title.text = goal?.name
        goal_inc_dec_total.text = goal?.actualValue.toString()
        goal_total_total.setText(goal?.actualValue.toString())

        when (goal?.type) {
            1 -> {
                goal_items_list.visibility = View.VISIBLE
            }
            2 -> {
                goal_cl_dec_inc.visibility = View.VISIBLE
            }
            3 -> {
                goal_cl_total.visibility = View.VISIBLE
            }
        }
    }
}
