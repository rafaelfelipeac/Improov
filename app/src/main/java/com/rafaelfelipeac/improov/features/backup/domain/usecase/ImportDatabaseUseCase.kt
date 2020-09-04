package com.rafaelfelipeac.improov.features.backup.domain.usecase

import com.rafaelfelipeac.improov.features.backup.domain.repository.DatabaseRepository
import javax.inject.Inject

class ImportDatabaseUseCase @Inject constructor(
    private val databaseRepository: DatabaseRepository
) {
    suspend operator fun invoke(databaseBackup: String): Boolean {
        return databaseRepository.import(databaseBackup)
    }
}
