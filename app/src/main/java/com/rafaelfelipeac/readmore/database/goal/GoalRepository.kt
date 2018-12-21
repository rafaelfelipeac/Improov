package com.rafaelfelipeac.readmore.database.goal

import com.rafaelfelipeac.readmore.models.Goal
import javax.inject.Inject

class GoalRepository @Inject constructor(private val goalDAO: GoalDAO) {

    fun getGoals(): List<Goal> {
        return goalDAO.getAll()
    }

    fun insert(goal: Goal) {
        return goalDAO.insert(goal)
    }
}