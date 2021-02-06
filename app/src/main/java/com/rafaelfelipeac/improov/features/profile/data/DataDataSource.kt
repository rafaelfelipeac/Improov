package com.rafaelfelipeac.improov.features.profile.data

import com.rafaelfelipeac.improov.features.commons.data.dao.GoalDao
import com.rafaelfelipeac.improov.features.commons.data.dao.HistoricDao
import com.rafaelfelipeac.improov.features.commons.data.dao.ItemDao
import com.rafaelfelipeac.improov.features.commons.data.enums.GoalType
import com.rafaelfelipeac.improov.features.commons.data.model.GoalDataModelMapper
import com.rafaelfelipeac.improov.features.commons.domain.model.Goal
import com.rafaelfelipeac.improov.features.profile.domain.repository.DataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DataDataSource @Inject constructor(
    private val goalDao: GoalDao,
    private val itemDao: ItemDao,
    private val historicDao: HistoricDao,
    private val goalDataModelMapper: GoalDataModelMapper
) : DataRepository {

    override suspend fun generateData() {
        return withContext(Dispatchers.IO) {
            val goals = listOf(
                Goal(
                    name = "Counter",
                    type = GoalType.GOAL_COUNTER,
                    incrementValue = 1F,
                    decrementValue = 1F,
                    divideAndConquer = true,
                    bronzeValue = 5F,
                    silverValue = 10F,
                    goldValue = 15F
                ),
                Goal(
                    name = "List",
                    type = GoalType.GOAL_LIST,
                    divideAndConquer = false,
                    singleValue = 10F
                ),
                Goal(
                    name = "Final",
                    type = GoalType.GOAL_FINAL,
                    divideAndConquer = true,
                    bronzeValue = 15F,
                    silverValue = 50F,
                    goldValue = 100F
                )
            )

            goals.forEach { goal ->
                goalDao.save(goalDataModelMapper.mapReverse(goal))
            }
        }
    }

    override suspend fun clearData() {
        return withContext(Dispatchers.IO) {
            goalDao.getAll().forEach { goal -> goalDao.delete(goal) }
            itemDao.getAll().forEach { item -> itemDao.delete(item) }
            historicDao.getAll().forEach { historic -> historicDao.delete(historic) }
        }
    }
}
