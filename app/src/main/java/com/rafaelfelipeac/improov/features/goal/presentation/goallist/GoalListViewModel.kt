package com.rafaelfelipeac.improov.features.goal.presentation.goallist

import androidx.lifecycle.viewModelScope
import com.rafaelfelipeac.improov.core.platform.base.BaseAction
import com.rafaelfelipeac.improov.core.platform.base.BaseViewModel
import com.rafaelfelipeac.improov.core.platform.base.BaseViewState
import com.rafaelfelipeac.improov.features.goal.domain.model.Goal
import com.rafaelfelipeac.improov.features.goal.domain.usecase.goal.GetGoalListUseCase
import com.rafaelfelipeac.improov.features.goal.domain.usecase.goal.SaveGoalUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class GoalListViewModel @Inject constructor(
    private val saveGoalUseCase: SaveGoalUseCase,
    private val getGoalListUseCase: GetGoalListUseCase
) : BaseViewModel<GoalListViewModel.ViewState, GoalListViewModel.Action>(
    ViewState()
) {

    override fun onLoadData() {
        getGoals()
    }

    fun onSaveGoal(goal: Goal) {
        saveGoal(goal)
    }

    private fun saveGoal(goal: Goal) {
        viewModelScope.launch {
            saveGoalUseCase.execute(goal).also {
                if (it > 0) {
                    getGoals() // for now
                }
            }
        }
    }

    private fun getGoals() {
        viewModelScope.launch {
            getGoalListUseCase.execute().also {
                if (it.isNotEmpty()) {
                    sendAction(
                        Action.GoalListLoaded(it)
                    )
                }
            }
        }
    }

    override fun onReduceState(viewAction: Action) = when (viewAction) {
        is Action.GoalListLoaded -> state.copy(
            goals = viewAction.goals
        )
    }

    data class ViewState(
        val goals: List<Goal> = listOf()
    ) : BaseViewState

    sealed class Action : BaseAction {
        class GoalListLoaded(val goals: List<Goal>) : Action()
    }
}
