package com.rafaelfelipeac.improov.features.profile.data.respository

import com.rafaelfelipeac.improov.core.persistence.sharedpreferences.Preferences
import com.rafaelfelipeac.improov.features.profile.domain.repository.WelcomeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WelcomeRepositoryImpl @Inject constructor(
    private val preferences: Preferences
) : WelcomeRepository {

    override suspend fun getWelcome(): Boolean {
        return withContext(Dispatchers.IO) {
            preferences.welcome
        }
    }

    override suspend fun save(welcome: Boolean) {
        return withContext(Dispatchers.IO) {
            preferences.welcome = welcome
        }
    }
}
