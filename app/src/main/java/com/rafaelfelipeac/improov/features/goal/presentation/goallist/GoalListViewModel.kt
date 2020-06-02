package com.rafaelfelipeac.improov.features.goal.presentation.goallist

import androidx.lifecycle.viewModelScope
import com.rafaelfelipeac.improov.core.platform.base.BaseAction
import com.rafaelfelipeac.improov.core.platform.base.BaseViewModel
import com.rafaelfelipeac.improov.core.platform.base.BaseViewState
import com.rafaelfelipeac.improov.features.goal.domain.model.Goal
import com.rafaelfelipeac.improov.features.goal.domain.usecase.firsttimelist.SaveFirstTimeListUseCase
import com.rafaelfelipeac.improov.features.goal.domain.usecase.firsttimelist.GetFirstTimeListUseCase
import com.rafaelfelipeac.improov.features.goal.domain.usecase.goal.GetGoalListUseCase
import com.rafaelfelipeac.improov.features.goal.domain.usecase.goal.SaveGoalUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class GoalListViewModel @Inject constructor(
    private val saveGoalUseCase: SaveGoalUseCase,
    private val getGoalListUseCase: GetGoalListUseCase,
    private val saveFirstTimeListUseCase: SaveFirstTimeListUseCase,
    private val getFirstTimeListUseCase: GetFirstTimeListUseCase
) : BaseViewModel<GoalListViewModel.ViewState, GoalListViewModel.Action>(
    ViewState()
) {

    override fun onLoadData() {
        getGoals()

        getFirstTimeList()
    }

    fun onSaveGoal(goal: Goal) {
        saveGoal(goal)
    }

    fun onSaveFirstTimeList(firstTimeList: Boolean) {
        saveFirstTimeList(firstTimeList)
    }

    private fun saveGoal(goal: Goal) {
        viewModelScope.launch {
            saveGoalUseCase.execute(goal).also {
                if (it > 0) {
                    sendAction(
                        Action.GoalSaved
                    )

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

    private fun saveFirstTimeList(firstTimeList: Boolean) {
        viewModelScope.launch {
            saveFirstTimeListUseCase.execute(firstTimeList).also {
                sendAction(Action.FirstTimeListSaved)
            }
        }
    }

    private fun getFirstTimeList() {
        viewModelScope.launch {
            getFirstTimeListUseCase.execute().also {
                sendAction(Action.FirstTimeListLoaded(it))
            }
        }
    }

    override fun onReduceState(viewAction: Action) = when (viewAction) {
        is Action.GoalSaved -> state.copy(
            goalSaved = true
        )
        is Action.GoalListLoaded -> state.copy(
            goals = viewAction.goals
        )
        is Action.FirstTimeListSaved -> state.copy(
            firstTimeListSaved = true
        )
        is Action.FirstTimeListLoaded -> state.copy(
            firstTimeList = viewAction.firstTimeList
        )
    }

    data class ViewState(
        val goalSaved: Boolean = false,
        val goals: List<Goal> = listOf(),
        val firstTimeListSaved: Boolean = false,
        val firstTimeList: Boolean = false
    ) : BaseViewState

    sealed class Action : BaseAction {
        object GoalSaved : Action()
        class GoalListLoaded(val goals: List<Goal>) : Action()
        object FirstTimeListSaved: Action()
        class FirstTimeListLoaded(val firstTimeList: Boolean) : Action()
    }
}
