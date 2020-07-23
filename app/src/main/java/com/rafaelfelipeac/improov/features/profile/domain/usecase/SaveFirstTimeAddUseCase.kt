package com.rafaelfelipeac.improov.features.profile.domain.usecase

import com.rafaelfelipeac.improov.features.profile.domain.repository.FirstTimeAddRepository
import javax.inject.Inject

class SaveFirstTimeAddUseCase @Inject constructor(
    private val firstTimeAddRepository: FirstTimeAddRepository
) {
    suspend operator fun invoke(saveFirstAdd: Boolean) {
        return firstTimeAddRepository.save(saveFirstAdd)
    }
}
