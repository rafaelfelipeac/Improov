package com.rafaelfelipeac.mountains.ui.fragments.goals

import androidx.lifecycle.LiveData
import com.rafaelfelipeac.mountains.models.Goal
import com.rafaelfelipeac.mountains.ui.base.BaseViewModel

class GoalsViewModel : BaseViewModel() {
    private var goals: LiveData<List<Goal>>? = null

    init {
        goals = goalRepository.getGoals()
    }

    fun getGoals(): LiveData<List<Goal>>? {
        return goals
    }

    fun insertGoal(goal: Goal) {
        goalRepository.insert(goal)
    }

    fun updateGoal(goal: Goal) {
        goalRepository.update(goal)
    }

    fun deleteGoal(goal: Goal) {
        goalRepository.delete(goal)
    }
}