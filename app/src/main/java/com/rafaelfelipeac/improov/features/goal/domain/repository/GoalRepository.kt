package com.rafaelfelipeac.improov.features.goal.domain

import com.rafaelfelipeac.improov.features.goal.Goal

interface GoalRepository {

    suspend fun getGoals(): List<Goal>

    suspend fun getGoal(goalId: Long): Goal

    suspend fun save(goal: Goal): Long

    suspend fun delete(goal: Goal)

}
