package com.rafaelfelipeac.improov.features.settings.data

import com.rafaelfelipeac.improov.core.persistence.sharedpreferences.Preferences
import com.rafaelfelipeac.improov.features.settings.domain.repository.LanguageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LanguageDataSource @Inject constructor(
    private val preferences: Preferences
) : LanguageRepository {

    override suspend fun getLanguage(): String {
        return withContext(Dispatchers.IO) {
            preferences.language
        }
    }

    override suspend fun save(language: String) {
        withContext(Dispatchers.IO) {
            preferences.language = language
        }
    }
}
