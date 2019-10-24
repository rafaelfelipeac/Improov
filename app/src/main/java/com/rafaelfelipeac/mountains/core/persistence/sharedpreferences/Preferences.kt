package com.rafaelfelipeac.mountains.core.persistence.sharedpreferences

import android.content.Context
import android.content.SharedPreferences

const val preferences_name = "com.rafaelfelipeac.mountains.preferences"
const val KEY_WELCOME = "KEY_WELCOME"
const val KEY_WEEKDAYS = "KEY_WEEKDAYS"
const val KEY_LANGUAGE = "KEY_LANGUAGE"

class Preferences(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences(preferences_name, 0)

    var welcome: Boolean
        get() = prefs.getBoolean(KEY_WELCOME, false)
        set(value) = prefs.edit().putBoolean(KEY_WELCOME, value).apply()

    var openWeekDays: Boolean
        get() = prefs.getBoolean(KEY_WEEKDAYS, false)
        set(value) = prefs.edit().putBoolean(KEY_WEEKDAYS, value).apply()

    var language: String
        get() = prefs.getString(KEY_LANGUAGE, "en")!!
        set(value) = prefs.edit().putString(KEY_LANGUAGE, value).apply()
}
