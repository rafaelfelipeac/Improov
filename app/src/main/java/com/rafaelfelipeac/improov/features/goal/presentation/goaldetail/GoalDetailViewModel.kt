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

@Suppress("TooManyFunctions")
class GoalDetailViewModel @Inject constructor(
    private val saveGoalUseCase: SaveGoalUseCase,
    private val getGoalUseCase: GetGoalUseCase,
    private val saveItemUseCase: SaveItemUseCase,
    private val getItemListUseCase: GetItemListUseCase,
    private val saveHistoricUseCCase: SaveHistoricUseCase,
    private val getHistoricListUseCase: GetHistoricListUseCase
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
        saveGoal(goal)
    }

    fun onSaveItem(item: Item, isFromDragOnDrop: Boolean = false) {
        saveItem(item, isFromDragOnDrop)
    }

    fun onSaveHistoric(historic: Historic) {
        saveHistoric(historic)
    }

    private fun saveGoal(goal: Goal) {
        viewModelScope.launch {
            saveGoalUseCase(goal).also {
                if (it > 0) {
                    sendAction(Action.GoalSaved)
                }
            }
        }
    }

    private fun saveItem(item: Item, isFromDragOnDrop: Boolean) {
        viewModelScope.launch {
            saveItemUseCase(item).also {
                if (it > 0) {
                    if (!isFromDragOnDrop) {
                        sendAction(Action.ItemSaved)

                        getItems() // for now
                    }
                }
            }
        }
    }

    private fun saveHistoric(historic: Historic) {
        viewModelScope.launch {
            saveHistoricUseCCase(historic).also {
                if (it > 0) {
                    sendAction(Action.HistoricSaved)

                    getHistorics() // for now
                }
            }
        }
    }

    private fun getGoal() {
        viewModelScope.launch {
            getGoalUseCase(goalId).also {
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
            getItemListUseCase(goalId).also {
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
            getHistoricListUseCase(goalId).also {
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
        is Action.ItemSaved -> state.copy(
            itemSaved = true
        )
        is Action.ItemListLoaded -> state.copy(
            items = viewAction.items
        )
        is Action.HistoricSaved -> state.copy(
            historicSaved = true
        )
        is Action.HistoricListLoaded -> state.copy(
            historics = viewAction.historics
        )
    }

    data class ViewState(
        val goalSaved: Boolean = false,
        val goal: Goal = Goal(),
        val itemSaved: Boolean = false,
        val items: List<Item> = listOf(),
        val historicSaved: Boolean = false,
        val historics: List<Historic> = listOf()
    ) : BaseViewState

    sealed class Action : BaseAction {
        object GoalSaved : Action()
        class GoalLoaded(val goal: Goal) : Action()
        object ItemSaved : Action()
        class ItemListLoaded(val items: List<Item>) : Action()
        object HistoricSaved : Action()
        class HistoricListLoaded(val historics: List<Historic>) : Action()
    }
}
