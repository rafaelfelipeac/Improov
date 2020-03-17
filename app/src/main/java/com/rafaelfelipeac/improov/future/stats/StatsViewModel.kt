package com.rafaelfelipeac.improov.future.stats

import androidx.lifecycle.LiveData
import com.rafaelfelipeac.improov.core.platform.base.BaseViewModel
import com.rafaelfelipeac.improov.features.goal.domain.model.Goal
import com.rafaelfelipeac.improov.features.goal.domain.repository.GoalRepository
import com.rafaelfelipeac.improov.future.habit.Habit
import com.rafaelfelipeac.improov.future.habit.HabitRepository
import javax.inject.Inject

class StatsViewModel @Inject constructor(
    private val goalRepository: GoalRepository,
    private val habitRepository: HabitRepository
) : BaseViewModel() {

    private var goals: LiveData<List<Goal>>? = null
    private var habits: LiveData<List<Habit>>? = null

    init {
        //goals = goalRepository.getGoals()
        habits = habitRepository.getHabits()
    }

    // Goal
    fun getGoals(): LiveData<List<Goal>>? {
        return goals
    }

    // Habit
    fun getHabits(): LiveData<List<Habit>>? {
        return habits
    }
}
