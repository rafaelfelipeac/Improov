package com.rafaelfelipeac.improov.features.commons

import androidx.lifecycle.LiveData
import com.rafaelfelipeac.improov.features.commons.data.GoalDAO
import javax.inject.Inject

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
