package com.rafaelfelipeac.mountains.ui.fragments.stats

import androidx.lifecycle.LiveData
import com.rafaelfelipeac.mountains.models.Goal
import com.rafaelfelipeac.mountains.ui.base.BaseViewModel

class StatsViewModel: BaseViewModel() {
    fun insertGoal(goal: Goal) {
        goalRepository.insert(goal)
    }

    fun updateGoal(goal: Goal) {
        goalRepository.update(goal)
    }

    fun getGoals(): LiveData<List<Goal>> {
        return goalRepository.getGoals()
    }

    fun deleteGoal(goal: Goal) {
        goalRepository.delete(goal)
    }
}