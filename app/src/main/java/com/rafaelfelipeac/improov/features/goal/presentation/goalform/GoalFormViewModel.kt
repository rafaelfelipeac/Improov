package com.rafaelfelipeac.improov.features.goal.presentation.goalform

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.rafaelfelipeac.improov.core.platform.base.BaseViewModel
import com.rafaelfelipeac.improov.features.commons.Goal
import com.rafaelfelipeac.improov.features.commons.GoalRepository
import com.rafaelfelipeac.improov.features.commons.Habit
import com.rafaelfelipeac.improov.features.commons.HabitRepository
import javax.inject.Inject

class GoalFormViewModel @Inject constructor(
    private val goalRepository: GoalRepository,
    private val habitRepository: HabitRepository
) : BaseViewModel() {

    private var goal: LiveData<Goal>? = null

    private var goals: LiveData<List<Goal>> = goalRepository.getGoals()
    private var habits: LiveData<List<Habit>> = habitRepository.getHabits()

    var goalIdInserted: MutableLiveData<Long> = MutableLiveData()

    fun init(goalId: Long) {
        goal = goalRepository.getGoal(goalId)
    }

    // Goal
    fun getGoals(): LiveData<List<Goal>>? {
        return goals
    }

    fun getGoal(): LiveData<Goal>? {
        return goal
    }

    fun saveGoal(goal: Goal) {
        goalIdInserted.value = goalRepository.save(goal)
    }

    // Habit
    fun getHabits(): LiveData<List<Habit>>? {
        return habits
    }
}