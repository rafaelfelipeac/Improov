package com.rafaelfelipeac.improov.features.settings.domain.usecase

import com.rafaelfelipeac.improov.features.settings.domain.repository.LanguageRepository
import javax.inject.Inject

class GetLanguageUseCase @Inject constructor(
    private val languageRepository: LanguageRepository
) {
    suspend fun execute() : String {
        return languageRepository.getLanguage()
    }
}
