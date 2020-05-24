package com.rafaelfelipeac.improov.features.profile.data.respository

import com.rafaelfelipeac.improov.core.persistence.sharedpreferences.Preferences
import com.rafaelfelipeac.improov.features.profile.domain.repository.FirstTimeListRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FirstTimeListRepositoryImpl @Inject constructor(
    private val preferences: Preferences
) : FirstTimeListRepository {

    override suspend fun getFirstTimeList(): Boolean {
        return withContext(Dispatchers.IO) {
            preferences.fistTimeList
        }
    }

    override suspend fun save(firstTimeList: Boolean) {
        return withContext(Dispatchers.IO) {
            preferences.fistTimeList = firstTimeList
        }
    }
}
