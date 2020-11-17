package com.rafaelfelipeac.improov.features.profile.data

import com.rafaelfelipeac.improov.core.persistence.sharedpreferences.Preferences
import com.rafaelfelipeac.improov.features.profile.domain.repository.FirstTimeAddRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FirstTimeAddDataSource @Inject constructor(
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
