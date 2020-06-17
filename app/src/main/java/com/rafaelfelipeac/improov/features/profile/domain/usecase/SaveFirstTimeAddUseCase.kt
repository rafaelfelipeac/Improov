package com.rafaelfelipeac.improov.features.profile.domain.usecase

import com.rafaelfelipeac.improov.features.profile.domain.repository.FirstTimeAddRepository
import javax.inject.Inject

class SaveFirstTimeAddUseCase @Inject constructor(
    private val firstTimeAddRepository: FirstTimeAddRepository
) {
    suspend fun execute(saveFirstAdd: Boolean) {
        return firstTimeAddRepository.save(saveFirstAdd)
    }
}
