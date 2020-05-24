package com.rafaelfelipeac.improov.features.profile.domain.usecase

import com.rafaelfelipeac.improov.features.profile.domain.repository.NameRepository
import javax.inject.Inject

class SaveNameUseCase @Inject constructor(
    private val nameRepository: NameRepository
) {
    suspend fun execute(name: String) {
        return nameRepository.save(name)
    }
}
