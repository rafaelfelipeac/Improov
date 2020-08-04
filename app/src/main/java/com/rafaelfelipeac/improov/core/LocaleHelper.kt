package com.rafaelfelipeac.improov.core

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import com.rafaelfelipeac.improov.R
import com.rafaelfelipeac.improov.core.persistence.sharedpreferences.Preferences
import java.util.Locale

object LocaleHelper {

    fun onAttach(context: Context): Context {
        val preferences = Preferences(context)

        val lang: String = preferences.language

        return setLocale(
            context,
            lang
        )
    }

    fun setLocale(context: Context, language: String?): Context {
        return updateResources(
            context,
            language!!
        )
    }

    @SuppressLint("ObsoleteSdkInt")
    fun updateResources(context: Context, language: String): Context {
        val contextFun: Context

        val locale =
            if (language == context.getString(R.string.settings_language_key_portuguese)) {
                Locale("pt", "BR")
            } else {
                Locale(language)
            }
        Locale.setDefault(locale)

        val configuration = Configuration(context.resources.configuration)

        configuration.setLocale(locale)
        contextFun = context.createConfigurationContext(configuration)

        return contextFun
    }
}
