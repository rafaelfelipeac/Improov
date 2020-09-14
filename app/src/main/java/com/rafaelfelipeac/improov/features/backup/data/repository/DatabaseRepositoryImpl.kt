package com.rafaelfelipeac.improov.features.backup.data.repository

import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.rafaelfelipeac.improov.core.persistence.sharedpreferences.Preferences
import com.rafaelfelipeac.improov.features.backup.data.model.Database
import com.rafaelfelipeac.improov.features.backup.domain.repository.DatabaseRepository
import com.rafaelfelipeac.improov.features.commons.data.dao.GoalDAO
import com.rafaelfelipeac.improov.features.commons.data.dao.HistoricDAO
import com.rafaelfelipeac.improov.features.commons.data.dao.ItemDAO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import java.util.Calendar

class DatabaseRepositoryImpl @Inject constructor(
    private val goalsDao: GoalDAO,
    private val historicDao: HistoricDAO,
    private val itemDao: ItemDAO,
    private val preferences: Preferences
) : DatabaseRepository {

    override suspend fun export(): String {
        return withContext(Dispatchers.IO) {
            try {
                val json = Gson().toJson(
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

                preferences.exportDate = Calendar.getInstance().timeInMillis

                json
            } catch (e: JsonParseException) {
                e.printStackTrace()

                ""
            }
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

                preferences.importDate = Calendar.getInstance().timeInMillis

                true
            } catch (e: JsonParseException) {
                e.printStackTrace()

                false
            }
        }
    }

    override suspend fun getExportDate(): Long {
        return withContext(Dispatchers.IO) {
            preferences.exportDate
        }
    }

    override suspend fun getImportDate(): Long {
        return withContext(Dispatchers.IO) {
            preferences.importDate
        }
    }
}
