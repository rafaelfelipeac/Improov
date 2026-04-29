package com.rafaelfelipeac.improov.features.settings.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rafaelfelipeac.improov.core.ui.theme.AppThemeMode
import com.rafaelfelipeac.improov.features.settings.domain.usecase.GetThemeModeUseCase
import com.rafaelfelipeac.improov.features.settings.domain.usecase.SaveThemeModeUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

class SettingsViewModel @Inject constructor(
    private val getThemeModeUseCase: GetThemeModeUseCase,
    private val saveThemeModeUseCase: SaveThemeModeUseCase,
) : ViewModel() {

    val themeMode: Flow<AppThemeMode> get() = getThemeModeUseCase()

    fun saveThemeMode(themeMode: AppThemeMode) {
        viewModelScope.launch {
            saveThemeModeUseCase(themeMode)
        }
    }
}
