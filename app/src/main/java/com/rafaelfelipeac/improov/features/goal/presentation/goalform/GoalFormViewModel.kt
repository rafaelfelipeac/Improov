package com.rafaelfelipeac.improov.features.goal.presentation.goalform

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.rafaelfelipeac.improov.core.platform.base.BaseViewModel
import com.rafaelfelipeac.improov.features.commons.domain.model.Goal
import com.rafaelfelipeac.improov.features.goal.domain.usecase.firsttimeadd.GetFirstTimeAddUseCase
import com.rafaelfelipeac.improov.features.goal.domain.usecase.firsttimeadd.SaveFirstTimeAddUseCase
import com.rafaelfelipeac.improov.features.goal.domain.usecase.firsttimelist.SaveFirstTimeListUseCase
import com.rafaelfelipeac.improov.features.goal.domain.usecase.goal.GetGoalListUseCase
import com.rafaelfelipeac.improov.features.goal.domain.usecase.goal.GetGoalUseCase
import com.rafaelfelipeac.improov.features.goal.domain.usecase.goal.SaveGoalUseCase
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
) : BaseViewModel() {

    private var goalId = 0L

    val savedGoal: LiveData<Long> get() = _savedGoal
    private val _savedGoal = MutableLiveData<Long>()
    val goal: LiveData<Goal> get() = _goal
    private val _goal = MutableLiveData<Goal>()
    val goals: LiveData<List<Goal>> get() = _goals
    private val _goals = MutableLiveData<List<Goal>>()
    val savedFirstTimeList: LiveData<Unit> get() = _savedFirstTimeList
    private val _savedFirstTimeList = MutableLiveData<Unit>()
    val savedFirstTimeAdd: LiveData<Unit> get() = _savedFirstTimeAdd
    private val _savedFirstTimeAdd = MutableLiveData<Unit>()
    val firstTimeAdd: LiveData<Boolean> get() = _firstTimeAdd
    private val _firstTimeAdd = MutableLiveData<Boolean>()

    fun setGoalId(goalId: Long) {
        this.goalId = goalId
    }

    override fun loadData() {
        if (goalId > 0L) {
            getGoal()
        }

        getGoals()

        getFirstTimeAdd()
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

    private fun getGoals() {
        viewModelScope.launch {
            _goals.postValue(getGoalListUseCase())
        }
    }

    fun saveFirstTimeList(firstTimeList: Boolean) {
        viewModelScope.launch {
            _savedFirstTimeList.postValue(saveFirstTimeListUseCase(firstTimeList))
        }
    }

    fun saveFirstTimeAdd(firstTimeAdd: Boolean) {
        viewModelScope.launch {
            _savedFirstTimeAdd.postValue(saveFirstTimeAddUseCase(firstTimeAdd))
        }
    }

    private fun getFirstTimeAdd() {
        viewModelScope.launch {
            _firstTimeAdd.postValue(getFirstTimeAddUseCase())
        }
    }
}
