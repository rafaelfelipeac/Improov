package com.rafaelfelipeac.mountains.features.today.presentation

import androidx.lifecycle.LiveData
import com.rafaelfelipeac.mountains.core.platform.base.BaseViewModel
import com.rafaelfelipeac.mountains.features.commons.Goal
import com.rafaelfelipeac.mountains.features.commons.GoalRepository
import com.rafaelfelipeac.mountains.features.commons.Habit
import com.rafaelfelipeac.mountains.features.commons.HabitRepository
import javax.inject.Inject

class TodayViewModel @Inject constructor(
    private val goalRepository: GoalRepository,
    private val habitRepository: HabitRepository
) : BaseViewModel() {

    private var habits: LiveData<List<Habit>>? = null
    private var goals: LiveData<List<Goal>>? = null

    init {
        habits = habitRepository.getHabits()
        goals = goalRepository.getGoals()
    }

    // Goal
    fun getGoals(): LiveData<List<Goal>>? {
        return goals
    }

    fun saveGoal(goal: Goal) {
        goalRepository.save(goal)

        this.goals = goalRepository.getGoals()
    }

    // Habit
    fun getHabits(): LiveData<List<Habit>>? {
        return habits
    }

    fun saveHabit(habit: Habit) {
        habitRepository.save(habit)

        this.habits = habitRepository.getHabits()
    }
}