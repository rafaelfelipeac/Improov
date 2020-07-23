package com.rafaelfelipeac.improov.features.splash.domain.usecase

import com.rafaelfelipeac.improov.features.splash.domain.repository.WelcomeRepository
import javax.inject.Inject

class GetWelcomeUseCase @Inject constructor(
    private val welcomeRepository: WelcomeRepository
) {
    suspend operator fun invoke(): Boolean {
        return welcomeRepository.getWelcome()
    }
}
