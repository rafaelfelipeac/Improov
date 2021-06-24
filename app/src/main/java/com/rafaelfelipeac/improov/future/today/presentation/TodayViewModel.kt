package com.rafaelfelipeac.improov.future.today.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rafaelfelipeac.improov.features.commons.domain.model.Habit
import com.rafaelfelipeac.improov.future.today.domain.usecase.GetHabitListUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import javax.inject.Inject

class TodayViewModel @Inject constructor(
    private val getHabitListUseCase: GetHabitListUseCase
) : ViewModel() {

    val habits: Flow<List<Habit>> get() = _habits.filterNotNull()
    private val _habits = MutableStateFlow<List<Habit>?>(null)

    fun loadData() {
        getHabits()
    }

    private fun getHabits() {
        viewModelScope.launch {
            _habits.value = getHabitListUseCase()
        }
    }
}
