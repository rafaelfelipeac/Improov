package com.rafaelfelipeac.improov.features.goal.presentation.goalform

import androidx.lifecycle.viewModelScope
import com.rafaelfelipeac.improov.core.platform.base.BaseAction
import com.rafaelfelipeac.improov.core.platform.base.BaseViewModel
import com.rafaelfelipeac.improov.core.platform.base.BaseViewState
import com.rafaelfelipeac.improov.features.goal.domain.model.Goal
import com.rafaelfelipeac.improov.features.goal.domain.model.Historic
import com.rafaelfelipeac.improov.features.goal.domain.model.Item
import com.rafaelfelipeac.improov.features.goal.domain.usecase.goal.GetGoalListUseCase
import com.rafaelfelipeac.improov.features.goal.domain.usecase.goal.GetGoalUseCase
import com.rafaelfelipeac.improov.features.goal.domain.usecase.goal.SaveGoalUseCase
import com.rafaelfelipeac.improov.features.goal.domain.usecase.historic.DeleteHistoricUseCase
import com.rafaelfelipeac.improov.features.goal.domain.usecase.historic.GetHistoricListUseCase
import com.rafaelfelipeac.improov.features.goal.domain.usecase.item.DeleteItemUseCase
import com.rafaelfelipeac.improov.features.goal.domain.usecase.item.GetItemListUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class GoalFormViewModel @Inject constructor(
    private val saveGoalUseCase: SaveGoalUseCase,
    private val getGoalUseCase: GetGoalUseCase,
    private val getGoalListUseCase: GetGoalListUseCase,
    private val getItemListUseCase: GetItemListUseCase,
    private val deleteItemUseCase: DeleteItemUseCase,
    private val getHistoricListUseCase: GetHistoricListUseCase,
    private val deleteHistoricUseCase: DeleteHistoricUseCase
) : BaseViewModel<GoalFormViewModel.ViewState, GoalFormViewModel.Action>(
    ViewState()
) {
    private var goalId = 0L

    fun setGoalId(goalId: Long) {
        this.goalId = goalId
    }

    override fun onLoadData() {
        if (goalId > 0L) {
            getGoal()

            getItems()
            getHistorics()
        }

        getGoals()
    }

    fun onSaveGoal(goal: Goal) {
        viewModelScope.launch {
            saveGoalUseCase.execute(goal).also {
                if (it > 0) {
                    sendAction(Action.GoalSaved)
                } else {
                    sendAction(Action.GoalSaved)
                }
            }
        }
    }

    fun onDeleteItem(item: Item) {
        viewModelScope.launch {
            deleteItemUseCase.execute(item)
        }
    }

    fun onDeleteHistoric(historic: Historic) {
        viewModelScope.launch {
            deleteHistoricUseCase.execute(historic)
        }
    }

    private fun getGoal() {
        viewModelScope.launch {
            getGoalUseCase.execute(goalId).also {
                if (it.goalId > 0) {
                    sendAction(
                        Action.GoalLoaded(it)
                    )
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

    private fun getItems() {
        viewModelScope.launch {
            getItemListUseCase.execute(goalId).also {
                if (it.isNotEmpty()) {
                    sendAction(
                        Action.ItemListLoaded(it)
                    )
                }
            }
        }
    }

    private fun getHistorics() {
        viewModelScope.launch {
            getHistoricListUseCase.execute(goalId).also {
                if (it.isNotEmpty()) {
                    sendAction(
                        Action.HistoricListLoaded(it)
                    )
                }
            }
        }
    }

    override fun onReduceState(viewAction: Action) = when (viewAction) {
        is Action.GoalSaved -> state.copy(
            goalSaved = true
        )
        is Action.GoalLoaded -> state.copy(
            goal = viewAction.goal
        )
        is Action.GoalListLoaded -> state.copy(
            goals = viewAction.goals
        )
        is Action.ItemListLoaded -> state.copy(
            items = viewAction.items
        )
        is Action.HistoricListLoaded -> state.copy(
            historics = viewAction.historics
        )
    }

    data class ViewState(
        val goalSaved: Boolean = false,
        val goal: Goal = Goal(),
        val goals: List<Goal> = listOf(),
        val items: List<Item> = listOf(),
        val historics: List<Historic> = listOf()
    ) : BaseViewState

    sealed class Action : BaseAction {
        object GoalSaved : Action()
        class GoalLoaded(val goal: Goal) : Action()
        class GoalListLoaded(val goals: List<Goal>) : Action()
        class ItemListLoaded(val items: List<Item>) : Action()
        class HistoricListLoaded(val historics: List<Historic>) : Action()
    }
}
