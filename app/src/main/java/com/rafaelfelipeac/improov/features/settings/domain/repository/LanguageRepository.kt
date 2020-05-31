package com.rafaelfelipeac.improov.features.settings.domain.repository

interface LanguageRepository {

    suspend fun getLanguage(): String

    suspend fun save(language: String)
}
