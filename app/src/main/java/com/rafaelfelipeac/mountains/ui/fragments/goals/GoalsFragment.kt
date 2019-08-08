package com.rafaelfelipeac.mountains.ui.fragments.goals

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
import com.rafaelfelipeac.mountains.models.GoalAbstract
import com.rafaelfelipeac.mountains.models.Repetition
import com.rafaelfelipeac.mountains.ui.activities.MainActivity
import com.rafaelfelipeac.mountains.ui.adapter.GoalsAdapter
import com.rafaelfelipeac.mountains.ui.base.BaseFragment
import com.rafaelfelipeac.mountains.ui.helper.SwipeAndDragHelperGoal
import kotlinx.android.synthetic.main.fragment_goals.*

class GoalsFragment : BaseFragment() {

    private var isFromDragAndDrop: Boolean = false

    private lateinit var goalsAdapter: GoalsAdapter

    private lateinit var bottomSheetDoneGoalNo: Button
    private lateinit var bottomSheetDoneGoalYes: Button
    private lateinit var bottomSheetDoneGoal: BottomSheetDialog

    var goals: List<Goal>? = listOf()
    var repetitions: List<Repetition>? = listOf()

    var goalsFinal: MutableList<GoalAbstract>? = mutableListOf()

    private lateinit var viewModel: GoalsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        injector.inject(this)

        (activity as MainActivity).closeToolbar()

        setHasOptionsMenu(true)

        (activity as MainActivity).bottomNavigationVisible(View.VISIBLE)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        (activity as MainActivity).supportActionBar?.title = getString(R.string.fragment_title_goals)

        viewModel = ViewModelProviders.of(this).get(GoalsViewModel::class.java)

        return inflater.inflate(R.layout.fragment_goals, container, false)
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
        bottomSheetDoneGoal = BottomSheetDialog(this.activity!!)
        val sheetView = layoutInflater.inflate(R.layout.bottom_sheet_goal_done, null)
        bottomSheetDoneGoal.setContentView(sheetView)

        bottomSheetDoneGoalYes = sheetView.findViewById(R.id.bottom_sheet_button_yes)
        bottomSheetDoneGoalNo = sheetView.findViewById(R.id.bottom_sheet_button_no)
    }

    private fun openBottomSheetDoneGoal(goal: Goal, function: (goal: Goal) -> Unit) {
        bottomSheetDoneGoal.show()

        bottomSheetDoneGoalYes.setOnClickListener {
            function(goal)
            closeBottomSheetDoneGoal()
        }

        bottomSheetDoneGoalNo.setOnClickListener {
            closeBottomSheetDoneGoal()
        }
    }

    private fun closeBottomSheetDoneGoal() {
        bottomSheetDoneGoal.hide()
    }

    private fun observeViewModel() {
        viewModel.user?.observe(this, Observer { user ->
            (activity as MainActivity).user = user
        })

        viewModel.getGoals()?.observe(this, Observer { goals ->
            this.goals = goals.filter { it.userId == user.userId && !it.archived}

            if (!isFromDragAndDrop) {
                setupItems()
            }

            isFromDragAndDrop = false

            verifyMidnight()
        })

        viewModel.getRepetitions()?.observe(this, Observer { repetitions ->
            this.repetitions = repetitions.filter { it.userId == user.userId && !it.archived}

            if (!isFromDragAndDrop) {
                setupItems()
            }

            isFromDragAndDrop = false

            verifyMidnight()
        })
    }

    private fun verifyMidnight() {
        repetitions?.forEach {
            if (it.nextDate.isToday() && it.doneToday) {
                it.doneToday = false
                viewModel.saveRepetition(it)
            }
        }
    }

    override fun onResume() {
        super.onResume()

        (activity as MainActivity).closeToolbar()
    }

    private fun setupItems() {
        if (goals?.isNotEmpty()!! || repetitions?.isNotEmpty()!!) {
            setItems()

            goals_list.visible()
            goals_placeholder.invisible()
        } else {
            goals_list.invisible()
            goals_placeholder.visible()
        }
    }

    private fun setItems() {
        goalsFinal = mutableListOf()

        goals?.let { goalsFinal?.addAll(it) }
        repetitions?.let { goalsFinal?.addAll(it) }

        goalsAdapter = GoalsAdapter(this)

        goalsFinal
            ?.sortedBy { it.order }
            ?.let { goalsAdapter.setItems(it) }

        goalsAdapter.clickListener = {
            when (it) {
                is Repetition -> {
                    val action = GoalsFragmentDirections.actionNavigationGoalsToNavigationRepetition(it.repetitionId)
                    navController.navigate(action)
                }
                is Goal -> {
                    val action = GoalsFragmentDirections.actionNavigationGoalsToNavigationGoal(it.goalId)
                    navController.navigate(action)
                }
            }
        }

        val swipeAndDragHelper = SwipeAndDragHelperGoal(goalsAdapter)
        val touchHelper = ItemTouchHelper(swipeAndDragHelper)

        goalsAdapter.touchHelper = touchHelper

        goals_list.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        goals_list.adapter = goalsAdapter

        touchHelper.attachToRecyclerView(goals_list)
    }

    fun onViewMoved(oldPosition: Int, newPosition: Int, items: List<GoalAbstract>,
                    function: (oldPosition: Int, newPosition: Int) -> Unit) {
        val targetGoal = items[oldPosition]
        val otherGoal = items[newPosition]

        targetGoal.order = newPosition
        otherGoal.order = oldPosition

        isFromDragAndDrop = true

        when (targetGoal) {
            is Goal -> viewModel.saveGoal(targetGoal)
            is Repetition -> viewModel.saveRepetition(targetGoal)
        }

        when (otherGoal) {
            is Goal -> viewModel.saveGoal(otherGoal)
            is Repetition -> viewModel.saveRepetition(otherGoal)
        }

//        items.removeAt(oldPosition)
//        items.add(newPosition, targetGoal)

        function(oldPosition, newPosition)
    }

    fun onViewSwiped(position: Int, direction: Int, holder: RecyclerView.ViewHolder, items: List<GoalAbstract>) {
        val goal = items[position]

        when (direction) {
            ItemTouchHelper.RIGHT -> {
                when (goal) {
                    is Goal -> {
                        if (goal.done || goal.getPercentage() >= 100) {
                            doneOrUndoneGoal(goal)
                        } else {
                            setupItems()
                            openBottomSheetDoneGoal(goal, ::doneOrUndoneGoal)
                        }
                    }
                    is Repetition -> {
                        setupItems()

                        //openBottomSheetDoneGoal()
                    }
                }
            }
            ItemTouchHelper.LEFT -> {
                when (goal) {
                    is Goal -> {
                        goal.archived = true
                        goal.archiveDate = getCurrentTime()

                        viewModel.saveGoal(goal)

                        showSnackBarWithAction(holder.itemView,
                            getString(R.string.goals_fragment_resolved_goal_message), goal as Any, ::archiveGoal)

                        setupItems()
                    }
                    is Repetition -> {
                        setupItems()

                        //openBottomSheetDoneGoal()
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
