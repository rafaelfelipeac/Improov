package com.rafaelfelipeac.domore.database.goal

import com.rafaelfelipeac.domore.models.Goal
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