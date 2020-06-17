package com.rafaelfelipeac.improov.features.profile.domain.repository

interface FirstTimeListRepository {

    suspend fun getFirstTimeList(): Boolean

    suspend fun save(firstTimeList: Boolean)
}
