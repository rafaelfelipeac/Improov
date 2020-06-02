package com.rafaelfelipeac.improov.features.goal.data.repository

import com.rafaelfelipeac.improov.core.persistence.sharedpreferences.Preferences
import com.rafaelfelipeac.improov.features.goal.domain.repository.FirstTimeListRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FirstTimeListRepositoryImpl @Inject constructor(
    private val preferences: Preferences
) : FirstTimeListRepository {

    override suspend fun getFirstTimeList(): Boolean {
        return withContext(Dispatchers.IO) {
            preferences.firstTimeList
        }
    }

    override suspend fun save(firstTimeList: Boolean) {
        return withContext(Dispatchers.IO) {
            preferences.firstTimeList = firstTimeList
        }
    }
}
