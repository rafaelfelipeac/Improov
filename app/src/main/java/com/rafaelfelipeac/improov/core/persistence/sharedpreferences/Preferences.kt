package com.rafaelfelipeac.improov.core.persistence.sharedpreferences

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

const val PREFERENCES_NAME = "com.rafaelfelipeac.improov.preferences"

private val Context.settingsDataStore by preferencesDataStore(name = PREFERENCES_NAME)

private val KEY_WELCOME = booleanPreferencesKey("KEY_WELCOME")
private val KEY_NAME = stringPreferencesKey("KEY_NAME")
private val KEY_LANGUAGE = stringPreferencesKey("KEY_LANGUAGE")
private val KEY_FIRST_TIME_ADD = booleanPreferencesKey("KEY_FIRST_TIME_ADD")
private val KEY_FIRST_TIME_LIST = booleanPreferencesKey("KEY_FIRST_TIME_LIST")
private val KEY_EXPORT_DATE = longPreferencesKey("KEY_EXPORT_DATE")
private val KEY_IMPORT_DATE = longPreferencesKey("KEY_IMPORT_DATE")

class Preferences(context: Context) {

    private val dataStore = context.applicationContext.settingsDataStore

    var welcome: Boolean
        get() = readBoolean(KEY_WELCOME, defaultValue = false)
        set(value) = writeBoolean(KEY_WELCOME, value)

    var name: String
        get() = readString(KEY_NAME, defaultValue = "")
        set(value) = writeString(KEY_NAME, value)

    var language: String
        get() = readString(KEY_LANGUAGE, defaultValue = "en")
        set(value) = writeString(KEY_LANGUAGE, value)

    var firstTimeAdd: Boolean
        get() = readBoolean(KEY_FIRST_TIME_ADD, defaultValue = true)
        set(value) = writeBoolean(KEY_FIRST_TIME_ADD, value)

    var firstTimeList: Boolean
        get() = readBoolean(KEY_FIRST_TIME_LIST, defaultValue = false)
        set(value) = writeBoolean(KEY_FIRST_TIME_LIST, value)

    var exportDate: Long
        get() = readLong(KEY_EXPORT_DATE, defaultValue = 0L)
        set(value) = writeLong(KEY_EXPORT_DATE, value)

    var importDate: Long
        get() = readLong(KEY_IMPORT_DATE, defaultValue = 0L)
        set(value) = writeLong(KEY_IMPORT_DATE, value)

    private fun readBoolean(
        key: androidx.datastore.preferences.core.Preferences.Key<Boolean>,
        defaultValue: Boolean,
    ): Boolean {
        return runBlocking(Dispatchers.IO) {
            dataStore.data.first()[key] ?: defaultValue
        }
    }

    private fun readString(
        key: androidx.datastore.preferences.core.Preferences.Key<String>,
        defaultValue: String,
    ): String {
        return runBlocking(Dispatchers.IO) {
            dataStore.data.first()[key] ?: defaultValue
        }
    }

    private fun readLong(
        key: androidx.datastore.preferences.core.Preferences.Key<Long>,
        defaultValue: Long,
    ): Long {
        return runBlocking(Dispatchers.IO) {
            dataStore.data.first()[key] ?: defaultValue
        }
    }

    private fun writeBoolean(
        key: androidx.datastore.preferences.core.Preferences.Key<Boolean>,
        value: Boolean,
    ) {
        runBlocking(Dispatchers.IO) {
            dataStore.edit { preferences -> preferences[key] = value }
        }
    }

    private fun writeString(
        key: androidx.datastore.preferences.core.Preferences.Key<String>,
        value: String,
    ) {
        runBlocking(Dispatchers.IO) {
            dataStore.edit { preferences -> preferences[key] = value }
        }
    }

    private fun writeLong(
        key: androidx.datastore.preferences.core.Preferences.Key<Long>,
        value: Long,
    ) {
        runBlocking(Dispatchers.IO) {
            dataStore.edit { preferences -> preferences[key] = value }
        }
    }
}
