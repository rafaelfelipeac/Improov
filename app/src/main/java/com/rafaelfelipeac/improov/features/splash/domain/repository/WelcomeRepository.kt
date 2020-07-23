package com.rafaelfelipeac.improov.features.splash.domain.repository

interface WelcomeRepository {

    suspend fun getWelcome(): Boolean
}
