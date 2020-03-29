package com.rafaelfelipeac.improov.core.persistence.sharedpreferences

import android.content.Context
import android.content.SharedPreferences

const val preferences_name = "com.rafaelfelipeac.improov.preferences"
const val KEY_WELCOME = "KEY_WELCOME"
const val KEY_NAME = "KEY_NAME"
const val KEY_WEEKDAYS = "KEY_WEEKDAYS"
const val KEY_LANGUAGE = "KEY_LANGUAGE"
const val KEY_FIRST_TIME_ADD = "KEY_FIRST_TIME_ADD"
const val KEY_FIRST_TIME_LIST = "KEY_FIRST_TIME_LIST"

class Preferences(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences(preferences_name, 0)

    var welcome: Boolean
        get() = prefs.getBoolean(KEY_WELCOME, false)
        set(value) = prefs.edit().putBoolean(KEY_WELCOME, value).apply()

    var fistTimeAdd: Boolean
        get() = prefs.getBoolean(KEY_FIRST_TIME_ADD, true)
        set(value) = prefs.edit().putBoolean(KEY_FIRST_TIME_ADD, value).apply()

    var fistTimeList: Boolean
        get() = prefs.getBoolean(KEY_FIRST_TIME_LIST, false)
        set(value) = prefs.edit().putBoolean(KEY_FIRST_TIME_LIST, value).apply()

    var name: String
        get() = prefs.getString(KEY_NAME, "")!!
        set(value) = prefs.edit().putString(KEY_NAME, value).apply()

    var openWeekDays: Boolean
        get() = prefs.getBoolean(KEY_WEEKDAYS, false)
        set(value) = prefs.edit().putBoolean(KEY_WEEKDAYS, value).apply()

    var language: String
        get() = prefs.getString(KEY_LANGUAGE, "en")!!
        set(value) = prefs.edit().putString(KEY_LANGUAGE, value).apply()
}
