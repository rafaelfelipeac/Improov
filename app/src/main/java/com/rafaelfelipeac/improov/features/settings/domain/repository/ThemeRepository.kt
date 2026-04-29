package com.rafaelfelipeac.improov.features.settings.domain.repository

import com.rafaelfelipeac.improov.core.ui.theme.AppThemeMode
import kotlinx.coroutines.flow.Flow

interface ThemeRepository {

    fun observeThemeMode(): Flow<AppThemeMode>

    suspend fun save(themeMode: AppThemeMode)
}
