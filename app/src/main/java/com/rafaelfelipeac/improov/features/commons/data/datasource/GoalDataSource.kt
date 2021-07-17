package com.rafaelfelipeac.improov.features.commons.data.datasource

import com.rafaelfelipeac.improov.features.commons.data.dao.GoalDao
import com.rafaelfelipeac.improov.features.commons.data.model.GoalDataModelMapper
import com.rafaelfelipeac.improov.features.commons.domain.model.Goal
import com.rafaelfelipeac.improov.features.commons.domain.repository.GoalRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GoalDataSource @Inject constructor(
    private val goalDao: GoalDao,
    private val goalDataModelMapper: GoalDataModelMapper
) : GoalRepository {

    override suspend fun getGoals(): List<Goal> {
        return withContext(Dispatchers.IO) {
            goalDao.getAll()
                .map { goalDataModelMapper.map(it) }
        }
    }

    override suspend fun getGoal(goalId: Long): Goal {
        return withContext(Dispatchers.IO) {
            goalDao.get(goalId)
                .let { goalDataModelMapper.map(it) }
        }
    }

    override suspend fun save(goal: Goal): Long {
        return withContext(Dispatchers.IO) {
            goalDao.save(goal
                .let { goalDataModelMapper.mapReverse(it) })
        }
    }

    override suspend fun delete(goal: Goal) {
        return withContext(Dispatchers.IO) {
            goalDao.delete(goal
                .let { goalDataModelMapper.mapReverse(it) })
        }
    }
}
