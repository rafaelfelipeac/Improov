package com.rafaelfelipeac.improov.core.persistence.sharedpreferences

import android.content.Context
import android.content.SharedPreferences

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

    var welcome: Boolean
        get() = prefs.getBoolean(KEY_WELCOME, false)
        set(value) = prefs.edit().putBoolean(KEY_WELCOME, value).apply()

    var name: String
        get() = prefs.getString(KEY_NAME, "")!!
        set(value) = prefs.edit().putString(KEY_NAME, value).apply()

    var language: String
        get() = prefs.getString(KEY_LANGUAGE, "en")!!
        set(value) = prefs.edit().putString(KEY_LANGUAGE, value).apply()

    var firstTimeAdd: Boolean
        get() = prefs.getBoolean(KEY_FIRST_TIME_ADD, true)
        set(value) = prefs.edit().putBoolean(KEY_FIRST_TIME_ADD, value).apply()

    var firstTimeList: Boolean
        get() = prefs.getBoolean(KEY_FIRST_TIME_LIST, false)
        set(value) = prefs.edit().putBoolean(KEY_FIRST_TIME_LIST, value).apply()

    var exportDate: Long
        get() = prefs.getLong(KEY_EXPORT_DATE, 0L)
        set(value) = prefs.edit().putLong(KEY_EXPORT_DATE, value).apply()

    var importDate: Long
        get() = prefs.getLong(KEY_IMPORT_DATE, 0L)
        set(value) = prefs.edit().putLong(KEY_IMPORT_DATE, value).apply()
}
