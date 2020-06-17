package com.rafaelfelipeac.improov.features.profile.domain.repository

interface WelcomeRepository {

    suspend fun getWelcome(): Boolean

    suspend fun save(welcome: Boolean)
}
