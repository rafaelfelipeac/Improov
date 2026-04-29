package com.rafaelfelipeac.improov.features.settings.domain.usecase

import com.rafaelfelipeac.improov.core.ui.theme.AppThemeMode
import com.rafaelfelipeac.improov.features.settings.domain.repository.ThemeRepository
import javax.inject.Inject

class SaveThemeModeUseCase @Inject constructor(
    private val themeRepository: ThemeRepository,
) {
    suspend operator fun invoke(themeMode: AppThemeMode) {
        themeRepository.save(themeMode)
    }
}
