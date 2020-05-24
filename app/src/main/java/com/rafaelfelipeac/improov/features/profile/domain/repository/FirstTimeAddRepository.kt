package com.rafaelfelipeac.improov.features.profile.domain.repository

interface FirstTimeAddRepository {

    suspend fun getFirstTimeAdd(): Boolean

    suspend fun save(firstTimeAdd: Boolean)
}
