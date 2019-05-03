package com.rafaelfelipeac.mountains.ui.fragments.goalForm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.rafaelfelipeac.mountains.models.Goal
import com.rafaelfelipeac.mountains.ui.base.BaseViewModel

class GoalFormViewModel : BaseViewModel() {
    private var goalId: Long? = null

    private var goal: LiveData<Goal>? = null
    private var goals: LiveData<List<Goal>>? = null

    var goalIdInserted: MutableLiveData<Long> = MutableLiveData()

    init {
        goals = goalRepository.getGoals()
    }

    fun init(goalId: Long) {
        this.goalId = goalId

        goal = goalRepository.getGoal(goalId)
    }

    fun getGoals(): LiveData<List<Goal>>? {
        return goals
    }

    fun getGoal(): LiveData<Goal>? {
        return goal
    }

    fun saveGoal(goal: Goal) {
        val goalId = goalRepository.save(goal)

        goalIdInserted.value = goalId
    }
}