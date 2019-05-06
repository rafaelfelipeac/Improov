package com.rafaelfelipeac.mountains.ui.fragments.goalForm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.rafaelfelipeac.mountains.models.Goal
import com.rafaelfelipeac.mountains.ui.base.BaseViewModel

class GoalFormViewModel : BaseViewModel() {

    private var goal: LiveData<Goal>? = null
    private var goals: LiveData<List<Goal>>? = null

    var goalIdInserted: MutableLiveData<Long> = MutableLiveData()

    init {
        goals = goalRepository.getGoals()
    }

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
}