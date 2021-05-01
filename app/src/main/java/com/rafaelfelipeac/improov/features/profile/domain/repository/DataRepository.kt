package com.rafaelfelipeac.improov.features.profile.domain.repository

interface DataRepository {

    suspend fun generateData()

    suspend fun clearData()
}
