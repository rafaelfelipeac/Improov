package com.rafaelfelipeac.improov.features.list

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.rafaelfelipeac.improov.R
import com.rafaelfelipeac.improov.core.extension.getPercentage
import com.rafaelfelipeac.improov.core.extension.invisible
import com.rafaelfelipeac.improov.core.extension.isToday
import com.rafaelfelipeac.improov.core.extension.visible
import com.rafaelfelipeac.improov.core.platform.base.BaseFragment
import com.rafaelfelipeac.improov.features.commons.Goal
import com.rafaelfelipeac.improov.features.commons.GoalHabit
import com.rafaelfelipeac.improov.features.commons.Habit
import com.rafaelfelipeac.improov.features.commons.presentation.ListAdapter
import com.rafaelfelipeac.improov.features.commons.presentation.SwipeAndDragHelperList
import com.rafaelfelipeac.improov.features.main.MainActivity
import kotlinx.android.synthetic.main.fragment_list.*

class ListFragment : BaseFragment() {

    private var isFromDragAndDrop: Boolean = false

    private lateinit var listAdapter: ListAdapter

    private lateinit var bottomSheetGoalDone: BottomSheetDialog
    private lateinit var bottomSheetGoalDoneYes: Button
    private lateinit var bottomSheetGoalDoneNo: Button

    private lateinit var bottomSheetHabitDone: BottomSheetDialog
    private lateinit var bottomSheetHabitDoneOK: Button

    private var bottomSheetTip: BottomSheetBehavior<*>? = null
    private var bottomSheetTipClose: ConstraintLayout? = null

    var goals: List<Goal>? = listOf()
    var habits: List<Habit>? = listOf()

    var list: MutableList<GoalHabit>? = mutableListOf()

    private val listViewModel by lazy { viewModelFactory.get<ListViewModel>(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        injector.inject(this)

        setHasOptionsMenu(true)
    }

    override fun onResume() {
        super.onResume()

        (activity as MainActivity).closeToolbar()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        (activity as MainActivity).supportActionBar?.title = getString(R.string.list_title)

        showNavigation()

        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()

        fab.setOnClickListener {
            navController.navigate(ListFragmentDirections.actionNavigationListToNavigationGoalForm())
        }

        setupBottomSheetGoalDone()
        setupBottomSheetHabitDone()

        if (preferences.fistTimeList) {
            preferences.fistTimeList = false

            Handler().postDelayed({
                    (activity as MainActivity).setupBottomSheetTipsThree()
                    setupBottomSheetTip()
                    (activity as MainActivity).openBottomSheetTips()
                }, 1000
            )
        }
    }

    private fun setupBottomSheetTip() {
        bottomSheetTip = (activity as MainActivity).bottomSheetTip
        bottomSheetTipClose = (activity as MainActivity).bottomSheetTipClose

        bottomSheetTipClose?.setOnClickListener {
            hideSoftKeyboard()
            (activity as MainActivity).closeBottomSheetTips()
        }
    }

    private fun observeViewModel() {
        listViewModel.getGoals()?.observe(this, Observer { goals ->
            this.goals = goals.filter { !it.archived}

            if (!isFromDragAndDrop) {
                setupList()
            }

            isFromDragAndDrop = false
        })

        listViewModel.getHabits()?.observe(this, Observer { habits ->
            this.habits = habits.filter { !it.archived}

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
                listViewModel.saveHabit(it)
            }
        }
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

    private fun setupBottomSheetGoalDone() {
        bottomSheetGoalDone = BottomSheetDialog(this.activity!!)
        val sheetView = layoutInflater.inflate(R.layout.bottom_sheet_goal_done, null)
        bottomSheetGoalDone.setContentView(sheetView)

        bottomSheetGoalDoneYes = sheetView.findViewById(R.id.goal_done_yes)
        bottomSheetGoalDoneNo = sheetView.findViewById(R.id.goal_done_no)
    }

    private fun setupBottomSheetHabitDone() {
        bottomSheetHabitDone = BottomSheetDialog(this.activity!!)
        val sheetView = layoutInflater.inflate(R.layout.bottom_sheet_habit_done, null)
        bottomSheetHabitDone.setContentView(sheetView)

        bottomSheetHabitDoneOK = sheetView.findViewById(R.id.habit_done_ok)
    }

    private fun openBottomSheetGoalDone(goal: Goal, function: (goal: Goal) -> Unit) {
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

    private fun openBottomSheetHabitDone() {
        bottomSheetHabitDone.show()

        bottomSheetHabitDoneOK.setOnClickListener {
            closeBottomSheetHabitDone()
        }
    }

    private fun closeBottomSheetHabitDone() {
        bottomSheetHabitDone.hide()
    }

    fun onViewMoved(oldPosition: Int, newPosition: Int, items: List<GoalHabit>,
                    function: (oldPosition: Int, newPosition: Int) -> Unit) {
        val target = items[oldPosition]
        val other = items[newPosition]

        target.order = newPosition
        other.order = oldPosition

        isFromDragAndDrop = true

        when (target) {
            is Goal -> listViewModel.saveGoal(target)
            is Habit -> listViewModel.saveHabit(target)
        }

        when (other) {
            is Goal -> listViewModel.saveGoal(other)
            is Habit -> listViewModel.saveHabit(other)
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
                            openBottomSheetGoalDone(goalHabit, ::doneOrUndoneGoal)
                        }
                    }
                    is Habit -> {
                        setupList()

                        openBottomSheetHabitDone()
                    }
                }
            }
            ItemTouchHelper.LEFT -> {
                setupList()
//                when (goalHabit) {
//                    is Goal -> {
//                        goalHabit.archived = true
//                        goalHabit.archiveDate = getCurrentTime()
//
//                        listViewModel.saveGoal(goalHabit)
//
//                        showSnackBarWithAction(holder.itemView,
//                            getString(R.string.list_item_resolved_message), goalHabit as Any, ::archiveGoal)
//
//
//                    }
//                    is Habit -> {
//                        setupList()
//
//                        openBottomSheetHabitDone()
//                    }
//                }
            }
        }
    }

    private fun doneOrUndoneGoal(goal: Goal) {
        goal.done = !goal.done
        goal.undoneDate = getCurrentTime()

        listViewModel.saveGoal(goal)
    }

    private fun archiveGoal(goal: Any) {
        (goal as Goal).archived = false
        listViewModel.saveGoal(goal)
    }
}
