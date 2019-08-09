package com.rafaelfelipeac.mountains.ui.fragments.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.rafaelfelipeac.mountains.R
import com.rafaelfelipeac.mountains.extension.getPercentage
import com.rafaelfelipeac.mountains.extension.invisible
import com.rafaelfelipeac.mountains.extension.isToday
import com.rafaelfelipeac.mountains.extension.visible
import com.rafaelfelipeac.mountains.models.Goal
import com.rafaelfelipeac.mountains.models.GoalHabit
import com.rafaelfelipeac.mountains.models.Habit
import com.rafaelfelipeac.mountains.ui.activities.MainActivity
import com.rafaelfelipeac.mountains.ui.adapter.ListAdapter
import com.rafaelfelipeac.mountains.ui.base.BaseFragment
import com.rafaelfelipeac.mountains.ui.helper.SwipeAndDragHelperList
import kotlinx.android.synthetic.main.fragment_list.*

class ListFragment : BaseFragment() {

    private var isFromDragAndDrop: Boolean = false

    private lateinit var listAdapter: ListAdapter

    private lateinit var bottomSheetGoalDoneNo: Button
    private lateinit var bottomSheetGoalDoneYes: Button
    private lateinit var bottomSheetGoalDone: BottomSheetDialog

    var goals: List<Goal>? = listOf()
    var habits: List<Habit>? = listOf()

    var list: MutableList<GoalHabit>? = mutableListOf()

    private lateinit var viewModel: ListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        injector.inject(this)

        (activity as MainActivity).closeToolbar()

        setHasOptionsMenu(true)

        (activity as MainActivity).bottomNavigationVisible(View.VISIBLE)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        (activity as MainActivity).supportActionBar?.title = "Lista"

        viewModel = ViewModelProviders.of(this).get(ListViewModel::class.java)

        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()

        fab.setOnClickListener {
            (activity as MainActivity).openBottomSheetFAB()
        }

        showNavigation()

        setupBottomSheetDoneGoal()
    }

    private fun setupBottomSheetDoneGoal() {
        bottomSheetGoalDone = BottomSheetDialog(this.activity!!)
        val sheetView = layoutInflater.inflate(R.layout.bottom_sheet_goal_done, null)
        bottomSheetGoalDone.setContentView(sheetView)

        bottomSheetGoalDoneYes = sheetView.findViewById(R.id.bottom_sheet_done_yes)
        bottomSheetGoalDoneNo = sheetView.findViewById(R.id.bottom_sheet_done_no)
    }

    private fun openBottomSheetDone(goal: Goal, function: (goal: Goal) -> Unit) {
        bottomSheetGoalDone.show()

        bottomSheetGoalDoneYes.setOnClickListener {
            function(goal)
            closeBottomSheetGoalDone()
        }

        bottomSheetGoalDoneNo.setOnClickListener {
            closeBottomSheetGoalDone()
        }
    }

    private fun closeBottomSheetGoalDone() {
        bottomSheetGoalDone.hide()
    }

    private fun observeViewModel() {
        viewModel.user?.observe(this, Observer { user ->
            (activity as MainActivity).user = user
        })

        viewModel.getGoals()?.observe(this, Observer { goals ->
            this.goals = goals.filter { it.userId == user.userId && !it.archived}

            if (!isFromDragAndDrop) {
                setupList()
            }

            isFromDragAndDrop = false

            verifyMidnight()
        })

        viewModel.getHabits()?.observe(this, Observer { habits ->
            this.habits = habits.filter { it.userId == user.userId && !it.archived}

            if (!isFromDragAndDrop) {
                setupList()
            }

            isFromDragAndDrop = false

            verifyMidnight()
        })
    }

    private fun verifyMidnight() {
        habits?.forEach {
            if (it.nextDate.isToday() && it.doneToday) {
                it.doneToday = false
                viewModel.saveHabit(it)
            }
        }
    }

    override fun onResume() {
        super.onResume()

        (activity as MainActivity).closeToolbar()
    }

    private fun setupList() {
        if (goals?.isNotEmpty()!! || habits?.isNotEmpty()!!) {
            setList()

            list_list.visible()
            list_placeholder.invisible()
        } else {
            list_list.invisible()
            list_placeholder.visible()
        }
    }

    private fun setList() {
        list = mutableListOf()

        goals?.let { list?.addAll(it) }
        habits?.let { list?.addAll(it) }

        listAdapter = ListAdapter(this)

        list
            ?.sortedBy { it.order }
            ?.let { listAdapter.setItems(it) }

        listAdapter.clickListener = {
            when (it) {
                is Habit -> {
                    val action = ListFragmentDirections.actionNavigationListToNavigationHabit(it.habitId)
                    navController.navigate(action)
                }
                is Goal -> {
                    val action = ListFragmentDirections.actionNavigationListToNavigationGoal(it.goalId)
                    navController.navigate(action)
                }
            }
        }

        val swipeAndDragHelper = SwipeAndDragHelperList(listAdapter)
        val touchHelper = ItemTouchHelper(swipeAndDragHelper)

        listAdapter.touchHelper = touchHelper

        list_list.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        list_list.adapter = listAdapter

        touchHelper.attachToRecyclerView(list_list)
    }

    fun onViewMoved(oldPosition: Int, newPosition: Int, items: List<GoalHabit>,
                    function: (oldPosition: Int, newPosition: Int) -> Unit) {
        val target = items[oldPosition]
        val other = items[newPosition]

        target.order = newPosition
        other.order = oldPosition

        isFromDragAndDrop = true

        when (target) {
            is Goal -> viewModel.saveGoal(target)
            is Habit -> viewModel.saveHabit(target)
        }

        when (other) {
            is Goal -> viewModel.saveGoal(other)
            is Habit -> viewModel.saveHabit(other)
        }

//        items.removeAt(oldPosition)
//        items.add(newPosition, targetGoal)

        function(oldPosition, newPosition)
    }

    fun onViewSwiped(position: Int, direction: Int, holder: RecyclerView.ViewHolder, items: List<GoalHabit>) {
        val goalHabit = items[position]

        when (direction) {
            ItemTouchHelper.RIGHT -> {
                when (goalHabit) {
                    is Goal -> {
                        if (goalHabit.done || goalHabit.getPercentage() >= 100) {
                            doneOrUndoneGoal(goalHabit)
                        } else {
                            setupList()
                            openBottomSheetDone(goalHabit, ::doneOrUndoneGoal)
                        }
                    }
                    is Habit -> {
                        setupList()

                        //openBottomSheetDone()
                    }
                }
            }
            ItemTouchHelper.LEFT -> {
                when (goalHabit) {
                    is Goal -> {
                        goalHabit.archived = true
                        goalHabit.archiveDate = getCurrentTime()

                        viewModel.saveGoal(goalHabit)

                        showSnackBarWithAction(holder.itemView,
                            getString(R.string.goals_fragment_resolved_goal_message), goalHabit as Any, ::archiveGoal)

                        setupList()
                    }
                    is Habit -> {
                        setupList()

                        //openBottomSheetDone()
                    }
                }
            }
        }
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
