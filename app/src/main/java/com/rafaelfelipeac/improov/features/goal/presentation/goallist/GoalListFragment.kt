package com.rafaelfelipeac.improov.features.goal.presentation.goallist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rafaelfelipeac.improov.core.extension.setVisibility
import com.rafaelfelipeac.improov.core.extension.gone
import com.rafaelfelipeac.improov.core.extension.viewBinding
import com.rafaelfelipeac.improov.core.extension.vibrate
import com.rafaelfelipeac.improov.core.platform.base.BaseFragment
import com.rafaelfelipeac.improov.databinding.FragmentGoalListBinding
import com.rafaelfelipeac.improov.features.commons.domain.model.Goal
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

const val PERCENTAGE_MAX = 100
const val SECONDS_BOTTOM_SHEET = 1000L

@Suppress("TooManyFunctions")
class GoalListFragment : BaseFragment() {

    private var goalsAdapter = GoalListAdapter(this)

    private var swipeShown = false
    private var swipedPosition = 0

    private val viewModel by lazy { viewModelProvider.goalListViewModel() }

    private var binding by viewBinding<FragmentGoalListBinding>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        setScreen()

        return FragmentGoalListBinding.inflate(inflater, container, false).run {
            binding = this
            binding.root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.loadData()

        setupLayout()
        setupBottomSheetGoal()
        observeViewModel()
    }

    private fun setScreen() {
        hideToolbar()
        showNavigation()
        hideBottomSheetTips()
    }

    private fun setupLayout() {
        fab.setOnClickListener {
            hideBottomSheetTips()

            navController.navigate(GoalListFragmentDirections.listToAdd())
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.goals.collect {
                it.let { goalsAdapter.setItems(it) }
                setupGoals(it.isNotEmpty())
            }
        }

        lifecycleScope.launch {
            viewModel.savedFirstTimeList.collect {
                if (!swipeShown) {
                    showBottomSheetTipsSwipe()
                }
            }
        }

        lifecycleScope.launch {
            viewModel.firstTimeList.collect {
                if (it) {
                    viewModel.saveFirstTimeList(false)
                }
            }
        }
    }

    private fun setupGoals(visible: Boolean = true) {
        setGoals()

        binding.goalListLoading.gone()

        binding.goalListList.setVisibility(visible)
        binding.goalListPlaceholder.setVisibility(!visible)
    }

    private fun setGoals() {
        val swipeAndDragHelper =
            SwipeAndDragHelperGoal(
                goalsAdapter
            )
        val touchHelper = ItemTouchHelper(swipeAndDragHelper)

        touchHelper.attachToRecyclerView(binding.goalListList)

        goalsAdapter.touchHelper = touchHelper
        goalsAdapter.clickListener = {
            navController.navigate(GoalListFragmentDirections.listToGoal(it.goalId))
        }

        binding.goalListList.apply {
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
        items: List<Goal>
    ) {
        val goal = items[position]

        swipedPosition = position

        when (direction) {
            ItemTouchHelper.RIGHT -> {
                if (goal.done || goal.getPercentage() >= PERCENTAGE_MAX) {
                    doneOrUndoneGoal(goal)
                } else {
                    showBottomSheetGoal(goal, ::doneOrUndoneGoal, ::reloadGoalAfterSwipe)
                }
            }
            ItemTouchHelper.LEFT -> {
                reloadGoalAfterSwipe()
            }
        }
    }

    private fun doneOrUndoneGoal(goal: Goal) {
        goal.done = !goal.done
        goal.undoneDate = getCurrentTime()

        reloadGoalAfterSwipe()

        viewModel.saveGoal(goal)
    }

    private fun reloadGoalAfterSwipe() {
        goalsAdapter.updateGoal(swipedPosition)
    }

    private fun archiveGoal(goal: Any) {
        (goal as Goal).archived = false
        viewModel.saveGoal(goal)
    }

    private fun showBottomSheetTipsSwipe() {
        swipeShown = true

        lifecycleScope.launch {
            delay(SECONDS_BOTTOM_SHEET)

            setupBottomSheetTipsSwipe()
            setupBottomSheetTip()
            showBottomSheetTips()
        }
    }
}
