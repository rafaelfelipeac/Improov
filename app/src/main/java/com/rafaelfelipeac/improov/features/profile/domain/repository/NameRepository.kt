package com.rafaelfelipeac.improov.features.profile.domain.repository

interface NameRepository {

    suspend fun getName(): String

    suspend fun save(name: String)
}
