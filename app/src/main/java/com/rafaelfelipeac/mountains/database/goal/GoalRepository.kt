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

    fun save(goal: Goal): Long {
        return goalDAO.save(goal)
    }

    fun delete(goal: Goal) {
        return goalDAO.delete(goal)
    }
}