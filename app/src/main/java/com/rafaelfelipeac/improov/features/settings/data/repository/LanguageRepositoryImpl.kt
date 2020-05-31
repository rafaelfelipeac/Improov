package com.rafaelfelipeac.improov.features.settings.data.repository

import android.content.Context
import com.rafaelfelipeac.improov.core.persistence.sharedpreferences.Preferences
import com.rafaelfelipeac.improov.features.settings.domain.repository.LanguageRepository
import com.rafaelfelipeac.improov.features.settings.presentation.LocaleHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LanguageRepositoryImpl @Inject constructor(
    private val preferences: Preferences,
    private val context: Context
) : LanguageRepository {

    override suspend fun getLanguage(): String {
        return withContext(Dispatchers.IO) {
            preferences.language
        }
    }

    override suspend fun save(language: String) {
        withContext(Dispatchers.IO) {
            LocaleHelper.setLocale(
                context,
                language
            )
        }
    }
}
