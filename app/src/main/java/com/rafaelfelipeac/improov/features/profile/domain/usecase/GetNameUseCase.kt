package com.rafaelfelipeac.improov.features.profile.domain.usecase

import com.rafaelfelipeac.improov.features.profile.domain.repository.NameRepository
import javax.inject.Inject

class GetNameUseCase @Inject constructor(
    private val nameRepository: NameRepository
) {
    suspend operator fun invoke(): String {
        return nameRepository.getName()
    }
}
