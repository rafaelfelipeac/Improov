package com.rafaelfelipeac.improov.future.stats.domain.repository

import com.rafaelfelipeac.improov.features.commons.domain.model.Goal

interface GoalRepository {

    suspend fun getGoals(): List<Goal>

    suspend fun getGoal(goalId: Long): Goal

    suspend fun save(goal: Goal): Long

    suspend fun delete(goal: Goal)
}
