package com.rafaelfelipeac.improov.features.settings.data

import com.rafaelfelipeac.improov.core.persistence.sharedpreferences.Preferences
import com.rafaelfelipeac.improov.core.ui.theme.AppThemeMode
import com.rafaelfelipeac.improov.features.settings.domain.repository.ThemeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ThemeDataSource @Inject constructor(
    private val preferences: Preferences,
) : ThemeRepository {

    override fun observeThemeMode(): Flow<AppThemeMode> {
        return preferences.observeThemeMode().map(AppThemeMode::from)
    }

    override suspend fun save(themeMode: AppThemeMode) {
        withContext(Dispatchers.IO) {
            preferences.themeMode = themeMode.name
        }
    }
}
