package com.rafaelfelipeac.improov.features.goal.presentation.goalform

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rafaelfelipeac.improov.features.commons.domain.model.Goal
import com.rafaelfelipeac.improov.features.goal.domain.usecase.firsttimeadd.GetFirstTimeAddUseCase
import com.rafaelfelipeac.improov.features.goal.domain.usecase.firsttimeadd.SaveFirstTimeAddUseCase
import com.rafaelfelipeac.improov.features.goal.domain.usecase.firsttimelist.SaveFirstTimeListUseCase
import com.rafaelfelipeac.improov.features.goal.domain.usecase.goal.GetGoalListUseCase
import com.rafaelfelipeac.improov.features.goal.domain.usecase.goal.GetGoalUseCase
import com.rafaelfelipeac.improov.features.goal.domain.usecase.goal.SaveGoalUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@Suppress("TooManyFunctions", "LongParameterList")
class GoalFormViewModel @Inject constructor(
    private val saveGoalUseCase: SaveGoalUseCase,
    private val getGoalUseCase: GetGoalUseCase,
    private val getGoalListUseCase: GetGoalListUseCase,
    private val saveFirstTimeListUseCase: SaveFirstTimeListUseCase,
    private val saveFirstTimeAddUseCase: SaveFirstTimeAddUseCase,
    private val getFirstTimeAddUseCase: GetFirstTimeAddUseCase
) : ViewModel() {

    private var goalId = 0L

    val savedGoal: Flow<Long> get() = _savedGoal.filterNotNull()
    private val _savedGoal = MutableStateFlow<Long?>(null)
    val goal: Flow<Goal> get() = _goal.filterNotNull()
    private val _goal = MutableStateFlow<Goal?>(null)
    val goals: Flow<List<Goal>> get() = _goals.filterNotNull()
    private val _goals = MutableStateFlow<List<Goal>?>(null)
    val savedFirstTimeList: Flow<Unit> get() = _savedFirstTimeList.filterNotNull()
    private val _savedFirstTimeList = MutableStateFlow<Unit?>(null)
    val savedFirstTimeAdd: Flow<Unit> get() = _savedFirstTimeAdd.filterNotNull()
    private val _savedFirstTimeAdd = MutableStateFlow<Unit?>(null)
    val firstTimeAdd: Flow<Boolean> get() = _firstTimeAdd.filterNotNull()
    private val _firstTimeAdd = MutableStateFlow<Boolean?>(null)

    fun setGoalId(goalId: Long) {
        this.goalId = goalId
    }

    fun loadData() {
        if (goalId > 0L) {
            getGoal()
        }

        getGoals()

        getFirstTimeAdd()
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

    fun saveFirstTimeAdd(firstTimeAdd: Boolean) {
        viewModelScope.launch {
            _savedFirstTimeAdd.value = saveFirstTimeAddUseCase(firstTimeAdd)
        }
    }

    private fun getFirstTimeAdd() {
        viewModelScope.launch {
            _firstTimeAdd.value = getFirstTimeAddUseCase()
        }
    }
}
