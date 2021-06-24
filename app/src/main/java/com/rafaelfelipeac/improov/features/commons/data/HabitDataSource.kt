package com.rafaelfelipeac.improov.features.commons.data

import com.rafaelfelipeac.improov.features.commons.data.dao.HabitDao
import com.rafaelfelipeac.improov.features.commons.data.model.HabitDataModelMapper
import com.rafaelfelipeac.improov.features.commons.domain.model.Habit
import com.rafaelfelipeac.improov.features.commons.domain.repository.HabitRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HabitDataSource @Inject constructor(
    private val habitDao: HabitDao,
    private val habitDataModelMapper: HabitDataModelMapper
) : HabitRepository {

    override suspend fun getHabits(): List<Habit> {
        return withContext(Dispatchers.IO) {
            habitDao.getAll()
                .map { habitDataModelMapper.map(it) }
        }
    }

    override suspend fun getHabit(habitId: Long): Habit {
        return withContext(Dispatchers.IO) {
            habitDao.get(habitId)
                .let { habitDataModelMapper.map(it) }
        }
    }

    override suspend fun save(habit: Habit): Long {
        return withContext(Dispatchers.IO) {
            habitDao.save(habit
                .let { habitDataModelMapper.mapReverse(it) })
        }
    }

    override suspend fun delete(habit: Habit) {
        return withContext(Dispatchers.IO) {
            habitDao.delete(habit
                .let { habitDataModelMapper.mapReverse(it) })
        }
    }
}
