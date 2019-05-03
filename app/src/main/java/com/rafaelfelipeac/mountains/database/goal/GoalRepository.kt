package com.rafaelfelipeac.mountains.database.goal

import androidx.lifecycle.LiveData
import com.rafaelfelipeac.mountains.models.Goal
import javax.inject.Inject
import androidx.lifecycle.MutableLiveData

class GoalRepository @Inject constructor(private val goalDAO: GoalDAO) {

    fun getGoals(): LiveData<List<Goal>> {
       return goalDAO.getAll()
    }

    fun getGoal(goalId: Long): LiveData<Goal> {
        return goalDAO.get(goalId)
    }

    fun insert(goal: Goal) {
        return goalDAO.insert(goal)
    }

    fun update(goal: Goal) {
        return goalDAO.update(goal)
    }

    fun delete(goal: Goal) {
        return goalDAO.delete(goal)
    }
}