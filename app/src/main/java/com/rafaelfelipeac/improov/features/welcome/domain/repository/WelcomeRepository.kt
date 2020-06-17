package com.rafaelfelipeac.improov.features.welcome.domain.repository

interface WelcomeRepository {

    suspend fun getWelcome(): Boolean

    suspend fun save(welcome: Boolean)
}
