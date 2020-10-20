package com.rafaelfelipeac.improov.features.goal.presentation.goaldetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

    val savedGoal: LiveData<Long> get() = _savedGoal
    private val _savedGoal = MutableLiveData<Long>()
    val goal: LiveData<Goal> get() = _goal
    private val _goal = MutableLiveData<Goal>()
    val savedItem: LiveData<Long> get() = _savedItem
    private val _savedItem = MutableLiveData<Long>()
    val items: LiveData<List<Item>> get() = _items
    private val _items = MutableLiveData<List<Item>>()
    val savedHistoric: LiveData<Long> get() = _savedHistoric
    private val _savedHistoric = MutableLiveData<Long>()
    val historics: LiveData<List<Historic>> get() = _historics
    private val _historics = MutableLiveData<List<Historic>>()

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
            _savedGoal.postValue(saveGoalUseCase(goal))
        }
    }

    private fun getGoal() {
        viewModelScope.launch {
            _goal.postValue(getGoalUseCase(goalId))
        }
    }

    fun saveItem(item: Item, isFromDragOnDrop: Boolean = false) {
        viewModelScope.launch {
            saveItemUseCase(item).also {
                if (!isFromDragOnDrop) {
                    _savedItem.postValue(it)
                }
            }
        }
    }

    fun getItems() {
        viewModelScope.launch {
            _items.postValue(getItemListUseCase(goalId))
        }
    }

    fun saveHistoric(historic: Historic) {
        viewModelScope.launch {
            _savedHistoric.postValue(saveHistoricUseCCase(historic))
        }
    }

    fun getHistorics() {
        viewModelScope.launch {
            _historics.postValue(getHistoricListUseCase(goalId))
        }
    }
}
