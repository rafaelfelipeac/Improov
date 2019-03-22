package com.rafaelfelipeac.mountains.app

import android.content.Context
import android.content.SharedPreferences

class Prefs(context: Context) {
    private val constPrefsFilename = "com.rafaelfelipeac.mountains.prefs"
    private val constLogin = "loginUser"

    private val prefs: SharedPreferences = context.getSharedPreferences(constPrefsFilename, 0)

    var login: Boolean
        get() = prefs.getBoolean(constLogin, false)
        set(value) = prefs.edit().putBoolean(constLogin, value).apply()
}
