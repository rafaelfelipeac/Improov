package com.rafaelfelipeac.improov.features.backup.domain.repository

interface DatabaseRepository {

    suspend fun export(): String

    suspend fun import(databaseBackup: String): Boolean
}
