package com.rafaelfelipeac.improov.features.goal.presentation.goallist

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.rafaelfelipeac.improov.R
import com.rafaelfelipeac.improov.core.extension.getPercentage
import com.rafaelfelipeac.improov.core.extension.isVisible
import com.rafaelfelipeac.improov.core.extension.observe
import com.rafaelfelipeac.improov.core.extension.vibrate
import com.rafaelfelipeac.improov.core.platform.base.BaseFragment
import com.rafaelfelipeac.improov.features.goal.domain.model.Goal
import kotlinx.android.synthetic.main.fragment_list.*

class GoalListFragment : BaseFragment() {

    private var goalsAdapter = GoalListAdapter(this)

    private var isFromDragAndDrop = 0

    private lateinit var bottomSheetGoalDone: BottomSheetDialog
    private lateinit var bottomSheetGoalDoneYes: Button
    private lateinit var bottomSheetGoalDoneNo: Button

    private var bottomSheetTip: BottomSheetBehavior<*>? = null
    private var bottomSheetTipClose: ConstraintLayout? = null

    private val viewModel by lazy { viewModelFactory.get<GoalListViewModel>(this) }

    private val stateObserver = Observer<GoalListViewModel.ViewState> { response ->
        if (isFromDragAndDrop == 0) {
            response.goals
                .let { goalsAdapter.setItems(it) }
            setupGoals(response.goals.isNotEmpty())
        }

        if (isFromDragAndDrop > 0) {
            isFromDragAndDrop--
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        injector.inject(this)

        setHasOptionsMenu(true)
    }

    override fun onResume() {
        super.onResume()

        main.closeToolbar()
        main.closeBottomSheetTips()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        main.supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        main.supportActionBar?.title = getString(R.string.list_title)

        showNavigation()

        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observe(viewModel.stateLiveData, stateObserver)

        viewModel.loadData()

        fab.setOnClickListener {
            main.closeBottomSheetTips()

            navController.navigate(GoalListFragmentDirections.actionNavigationListToNavigationGoalForm())
        }

        setupBottomSheetGoalDone()

        if (preferences.fistTimeList) {
            preferences.fistTimeList = false

            Handler().postDelayed(
                {
                    main.setupBottomSheetTipsThree()
                    setupBottomSheetTip()
                    main.openBottomSheetTips()
                }, 1000
            )
        }
    }

    private fun setupGoals(visible: Boolean = true) {
        setGoals()

        list_list.isVisible(visible)
        list_placeholder.isVisible(!visible)
    }

    private fun setGoals() {
        val swipeAndDragHelper =
            SwipeAndDragHelperList(
                goalsAdapter
            )
        val touchHelper = ItemTouchHelper(swipeAndDragHelper)

        touchHelper.attachToRecyclerView(list_list)

        goalsAdapter.touchHelper = touchHelper
        goalsAdapter.clickListener = {
            val action = GoalListFragmentDirections.actionNavigationListToNavigationGoal(it.goalId)
            navController.navigate(action)
        }

        list_list.apply {
            setHasFixedSize(true)

            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = goalsAdapter
        }
    }

    private fun setupBottomSheetTip() {
        bottomSheetTip = main.bottomSheetTip
        bottomSheetTipClose = main.bottomSheetTipClose

        bottomSheetTipClose?.setOnClickListener {
            hideSoftKeyboard()
            main.closeBottomSheetTips()
        }
    }

    private fun setupBottomSheetGoalDone() {
        bottomSheetGoalDone = BottomSheetDialog(this.activity!!)
        val sheetView = layoutInflater.inflate(R.layout.bottom_sheet_goal_done, null)
        bottomSheetGoalDone.setContentView(sheetView)

        bottomSheetGoalDoneYes = sheetView.findViewById(R.id.goal_done_yes)
        bottomSheetGoalDoneNo = sheetView.findViewById(R.id.goal_done_no)
    }

    private fun openBottomSheetGoalDone(goal: Goal, function: (goal: Goal) -> Unit) {
        bottomSheetGoalDone.show()

        bottomSheetGoalDoneYes.setOnClickListener {
            function(goal)
            closeBottomSheetGoalDone()
        }

        bottomSheetGoalDoneNo.setOnClickListener {
            setupGoals()
            closeBottomSheetGoalDone()
        }
    }

    private fun closeBottomSheetGoalDone() {
        bottomSheetGoalDone.hide()
    }

    fun onViewMoved(
        fromPosition: Int, toPosition: Int, items: MutableList<Goal>,
        function: (fromPosition: Int, toPosition: Int) -> Unit
    ) {
        val target = items[fromPosition]
        val other = items[toPosition]

        target.order = toPosition
        other.order = fromPosition

        viewModel.onSaveGoal(target)
        isFromDragAndDrop++
        viewModel.onSaveGoal(other)
        isFromDragAndDrop++

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
//                goal.archived = true
//                        goal.archiveDate = getCurrentTime()
//
//                        listViewModel.saveGoal(goal)
//
//                        showSnackBarWithAction(holder.itemView,
//                            getString(R.string.list_item_resolved_message), goal as Any, ::archiveGoal)
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
