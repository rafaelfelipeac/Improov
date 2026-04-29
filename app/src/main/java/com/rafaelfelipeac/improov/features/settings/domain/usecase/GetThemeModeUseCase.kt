package com.rafaelfelipeac.improov.features.settings.domain.usecase

import com.rafaelfelipeac.improov.core.ui.theme.AppThemeMode
import com.rafaelfelipeac.improov.features.settings.domain.repository.ThemeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetThemeModeUseCase @Inject constructor(
    private val themeRepository: ThemeRepository,
) {
    operator fun invoke(): Flow<AppThemeMode> {
        return themeRepository.observeThemeMode()
    }
}
