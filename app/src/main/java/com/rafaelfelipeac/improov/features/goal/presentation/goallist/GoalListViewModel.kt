package com.rafaelfelipeac.improov.features.goal.presentation.goallist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rafaelfelipeac.improov.features.commons.domain.model.Goal
import com.rafaelfelipeac.improov.features.goal.domain.usecase.firsttimelist.GetFirstTimeListUseCase
import com.rafaelfelipeac.improov.features.goal.domain.usecase.firsttimelist.SaveFirstTimeListUseCase
import com.rafaelfelipeac.improov.features.goal.domain.usecase.goal.GetGoalListUseCase
import com.rafaelfelipeac.improov.features.goal.domain.usecase.goal.SaveGoalUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import javax.inject.Inject

class GoalListViewModel @Inject constructor(
    private val saveGoalUseCase: SaveGoalUseCase,
    private val getGoalListUseCase: GetGoalListUseCase,
    private val saveFirstTimeListUseCase: SaveFirstTimeListUseCase,
    private val getFirstTimeListUseCase: GetFirstTimeListUseCase
) : ViewModel() {

    val savedGoal: Flow<Long> get() = _savedGoal.filterNotNull()
    private val _savedGoal = MutableStateFlow<Long?>(null)
    val goals: Flow<List<Goal>> get() = _goals.filterNotNull()
    private val _goals = MutableStateFlow<List<Goal>?>(null)
    val savedFirstTimeList: Flow<Unit> get() = _savedFirstTimeList.filterNotNull()
    private val _savedFirstTimeList = MutableStateFlow<Unit?>(null)
    val firstTimeList: Flow<Boolean> get() = _firstTimeList.filterNotNull()
    private val _firstTimeList = MutableStateFlow<Boolean?>(null)

    fun loadData() {
        getGoals()
        getFirstTimeList()
    }

    fun saveGoal(goal: Goal, isFromDragAndDrop: Boolean = false) {
        viewModelScope.launch {
            saveGoalUseCase(goal).also {
                if (!isFromDragAndDrop) {
                    _savedGoal.value = it
                }
            }
        }
    }

    private fun getGoals() {
        viewModelScope.launch {
            _goals.value = getGoalListUseCase()
        }
    }

    fun saveFirstTimeList(firstTimeList: Boolean) {
        viewModelScope.launch {
            _savedFirstTimeList.value = saveFirstTimeListUseCase(firstTimeList)
        }
    }

    private fun getFirstTimeList() {
        viewModelScope.launch {
            _firstTimeList.value = getFirstTimeListUseCase()
        }
    }
}
