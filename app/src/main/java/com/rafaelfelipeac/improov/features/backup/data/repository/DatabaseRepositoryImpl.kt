package com.rafaelfelipeac.improov.features.backup.data.repository

import com.google.gson.Gson
import com.rafaelfelipeac.improov.core.persistence.sharedpreferences.Preferences
import com.rafaelfelipeac.improov.features.backup.data.model.Database
import com.rafaelfelipeac.improov.features.backup.domain.repository.DatabaseRepository
import com.rafaelfelipeac.improov.features.goal.data.dao.GoalDAO
import com.rafaelfelipeac.improov.features.goal.data.dao.HistoricDAO
import com.rafaelfelipeac.improov.features.goal.data.dao.ItemDAO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DatabaseRepositoryImpl @Inject constructor(
    private val goalsDao: GoalDAO,
    private val historicDao: HistoricDAO,
    private val itemDao: ItemDAO,
    private val preferences: Preferences
) : DatabaseRepository {

    override suspend fun export(): String {
        return withContext(Dispatchers.IO) {
            Gson().toJson(
                Database(
                    preferences.language,
                    preferences.welcome,
                    preferences.name,
                    preferences.firstTimeList,
                    preferences.firstTimeAdd,
                    goalsDao.getAll(),
                    itemDao.getAll(),
                    historicDao.getAll()
                )
            )
        }
    }

    override suspend fun import(databaseBackup: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val database = Gson().fromJson(databaseBackup, Database::class.java)

                database.goals.forEach { goalsDao.save(it) }
                database.items.forEach { itemDao.save(it) }
                database.historics.forEach { historicDao.save(it) }

                preferences.language = database.language
                preferences.welcome = database.welcome
                preferences.name = database.name
                preferences.firstTimeList = database.firstTimeList
                preferences.firstTimeAdd = database.firstTimeAdd

                true
            } catch (e: Exception) {
                e.printStackTrace()

                false
            }
        }
    }
}
