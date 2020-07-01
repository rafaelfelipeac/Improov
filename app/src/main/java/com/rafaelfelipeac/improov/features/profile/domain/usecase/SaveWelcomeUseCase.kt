package com.rafaelfelipeac.improov.features.profile.domain.usecase

import com.rafaelfelipeac.improov.features.profile.domain.repository.WelcomeRepository
import javax.inject.Inject

class SaveWelcomeUseCase @Inject constructor(
    private val welcomeRepository: WelcomeRepository
) {
    suspend operator fun invoke(welcome: Boolean) {
        return welcomeRepository.save(welcome)
    }
}
