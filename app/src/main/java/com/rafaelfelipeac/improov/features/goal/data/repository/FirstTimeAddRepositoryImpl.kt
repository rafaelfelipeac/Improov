package com.rafaelfelipeac.improov.features.goal.data.repository

import com.rafaelfelipeac.improov.core.persistence.sharedpreferences.Preferences
import com.rafaelfelipeac.improov.features.goal.domain.repository.FirstTimeAddRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FirstTimeAddRepositoryImpl @Inject constructor(
    private val preferences: Preferences
) : FirstTimeAddRepository {

    override suspend fun getFirstTimeAdd(): Boolean {
        return withContext(Dispatchers.IO) {
            preferences.firstTimeAdd
        }
    }

    override suspend fun save(firstTimeAdd: Boolean) {
        return withContext(Dispatchers.IO) {
            preferences.firstTimeAdd = firstTimeAdd
        }
    }
}
