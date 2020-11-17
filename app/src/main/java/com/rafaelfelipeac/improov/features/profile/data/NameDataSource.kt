package com.rafaelfelipeac.improov.features.profile.data

import com.rafaelfelipeac.improov.core.persistence.sharedpreferences.Preferences
import com.rafaelfelipeac.improov.features.profile.domain.repository.NameRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NameDataSource @Inject constructor(
    private val preferences: Preferences
) : NameRepository {

    override suspend fun getName(): String {
        return withContext(Dispatchers.IO) {
            preferences.name
        }
    }

    override suspend fun save(name: String) {
        return withContext(Dispatchers.IO) {
            preferences.name = name
        }
    }
}
