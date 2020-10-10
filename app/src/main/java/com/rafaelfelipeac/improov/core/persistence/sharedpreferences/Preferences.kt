package com.rafaelfelipeac.improov.core.persistence.sharedpreferences

import android.content.Context
import android.content.SharedPreferences
import kotlin.reflect.KProperty

const val PREFERENCES_NAME = "com.rafaelfelipeac.improov.preferences"

const val KEY_WELCOME = "KEY_WELCOME"
const val KEY_NAME = "KEY_NAME"
const val KEY_LANGUAGE = "KEY_LANGUAGE"
const val KEY_FIRST_TIME_ADD = "KEY_FIRST_TIME_ADD"
const val KEY_FIRST_TIME_LIST = "KEY_FIRST_TIME_LIST"
const val KEY_EXPORT_DATE = "KEY_EXPORT_DATE"
const val KEY_IMPORT_DATE = "KEY_IMPORT_DATE"

class Preferences(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, 0)

    var welcome: Boolean by prefs.persistedBoolean(KEY_WELCOME, defaultValue = false)

    var name: String by prefs.persistedString(KEY_NAME, defaultValue = "")

    var language: String by prefs.persistedString(KEY_LANGUAGE, defaultValue = "en")

    var firstTimeAdd: Boolean by prefs.persistedBoolean(KEY_FIRST_TIME_ADD, defaultValue = true)

    var firstTimeList: Boolean by prefs.persistedBoolean(KEY_FIRST_TIME_LIST, defaultValue = false)

    var exportDate: Long by prefs.persistedLong(KEY_EXPORT_DATE, defaultValue = 0L)

    var importDate: Long by prefs.persistedLong(KEY_IMPORT_DATE, defaultValue = 0L)
}

// region SharedPreferences extension
private class PersistedBoolean(
    private val sharedPreferences: SharedPreferences,
    private val key: String,
    private val defaultValue: Boolean
) {
    operator fun getValue(thisRef: Any, property: KProperty<*>): Boolean =
        sharedPreferences.getBoolean(key, defaultValue)

    operator fun setValue(thisRef: Any, property: KProperty<*>, value: Boolean) {
        sharedPreferences.edit { putBoolean(key, value) }
    }
}

private fun SharedPreferences.persistedBoolean(
    key: String,
    defaultValue: Boolean
) = PersistedBoolean(this, key, defaultValue)

private class PersistedString(
    private val sharedPreferences: SharedPreferences,
    private val key: String,
    private val defaultValue: String
) {
    operator fun getValue(thisRef: Any, property: KProperty<*>): String =
        sharedPreferences.getString(key, defaultValue) ?: defaultValue

    operator fun setValue(thisRef: Any, property: KProperty<*>, value: String) {
        sharedPreferences.edit { putString(key, value) }
    }
}

private fun SharedPreferences.persistedString(
    key: String,
    defaultValue: String
) = PersistedString(this, key, defaultValue)

private class PersistedLong(
    private val sharedPreferences: SharedPreferences,
    private val key: String,
    private val defaultValue: Long
) {
    operator fun getValue(thisRef: Any, property: KProperty<*>): Long =
        sharedPreferences.getLong(key, defaultValue)

    operator fun setValue(thisRef: Any, property: KProperty<*>, value: Long) {
        sharedPreferences.edit { putLong(key, value) }
    }
}

private fun SharedPreferences.persistedLong(
    key: String,
    defaultValue: Long
) = PersistedLong(this, key, defaultValue)

private inline fun SharedPreferences.edit(operation: SharedPreferences.Editor.() -> Unit) {
    val editor = edit()
    operation(editor)
    editor.apply()
}
// endregion
