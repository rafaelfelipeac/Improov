package com.rafaelfelipeac.improov.features.splash.data

import com.rafaelfelipeac.improov.core.persistence.sharedpreferences.Preferences
import com.rafaelfelipeac.improov.features.splash.domain.repository.WelcomeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WelcomeDataSource @Inject constructor(
    private val preferences: Preferences
) : WelcomeRepository {

    override suspend fun getWelcome(): Boolean {
        return withContext(Dispatchers.IO) {
            preferences.welcome
        }
    }
}
