package com.rafaelfelipeac.improov.features.goal.data

import com.rafaelfelipeac.improov.features.goal.Goal
import com.rafaelfelipeac.improov.features.goal.domain.repository.GoalRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GoalRepositoryImpl @Inject constructor(private val goalsDao: GoalDAO): GoalRepository {

    override suspend fun getGoals(): List<Goal> {
        return withContext(Dispatchers.IO) {
            goalsDao.getAll()
        }
    }

    override suspend fun getGoal(goalId: Long): Goal {
        return withContext(Dispatchers.IO) {
            goalsDao.get(goalId)
        }
    }

    override suspend fun save(goal: Goal): Long {
        return withContext(Dispatchers.IO) {
            goalsDao.save(goal)
        }
    }

    override suspend fun delete(goal: Goal) {
        return withContext(Dispatchers.IO) {
            goalsDao.delete(goal)
        }
    }
}
