package com.rafaelfelipeac.mountains.database.goal

import com.rafaelfelipeac.mountains.models.Goal
import javax.inject.Inject

class GoalRepository @Inject constructor(private val goalDAO: GoalDAO) {

    fun getGoals(): List<Goal> {
        return goalDAO.getAll()
    }

    fun insert(goal: Goal) {
        return goalDAO.insert(goal)
    }

    fun update(goal: Goal) {
        return goalDAO.update(goal)
    }
}