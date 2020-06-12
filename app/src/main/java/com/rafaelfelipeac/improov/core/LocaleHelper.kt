package com.rafaelfelipeac.improov.core

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import com.rafaelfelipeac.improov.R
import com.rafaelfelipeac.improov.core.persistence.sharedpreferences.Preferences
import java.util.Locale

object LocaleHelper {

    fun onAttach(context: Context): Context {
        val preferences = Preferences(context)

        val lang = preferences.language

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
        var contextFun = context

        val locale =
            if (language == context.getString(R.string.settings_language_key_portuguese))
                Locale("pt", "BR")
            else
                Locale(language)
        Locale.setDefault(locale)

        val resources = context.resources
        val configuration = Configuration(resources.configuration)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(locale)
            contextFun = context.createConfigurationContext(configuration)
        } else {
            configuration.locale = locale
            resources.updateConfiguration(configuration, resources.displayMetrics)
        }

        return contextFun
    }
}
