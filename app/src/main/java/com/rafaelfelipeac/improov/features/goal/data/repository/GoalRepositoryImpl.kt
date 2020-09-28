package com.rafaelfelipeac.improov.features.goal.data.repository

import com.rafaelfelipeac.improov.features.commons.data.dao.GoalDAO
import com.rafaelfelipeac.improov.features.commons.data.model.GoalDataModelMapper
import com.rafaelfelipeac.improov.features.commons.domain.model.Goal
import com.rafaelfelipeac.improov.features.goal.domain.repository.GoalRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GoalRepositoryImpl @Inject constructor(
    private val goalsDao: GoalDAO,
    private val goalDataModelMapper: GoalDataModelMapper
) : GoalRepository {

    override suspend fun getGoals(): List<Goal> {
        return withContext(Dispatchers.IO) {
            goalsDao.getAll()
                .map { goalDataModelMapper.map(it) }
        }
    }

    override suspend fun getGoal(goalId: Long): Goal {
        return withContext(Dispatchers.IO) {
            goalsDao.get(goalId)
                .let { goalDataModelMapper.map(it) }
        }
    }

    override suspend fun save(goal: Goal): Long {
        return withContext(Dispatchers.IO) {
            goalsDao.save(goal
                .let { goalDataModelMapper.mapReverse(it) })
        }
    }

    override suspend fun delete(goal: Goal) {
        return withContext(Dispatchers.IO) {
            goalsDao.delete(goal
                .let { goalDataModelMapper.mapReverse(it) })
        }
    }
}
