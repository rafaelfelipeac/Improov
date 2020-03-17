package com.rafaelfelipeac.improov.future.habit

import androidx.lifecycle.LiveData
import javax.inject.Inject

class HabitRepository @Inject constructor() {

    fun getHabits(): LiveData<List<Habit>>? {
        return null
    }

    fun getHabit(habitId: Long): LiveData<Habit>? {
        return null
    }

    fun save(habit: Habit): Long? {
        return null
    }

    fun delete(habit: Habit): Unit? {
        return null
    }
}
