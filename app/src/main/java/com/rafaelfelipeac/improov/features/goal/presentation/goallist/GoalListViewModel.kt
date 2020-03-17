package com.rafaelfelipeac.improov.features.goal.presentation.goallist

import androidx.lifecycle.viewModelScope
import com.rafaelfelipeac.improov.core.platform.base.BaseAction
import com.rafaelfelipeac.improov.core.platform.base.BaseViewModel
import com.rafaelfelipeac.improov.core.platform.base.BaseViewState
import com.rafaelfelipeac.improov.features.goal.domain.model.Goal
import com.rafaelfelipeac.improov.features.goal.domain.usecase.GetListUseCase
import com.rafaelfelipeac.improov.features.goal.domain.usecase.SaveListUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class GoalListViewModel @Inject constructor(
    private val getListUseCase: GetListUseCase,
    private val saveListUseCase: SaveListUseCase
) : BaseViewModel<GoalListViewModel.ViewState, GoalListViewModel.Action>(
    ViewState()
) {

    override fun onLoadData() {
        getList()
    }

    fun onSaveGoal(goal: Goal) {
        saveGoal(goal)

        getList()
    }

    private fun getList() {
        viewModelScope.launch {
            getListUseCase.execute().also {
                if (it.isNotEmpty()) {
                    sendAction(
                        Action.ListLoadingSuccess(
                            it
                        )
                    )
                } else {
                    sendAction(Action.ListLoadingFailure)
                }
            }
        }
    }

    private fun saveGoal(goal: Goal) {
        viewModelScope.launch {
            saveListUseCase.execute(goal).also {
                if (it > 0) {

                } else {

                }
            }
        }
    }

    data class ViewState(
        val goals: List<Goal> = listOf(),
        val listIsVisible: Boolean = false
    ) : BaseViewState

    sealed class Action :
        BaseAction {
        class ListLoadingSuccess(val goals: List<Goal>) : Action()
        object ListLoadingFailure : Action()
    }

    override fun onReduceState(viewAction: Action) = when (viewAction) {
        is Action.ListLoadingSuccess -> state.copy(
            goals = viewAction.goals,
            listIsVisible = true
        )
        is Action.ListLoadingFailure -> state.copy(
            goals = listOf(),
            listIsVisible = false
        )
    }
}
