package com.rafaelfelipeac.improov.features.goal.domain.repository

interface FirstTimeAddRepository {

    suspend fun getFirstTimeAdd(): Boolean

    suspend fun save(firstTimeAdd: Boolean)
}
