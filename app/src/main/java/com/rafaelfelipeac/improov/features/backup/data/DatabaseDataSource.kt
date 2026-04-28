package com.rafaelfelipeac.improov.features.backup.data

import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.google.gson.JsonParser
import com.rafaelfelipeac.improov.BuildConfig
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
                            appVersion = BuildConfig.VERSION_NAME,
                            settings =
                                Database.Settings(
                                    preferences.language,
                                    preferences.welcome,
                                    preferences.name,
                                    preferences.firstTimeList,
                                    preferences.firstTimeAdd,
                                ),
                            goals = goalDao.getAll(),
                            items = itemDao.getAll(),
                            historics = historicDao.getAll(),
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
                val database = parseDatabase(databaseBackup) ?: return@withContext false

                roomDatabase.runInTransaction {
                    goalDao.deleteAll()
                    itemDao.deleteAll()
                    historicDao.deleteAll()

                    database.goals.forEach { goalDao.save(it) }
                    database.items.forEach { itemDao.save(it) }
                    database.historics.forEach { historicDao.save(it) }
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
        preferences.language = database.settings.language
        preferences.welcome = database.settings.welcome
        preferences.name = database.settings.name
        preferences.firstTimeList = database.settings.firstTimeList
        preferences.firstTimeAdd = database.settings.firstTimeAdd
        preferences.importDate = Calendar.getInstance().timeInMillis
    }

    private fun parseDatabase(databaseBackup: String): Database? {
        val jsonObject =
            JsonParser.parseString(databaseBackup).takeIf { it.isJsonObject }?.asJsonObject ?: return null

        return when {
            isVersionedBackup(jsonObject) ->
                gson.fromJson(jsonObject, Database::class.java)

            isLegacyBackup(jsonObject) ->
                gson.fromJson(jsonObject, LegacyDatabase::class.java)?.toDatabase()

            else ->
                null
        }
    }

    private fun isVersionedBackup(jsonObject: com.google.gson.JsonObject): Boolean {
        return jsonObject.has("schemaVersion") &&
            jsonObject.get("schemaVersion").asInt == Database.CURRENT_SCHEMA_VERSION &&
            jsonObject.has("appVersion") &&
            jsonObject.has("settings") &&
            jsonObject.has("goals") &&
            jsonObject.has("items") &&
            jsonObject.has("historics")
    }

    private fun isLegacyBackup(jsonObject: com.google.gson.JsonObject): Boolean {
        return jsonObject.has("language") &&
            jsonObject.has("welcome") &&
            jsonObject.has("name") &&
            jsonObject.has("firstTimeList") &&
            jsonObject.has("firstTimeAdd") &&
            jsonObject.has("goals") &&
            jsonObject.has("items") &&
            jsonObject.has("historics")
    }

    private fun LegacyDatabase.toDatabase(): Database {
        return Database(
            appVersion = BuildConfig.VERSION_NAME,
            settings =
                Database.Settings(
                    language = language,
                    welcome = welcome,
                    name = name,
                    firstTimeList = firstTimeList,
                    firstTimeAdd = firstTimeAdd,
                ),
            goals = goals.orEmpty(),
            items = items.orEmpty(),
            historics = historics.orEmpty(),
        )
    }

    private data class LegacyDatabase(
        val language: String,
        val welcome: Boolean,
        val name: String,
        val firstTimeList: Boolean,
        val firstTimeAdd: Boolean,
        val goals: List<com.rafaelfelipeac.improov.features.commons.data.model.GoalDataModel>?,
        val items: List<com.rafaelfelipeac.improov.features.commons.data.model.ItemDataModel>?,
        val historics: List<com.rafaelfelipeac.improov.features.commons.data.model.HistoricDataModel>?,
    )
}
