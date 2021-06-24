package com.rafaelfelipeac.improov.future.stats.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rafaelfelipeac.improov.features.commons.domain.model.Goal
import com.rafaelfelipeac.improov.features.commons.domain.model.Habit
import com.rafaelfelipeac.improov.future.stats.domain.usecase.GetGoalListUseCase
import com.rafaelfelipeac.improov.future.stats.domain.usecase.GetHabitListUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import javax.inject.Inject

class StatsViewModel @Inject constructor(
    private val getGoalListUseCase: GetGoalListUseCase,
    private val getHabitListUseCase: GetHabitListUseCase
) : ViewModel() {

    val goals: Flow<List<Goal>> get() = _goals.filterNotNull()
    private val _goals = MutableStateFlow<List<Goal>?>(null)
    val habits: Flow<List<Habit>> get() = _habits.filterNotNull()
    private val _habits = MutableStateFlow<List<Habit>?>(null)

    fun loadData() {
        getGoals()
        getHabits()
    }

    private fun getGoals() {
        viewModelScope.launch {
            _goals.value = getGoalListUseCase()
        }
    }

    private fun getHabits() {
        viewModelScope.launch {
            _habits.value = getHabitListUseCase()
        }
    }
}
