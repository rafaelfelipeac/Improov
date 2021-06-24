package com.rafaelfelipeac.improov.features.commons.domain.repository

import com.rafaelfelipeac.improov.features.commons.domain.model.Habit

interface HabitRepository {

    suspend fun getHabits(): List<Habit>

    suspend fun getHabit(habitId: Long): Habit

    suspend fun save(habit: Habit): Long

    suspend fun delete(habit: Habit)
}
