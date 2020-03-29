package com.rafaelfelipeac.improov.features.goal.presentation.goaldetail

import androidx.lifecycle.viewModelScope
import com.rafaelfelipeac.improov.core.platform.base.BaseAction
import com.rafaelfelipeac.improov.core.platform.base.BaseViewModel
import com.rafaelfelipeac.improov.core.platform.base.BaseViewState
import com.rafaelfelipeac.improov.features.goal.domain.model.Goal
import com.rafaelfelipeac.improov.features.goal.domain.model.Historic
import com.rafaelfelipeac.improov.features.goal.domain.model.Item
import com.rafaelfelipeac.improov.features.goal.domain.usecase.goal.GetGoalUseCase
import com.rafaelfelipeac.improov.features.goal.domain.usecase.goal.SaveGoalUseCase
import com.rafaelfelipeac.improov.features.goal.domain.usecase.historic.GetHistoricListUseCase
import com.rafaelfelipeac.improov.features.goal.domain.usecase.historic.SaveHistoricUseCase
import com.rafaelfelipeac.improov.features.goal.domain.usecase.item.GetItemListUseCase
import com.rafaelfelipeac.improov.features.goal.domain.usecase.item.SaveItemUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class GoalDetailViewModel @Inject constructor(
    private val saveGoalUseCase: SaveGoalUseCase,
    private val getGoalUseCase: GetGoalUseCase,
    private val getItemListUseCase: GetItemListUseCase,
    private val saveItemUseCase: SaveItemUseCase,
    private val getHistoricListUseCase: GetHistoricListUseCase,
    private val saveHistoricUseCCase: SaveHistoricUseCase
) : BaseViewModel<GoalDetailViewModel.ViewState, GoalDetailViewModel.Action>(
    ViewState()
) {
    private var goalId = 0L

    fun setGoalId(goalId: Long) {
        this.goalId = goalId
    }

    override fun onLoadData() {
        getGoal()
    }

    fun onSaveGoal(goal: Goal) {
        viewModelScope.launch {
            saveGoalUseCase.execute(goal)
        }
    }

    fun onSaveItem(item: Item) {
        viewModelScope.launch {
            saveItemUseCase.execute(item).also {
                if (it > 0) {
                    getItems() // for now
                }
            }
        }
    }

    fun onSaveHistoric(historic: Historic) {
        viewModelScope.launch {
            saveHistoricUseCCase.execute(historic).also {
                if (it > 0) {
                    getHistorics() // for now
                }
            }
        }
    }

    private fun getGoal() {
        viewModelScope.launch {
            getGoalUseCase.execute(goalId).also {
                if (it.goalId > 0) {
                    getItems()
                    getHistorics()

                    sendAction(
                        Action.GoalLoaded(it)
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
        is Action.GoalLoaded -> state.copy(
            goal = viewAction.goal
        )
        is Action.ItemListLoaded -> state.copy(
            items = viewAction.items
        )
        is Action.HistoricListLoaded -> state.copy(
            historics = viewAction.historics
        )
    }

    data class ViewState(
        val goal: Goal = Goal(),
        val items: List<Item> = listOf(),
        val historics: List<Historic> = listOf()
    ) : BaseViewState

    sealed class Action : BaseAction {
        class GoalLoaded(val goal: Goal) : Action()
        class ItemListLoaded(val items: List<Item>) : Action()
        class HistoricListLoaded(val historics: List<Historic>) : Action()
    }
}
