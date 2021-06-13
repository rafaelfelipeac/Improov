package com.rafaelfelipeac.improov.future.habit.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rafaelfelipeac.improov.features.commons.domain.model.Habit
import com.rafaelfelipeac.improov.future.habit.domain.usecase.GetHabitListUseCase
import com.rafaelfelipeac.improov.future.habit.domain.usecase.GetHabitUseCase
import com.rafaelfelipeac.improov.future.habit.domain.usecase.SaveHabitUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import javax.inject.Inject

class HabitFormViewModel @Inject constructor(
    private val saveHabitUseCase: SaveHabitUseCase,
    private val getHabitUseCase: GetHabitUseCase,
    private val getHabitListUseCase: GetHabitListUseCase
) : ViewModel() {

    private var habitId = 0L

    val savedHabit: Flow<Long> get() = _savedHabit.filterNotNull()
    private val _savedHabit = MutableStateFlow<Long?>(null)
    val habit: Flow<Habit> get() = _habit.filterNotNull()
    private val _habit = MutableStateFlow<Habit?>(null)
    val habits: Flow<List<Habit>> get() = _habits.filterNotNull()
    private val _habits = MutableStateFlow<List<Habit>?>(null)

    fun setHabitId(habitId: Long) {
        this.habitId = habitId
    }

    fun loadData() {
        if (habitId > 0L) {
            getHabit()
        }

        getHabits()
    }

    fun saveHabit(habit: Habit) {
        viewModelScope.launch {
            _savedHabit.value = saveHabitUseCase(habit)
        }
    }

    private fun getHabit() {
        viewModelScope.launch {
            _habit.value = getHabitUseCase(habitId)
        }
    }

    private fun getHabits() {
        viewModelScope.launch {
            _habits.value = getHabitListUseCase()
        }
    }
}
