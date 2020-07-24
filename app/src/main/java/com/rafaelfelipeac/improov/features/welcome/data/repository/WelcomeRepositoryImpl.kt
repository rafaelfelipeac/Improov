package com.rafaelfelipeac.improov.features.welcome.data.repository

import com.rafaelfelipeac.improov.core.persistence.sharedpreferences.Preferences
import com.rafaelfelipeac.improov.features.welcome.domain.repository.WelcomeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WelcomeRepositoryImpl @Inject constructor(
    private val preferences: Preferences
) : WelcomeRepository {

    override suspend fun save(welcome: Boolean) {
        return withContext(Dispatchers.IO) {
            preferences.welcome = welcome
        }
    }
}
