package com.rafaelfelipeac.improov.features.goal.presentation.goallist

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
import com.rafaelfelipeac.improov.core.extension.*
import com.rafaelfelipeac.improov.core.platform.base.BaseFragment
import com.rafaelfelipeac.improov.features.goal.domain.model.Goal
import com.rafaelfelipeac.improov.features.goal.presentation.SwipeAndDragHelperList
import com.rafaelfelipeac.improov.features.main.MainActivity
import kotlinx.android.synthetic.main.fragment_list.*

class GoalListFragment : BaseFragment() {

    private var isFromDragAndDrop: Boolean = false

    private lateinit var listAdapter: GoalListAdapter

    private lateinit var bottomSheetGoalDone: BottomSheetDialog
    private lateinit var bottomSheetGoalDoneYes: Button
    private lateinit var bottomSheetGoalDoneNo: Button

    private lateinit var bottomSheetHabitDone: BottomSheetDialog
    private lateinit var bottomSheetHabitDoneOK: Button

    private var bottomSheetTip: BottomSheetBehavior<*>? = null
    private var bottomSheetTipClose: ConstraintLayout? = null

    var list: MutableList<Goal>? = mutableListOf()

    private val viewModel by lazy { viewModelFactory.get<GoalListViewModel>(this) }

    private val stateObserver = Observer<GoalListViewModel.ViewState> {
        listAdapter.setItems(it.goals)

        list_list.isVisible(it.listIsVisible)
        list_placeholder.isVisible(!it.listIsVisible)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        injector.inject(this)

        setHasOptionsMenu(true)
    }

    override fun onResume() {
        super.onResume()

        (activity as MainActivity).closeToolbar()
        (activity as MainActivity).closeBottomSheetTips()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        (activity as MainActivity).supportActionBar?.title = getString(R.string.list_title)

        showNavigation()

        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        if (!isFromDragAndDrop) {
//                setupList()
//            }
//
//            isFromDragAndDrop = false

        listAdapter = GoalListAdapter(this)

        val swipeAndDragHelper = SwipeAndDragHelperList(listAdapter)
        val touchHelper = ItemTouchHelper(swipeAndDragHelper)

        touchHelper.attachToRecyclerView(list_list)

        listAdapter.touchHelper = touchHelper
        listAdapter.clickListener = {
            val action = GoalListFragmentDirections.actionNavigationListToNavigationGoal(it.goalId)
            navController.navigate(action)
        }

        list_list.apply {
            setHasFixedSize(true)

            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = listAdapter
        }

        observe(viewModel.stateLiveData, stateObserver)
        viewModel.loadData()

        fab.setOnClickListener {
            (activity as MainActivity).closeBottomSheetTips()

            navController.navigate(GoalListFragmentDirections.actionNavigationListToNavigationGoalForm())
        }

        setupBottomSheetGoalDone()
        setupBottomSheetHabitDone()

        if (preferences.fistTimeList) {
            preferences.fistTimeList = false

            Handler().postDelayed(
                {
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

    fun onViewMoved(
        fromPosition: Int, toPosition: Int, items: MutableList<Goal>,
        function: (fromPosition: Int, toPosition: Int) -> Unit
    ) {
        val target = items[fromPosition]
        val other = items[toPosition]

        target.order = toPosition
        other.order = fromPosition

        isFromDragAndDrop = true

        items.removeAt(fromPosition)
        items.add(toPosition, target)

        function(fromPosition, toPosition)

        vibrate()
    }

    fun onViewSwiped(
        position: Int,
        direction: Int,
        holder: RecyclerView.ViewHolder,
        items: List<Goal>
    ) {
        val goal = items[position]

        when (direction) {
            ItemTouchHelper.RIGHT -> {
                if (goal.done || goal.getPercentage() >= 100) {
                    doneOrUndoneGoal(goal)
                } else {
                    //setupList()
                    openBottomSheetGoalDone(goal, ::doneOrUndoneGoal)
                }
            }
            ItemTouchHelper.LEFT -> {
//                goalHabit.archived = true
//                        goalHabit.archiveDate = getCurrentTime()
//
//                        listViewModel.saveGoal(goalHabit)
//
//                        showSnackBarWithAction(holder.itemView,
//                            getString(R.string.list_item_resolved_message), goalHabit as Any, ::archiveGoal)
            }
        }
    }

    private fun doneOrUndoneGoal(goal: Goal) {
        goal.done = !goal.done
        goal.undoneDate = getCurrentTime()

        viewModel.onSaveGoal(goal)
    }

    private fun archiveGoal(goal: Any) {
        (goal as Goal).archived = false
        viewModel.onSaveGoal(goal)
    }
}
