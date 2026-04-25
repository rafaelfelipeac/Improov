package com.rafaelfelipeac.improov.features.backup.data

import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.rafaelfelipeac.improov.core.persistence.database.RoomDatabase
import com.rafaelfelipeac.improov.core.persistence.sharedpreferences.Preferences
import com.rafaelfelipeac.improov.features.backup.data.model.Database
import com.rafaelfelipeac.improov.features.backup.domain.repository.DatabaseRepository
import com.rafaelfelipeac.improov.features.commons.data.dao.GoalDao
import com.rafaelfelipeac.improov.features.commons.data.dao.HistoricDao
import com.rafaelfelipeac.improov.features.commons.data.dao.ItemDao
import java.util.Calendar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DatabaseDataSource(
    private val roomDatabase: RoomDatabase,
    private val goalDao: GoalDao,
    private val historicDao: HistoricDao,
    private val itemDao: ItemDao,
    private val preferences: Preferences,
    private val gson: Gson,
) : DatabaseRepository {
    override suspend fun export(): String {
        return withContext(Dispatchers.IO) {
            try {
                val json =
                    gson.toJson(
                        Database(
                            preferences.language,
                            preferences.welcome,
                            preferences.name,
                            preferences.firstTimeList,
                            preferences.firstTimeAdd,
                            goalDao.getAll(),
                            itemDao.getAll(),
                            historicDao.getAll(),
                        ),
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
                val database = gson.fromJson(databaseBackup, Database::class.java) ?: return@withContext false

                roomDatabase.runInTransaction {
                    goalDao.deleteAll()
                    itemDao.deleteAll()
                    historicDao.deleteAll()

                    database.goals?.forEach { goalDao.save(it) }
                    database.items?.forEach { itemDao.save(it) }
                    database.historics?.forEach { historicDao.save(it) }
                }

                importPreferences(database)

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

    private fun importPreferences(database: Database) {
        preferences.language = database.language
        preferences.welcome = database.welcome
        preferences.name = database.name
        preferences.firstTimeList = database.firstTimeList
        preferences.firstTimeAdd = database.firstTimeAdd
        preferences.importDate = Calendar.getInstance().timeInMillis
    }
}
