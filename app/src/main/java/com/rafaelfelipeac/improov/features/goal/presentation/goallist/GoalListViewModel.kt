package com.rafaelfelipeac.improov.features.goal.presentation.goallist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.rafaelfelipeac.improov.core.platform.base.BaseViewModel
import com.rafaelfelipeac.improov.features.goal.domain.model.Goal
import com.rafaelfelipeac.improov.features.goal.domain.usecase.firsttimelist.GetFirstTimeListUseCase
import com.rafaelfelipeac.improov.features.goal.domain.usecase.firsttimelist.SaveFirstTimeListUseCase
import com.rafaelfelipeac.improov.features.goal.domain.usecase.goal.GetGoalListUseCase
import com.rafaelfelipeac.improov.features.goal.domain.usecase.goal.SaveGoalUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class GoalListViewModel @Inject constructor(
    private val saveGoalUseCase: SaveGoalUseCase,
    private val getGoalListUseCase: GetGoalListUseCase,
    private val saveFirstTimeListUseCase: SaveFirstTimeListUseCase,
    private val getFirstTimeListUseCase: GetFirstTimeListUseCase
) : BaseViewModel() {

    val savedGoal: LiveData<Long> get() = _savedGoal
    private val _savedGoal = MutableLiveData<Long>()
    val goals: LiveData<List<Goal>> get() = _goals
    private val _goals = MutableLiveData<List<Goal>>()
    val savedFirstTimeList: LiveData<Unit> get() = _savedFirstTimeList
    private val _savedFirstTimeList = MutableLiveData<Unit>()
    val firstTimeList: LiveData<Boolean> get() = _firstTimeList
    private val _firstTimeList = MutableLiveData<Boolean>()

    override fun loadData() {
        getGoals()
        getFirstTimeList()
    }

    fun saveGoal(goal: Goal, isFromDragAndDrop: Boolean = false) {
        viewModelScope.launch {
            saveGoalUseCase(goal).also {
                if (!isFromDragAndDrop) {
                    _savedGoal.postValue(it)
                }
            }
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

    private fun getFirstTimeList() {
        viewModelScope.launch {
            _firstTimeList.postValue(getFirstTimeListUseCase())
        }
    }
}
