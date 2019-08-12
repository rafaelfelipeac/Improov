package com.rafaelfelipeac.mountains.ui.fragments.stats

import androidx.lifecycle.LiveData
import com.rafaelfelipeac.mountains.models.Goal
import com.rafaelfelipeac.mountains.models.Habit
import com.rafaelfelipeac.mountains.ui.base.BaseViewModel
import javax.inject.Inject

class StatsViewModel @Inject constructor() : BaseViewModel() {

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