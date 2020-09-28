package com.rafaelfelipeac.improov.features.backup.domain.usecase

import com.rafaelfelipeac.improov.features.backup.domain.repository.DatabaseRepository
import javax.inject.Inject

class GetImportDateUseCase @Inject constructor(
    private val databaseRepository: DatabaseRepository
) {
    suspend operator fun invoke(): Long {
        return databaseRepository.getImportDate()
    }
}
