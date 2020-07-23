package com.rafaelfelipeac.improov.features.settings.domain.usecase

import com.rafaelfelipeac.improov.features.settings.domain.repository.LanguageRepository
import javax.inject.Inject

class SaveLanguageUseCase @Inject constructor(
    private val languageRepository: LanguageRepository
) {
    suspend operator fun invoke(language: String) {
        return languageRepository.save(language)
    }
}
