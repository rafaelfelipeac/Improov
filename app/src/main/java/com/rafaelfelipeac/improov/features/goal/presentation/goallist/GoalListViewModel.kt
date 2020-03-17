package com.rafaelfelipeac.improov.features.goal.presentation.goallist

import androidx.lifecycle.viewModelScope
import com.rafaelfelipeac.improov.core.platform.base.BaseAction
import com.rafaelfelipeac.improov.core.platform.base.BaseViewModel2
import com.rafaelfelipeac.improov.core.platform.base.BaseViewState
import com.rafaelfelipeac.improov.features.goal.Goal
import com.rafaelfelipeac.improov.features.goal.domain.usecase.GetListUseCase
import com.rafaelfelipeac.improov.features.goal.domain.usecase.SaveListUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class GoalsViewModel @Inject constructor(
    private val getListUseCase: GetListUseCase,
    private val saveListUseCase: SaveListUseCase
) : BaseViewModel2<GoalsViewModel.ViewState, GoalsViewModel.Action>(
    ViewState()
) {

    override fun onLoadData() {
        getList()
    }

    fun onSaveGoal(goal: Goal) {
        saveGoal(goal)
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
            saveListUseCase.execute().also {
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

    override fun onReduceState(viewAction: Action)= when (viewAction) {
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
