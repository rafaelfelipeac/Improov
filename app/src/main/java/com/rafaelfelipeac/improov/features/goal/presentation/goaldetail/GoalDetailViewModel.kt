package com.rafaelfelipeac.improov.features.goal.presentation.goaldetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rafaelfelipeac.improov.features.commons.domain.model.Goal
import com.rafaelfelipeac.improov.features.commons.domain.model.Historic
import com.rafaelfelipeac.improov.features.commons.domain.model.Item
import com.rafaelfelipeac.improov.features.goal.domain.usecase.goal.GetGoalUseCase
import com.rafaelfelipeac.improov.features.goal.domain.usecase.goal.SaveGoalUseCase
import com.rafaelfelipeac.improov.features.goal.domain.usecase.historic.GetHistoricListUseCase
import com.rafaelfelipeac.improov.features.goal.domain.usecase.historic.SaveHistoricUseCase
import com.rafaelfelipeac.improov.features.goal.domain.usecase.item.GetItemListUseCase
import com.rafaelfelipeac.improov.features.goal.domain.usecase.item.SaveItemUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
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
) : ViewModel() {

    private var goalId = 0L

    val savedGoal: Flow<Long> get() = _savedGoal.filterNotNull()
    private val _savedGoal = MutableStateFlow<Long?>(null)
    val goal: Flow<Goal> get() = _goal.filterNotNull()
    private val _goal = MutableStateFlow<Goal?>(null)
    val savedItem: Flow<Long> get() = _savedItem.filterNotNull()
    private val _savedItem = MutableStateFlow<Long?>(null)
    val items: Flow<List<Item>> get() = _items.filterNotNull()
    private val _items = MutableStateFlow<List<Item>?>(null)
    val savedHistoric: Flow<Long> get() = _savedHistoric.filterNotNull()
    private val _savedHistoric = MutableStateFlow<Long?>(null)
    val historics: Flow<List<Historic>> get() = _historics.filterNotNull()
    private val _historics = MutableStateFlow<List<Historic>?>(null)

    fun setGoalId(goalId: Long) {
        this.goalId = goalId
    }

    fun loadData() {
        if (goalId > 0L) {
            getGoal()

            getItems()
            getHistorics()
        }
    }

    fun saveGoal(goal: Goal) {
        viewModelScope.launch {
            _savedGoal.value = saveGoalUseCase(goal)
        }
    }

    private fun getGoal() {
        viewModelScope.launch {
            _goal.value = getGoalUseCase(goalId)
        }
    }

    fun saveItem(item: Item, isFromDragOnDrop: Boolean = false) {
        viewModelScope.launch {
            saveItemUseCase(item).also {
                if (!isFromDragOnDrop) {
                    _savedItem.value = it
                }
            }
        }
    }

    fun getItems() {
        viewModelScope.launch {
            _items.value = getItemListUseCase(goalId)
        }
    }

    fun saveHistoric(historic: Historic) {
        viewModelScope.launch {
            _savedHistoric.value = saveHistoricUseCCase(historic)
        }
    }

    fun getHistorics() {
        viewModelScope.launch {
            _historics.value = getHistoricListUseCase(goalId)
        }
    }
}
