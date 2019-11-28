package com.rafaelfelipeac.improov.features.habit

import androidx.lifecycle.LiveData
import com.rafaelfelipeac.improov.core.platform.base.BaseViewModel
import com.rafaelfelipeac.improov.features.commons.Habit
import com.rafaelfelipeac.improov.features.commons.HabitRepository
import javax.inject.Inject

class HabitViewModel @Inject constructor(
    private val habitRepository: HabitRepository
) : BaseViewModel() {

    private var habit: LiveData<Habit>? = null

    fun init(habitId: Long) {
        habit = habitRepository.getHabit(habitId)
    }

    // Habit
    fun getHabits(): LiveData<Habit>? {
        return habit
    }

    fun saveHabit(habit: Habit) {
        habitRepository.save(habit)
    }
}
