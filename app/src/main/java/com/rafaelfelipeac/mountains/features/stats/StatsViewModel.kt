package com.rafaelfelipeac.mountains.features.stats

import androidx.lifecycle.LiveData
import com.rafaelfelipeac.mountains.core.platform.base.BaseViewModel
import com.rafaelfelipeac.mountains.features.commons.Goal
import com.rafaelfelipeac.mountains.features.commons.GoalRepository
import com.rafaelfelipeac.mountains.features.commons.Habit
import com.rafaelfelipeac.mountains.features.commons.HabitRepository
import javax.inject.Inject

class StatsViewModel @Inject constructor(
    private val goalRepository: GoalRepository,
    private val habitRepository: HabitRepository
) : BaseViewModel() {

    private var goals: LiveData<List<Goal>>? = null
    private var habits: LiveData<List<Habit>>? = null

    init {
        goals = goalRepository.getGoals()
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