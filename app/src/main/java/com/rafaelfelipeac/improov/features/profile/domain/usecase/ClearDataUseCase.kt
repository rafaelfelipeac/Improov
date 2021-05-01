package com.rafaelfelipeac.improov.features.profile.domain.usecase

import com.rafaelfelipeac.improov.features.profile.domain.repository.DataRepository
import javax.inject.Inject

class ClearDataUseCase @Inject constructor(
    private val dataRepository: DataRepository
) {
    suspend operator fun invoke() {
        return dataRepository.clearData()
    }
}
