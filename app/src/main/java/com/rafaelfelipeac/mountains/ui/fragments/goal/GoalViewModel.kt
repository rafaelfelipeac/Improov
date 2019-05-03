package com.rafaelfelipeac.mountains.ui.fragments.goal

import androidx.lifecycle.LiveData
import com.rafaelfelipeac.mountains.models.Goal
import com.rafaelfelipeac.mountains.ui.base.BaseViewModel

class GoalViewModel: BaseViewModel() {
    private var goalId: Long? = null
    private var goal: LiveData<Goal>? = null

    fun init(goalId: Long) {
        this.goalId = goalId

        goal = goalRepository.getGoal(goalId)
    }

    fun getGoal(): LiveData<Goal>? {
        return goal
    }

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