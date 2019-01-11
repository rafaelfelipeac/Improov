package com.rafaelfelipeac.domore.ui.fragments.goal

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.*
import com.rafaelfelipeac.domore.R
import com.rafaelfelipeac.domore.models.Goal
import com.rafaelfelipeac.domore.ui.activities.MainActivity
import com.rafaelfelipeac.domore.ui.adapter.ItemsAdapter
import com.rafaelfelipeac.domore.ui.base.BaseFragment
import com.rafaelfelipeac.domore.ui.helper.SwipeAndDragHelperItem
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_goal.*
import javax.inject.Inject

class GoalFragment : BaseFragment() {

    @Inject
    lateinit var itemsAdapter: ItemsAdapter

    var goal: Goal? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        injector.inject(this)

        goal = GoalFragmentArgs.fromBundle(arguments!!).goal

        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        (activity as MainActivity).supportActionBar?.title = "Meta"

        return inflater.inflate(R.layout.fragment_goal, container, false)
    }

    override fun onStart() {
        super.onStart()

        hideNavigation()

        setupGoal()
    }

    override fun onResume() {
        super.onResume()

        setupGoal()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hideSoftKeyboard(activity!!)

        goal_btn_inc.setOnClickListener {
            goal_inc_dec_total.text = (goal_inc_dec_total.text.toString().toFloat() + 1F).toString()
            goal?.medalValue = goal_inc_dec_total.text.toString().toFloat()
            goalDAO?.update(goal!!)
        }

        goal_btn_dec.setOnClickListener {
            goal_inc_dec_total.text = (goal_inc_dec_total.text.toString().toFloat() - 1F).toString()
            goal?.medalValue = goal_inc_dec_total.text.toString().toFloat()
            goalDAO?.update(goal!!)
        }

        goal_btn_save.setOnClickListener {
            if (goal_total_total.text.isNotEmpty()) {
                goal?.medalValue = goal_total_total.text.toString().toFloat()
                goalDAO?.update(goal!!)

                Snackbar
                    .make(view, "Valor atualizado.", Snackbar.LENGTH_LONG)
                    .show()
            }
        }

        setItemsAdapter()
    }

    private fun setItemsAdapter() {
        val itemsList = itemDAO?.getAll()
        val orderItemsList =
            itemsList?.filter { !it.done && it.goalId == goal?.goalId && it.goalId != 0L }?.sortedBy { it.order }

        itemsAdapter.setItems(orderItemsList!!)

        itemsAdapter.clickListener = {
            navController.navigate(R.id.action_goalFragment_to_itemFragment)
        }

        goal_items_list.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        val swipeAndDragHelper = SwipeAndDragHelperItem(itemsAdapter)
        val touchHelper = ItemTouchHelper(swipeAndDragHelper)

        itemsAdapter.setTouchHelper(touchHelper)

        goal_items_list.adapter = itemsAdapter

        touchHelper.attachToRecyclerView(goal_items_list)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {

        if (goal?.type == 1) {
            inflater?.inflate(R.menu.menu_add, menu)
        }

        inflater?.inflate(R.menu.menu_edit, menu)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_goal_add -> {

                val action =
                    GoalFragmentDirections.actionGoalFragmentToItemFormFragment(goal!!)
                navController.navigate(action)

                return true
            }
            R.id.menu_goal_edit -> {
                val action =
                    GoalFragmentDirections.actionGoalFragmentToGoalFormFragment(goal!!)
                navController.navigate(action)
            }
        }

        return false
    }

    private fun setupGoal() {

        goal_title.text = goal?.name
        goal_inc_dec_total.text = goal?.medalValue.toString()
        goal_total_total.setText(goal?.medalValue.toString())

        if (goal?.trophies!!) {
            goal_medal.visibility = View.INVISIBLE
            goal_trophies.visibility = View.VISIBLE

            goal_trophy_bronze_text.text = goal?.bronzeValue.toString()
            goal_trophy_silver_text.text = goal?.silverValue.toString()
            goal_trophy_gold_text.text = goal?.goldValue.toString()
        }

        when (goal?.type) {
            1 -> {
                goal_items_list.visibility = View.VISIBLE
                (activity as MainActivity).toolbar.inflateMenu(R.menu.menu_add)
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
