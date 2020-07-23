package com.rafaelfelipeac.improov.features.welcome.domain.repository

interface WelcomeRepository {

    suspend fun save(welcome: Boolean)
}
