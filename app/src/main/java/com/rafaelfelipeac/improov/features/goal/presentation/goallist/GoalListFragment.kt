package com.rafaelfelipeac.improov.features.goal.presentation.goallist

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rafaelfelipeac.improov.R
import com.rafaelfelipeac.improov.core.extension.isVisible
import com.rafaelfelipeac.improov.core.extension.observeNew
import com.rafaelfelipeac.improov.core.extension.vibrate
import com.rafaelfelipeac.improov.core.extension.getPercentage
import com.rafaelfelipeac.improov.core.platform.base.BaseFragment
import com.rafaelfelipeac.improov.features.goal.domain.model.Goal
import kotlinx.android.synthetic.main.fragment_list.*

const val PERCENTAGE_MAX = 100
const val SECONDS_BOTTOM_SHEET = 1000L

@Suppress("TooManyFunctions")
class GoalListFragment : BaseFragment() {

    private var goalsAdapter = GoalListAdapter(this)

    private var swipeShown = false

    private val viewModel by lazy { viewModelFactory.get<GoalListViewModel>(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        injector.inject(this)

        setHasOptionsMenu(true)
    }

    override fun onResume() {
        super.onResume()

        main.closeToolbar()
        hideBottomSheetTips()
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

        viewModel.loadData()

        setupLayout()
        setupBottomSheetGoal()
        observeViewModel()
    }

    private fun setupLayout() {
        fab.setOnClickListener {
            hideBottomSheetTips()

            navController.navigate(GoalListFragmentDirections.actionNavigationListToNavigationGoalForm())
        }
    }

    private fun observeViewModel() {
        viewModel.goals.observeNew(this) {
            it.let { goalsAdapter.setItems(it) }
            setupGoals(it.isNotEmpty())
        }

        viewModel.firstTimeList.observeNew(this) {
            if (it && !swipeShown) {
                showBottomSheetTipsSwipe()
            }
        }

        viewModel.savedFirstTimeList.observeNew(this) {
            // ???
        }

        viewModel.savedGoal.observeNew(this) {
            setupGoals()
            // or update only the goal with the goalId (it)
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

    fun onViewMoved(
        fromPosition: Int,
        toPosition: Int,
        items: MutableList<Goal>,
        function: (fromPosition: Int, toPosition: Int) -> Unit
    ) {
        val target = items[fromPosition]
        val other = items[toPosition]

        target.order = toPosition
        other.order = fromPosition

        viewModel.saveGoal(target, isFromDragAndDrop = true)
        viewModel.saveGoal(other, isFromDragAndDrop = true)

        items.removeAt(fromPosition)
        items.add(toPosition, target)

        function(fromPosition, toPosition)

        vibrate()
    }

    fun onViewSwiped(
        position: Int,
        direction: Int,
        // holder: RecyclerView.ViewHolder,
        items: List<Goal>
    ) {
        val goal = items[position]

        when (direction) {
            ItemTouchHelper.RIGHT -> {
                if (goal.done || goal.getPercentage() >= PERCENTAGE_MAX) {
                    doneOrUndoneGoal(goal)
                } else {
                    setupGoals()
                    showBottomSheetGoal(goal, ::doneOrUndoneGoal)
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

        viewModel.saveGoal(goal)
    }

    private fun archiveGoal(goal: Any) {
        (goal as Goal).archived = false
        viewModel.saveGoal(goal)
    }

    private fun showBottomSheetTipsSwipe() {
        viewModel.saveFirstTimeList(false)

        swipeShown = true

        Handler().postDelayed(
            {
                setupBottomSheetTipsSwipe()
                setupBottomSheetTip()
                showBottomSheetTips()
            }, SECONDS_BOTTOM_SHEET
        )
    }
}
