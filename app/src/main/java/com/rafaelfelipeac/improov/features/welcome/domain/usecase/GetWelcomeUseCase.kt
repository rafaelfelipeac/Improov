package com.rafaelfelipeac.improov.features.welcome.domain.usecase

import com.rafaelfelipeac.improov.features.welcome.domain.repository.WelcomeRepository
import javax.inject.Inject

class GetWelcomeUseCase @Inject constructor(
    private val welcomeRepository: WelcomeRepository
) {
    suspend fun execute(): Boolean {
        return welcomeRepository.getWelcome()
    }
}
