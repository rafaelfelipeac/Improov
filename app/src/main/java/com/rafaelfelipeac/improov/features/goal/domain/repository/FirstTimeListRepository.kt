package com.rafaelfelipeac.improov.features.goal.domain.repository

interface FirstTimeListRepository {

    suspend fun getFirstTimeList(): Boolean

    suspend fun save(firstTimeList: Boolean)
}
