package com.rafaelfelipeac.improov.core.di.modules

import androidx.lifecycle.ViewModel
import com.rafaelfelipeac.improov.core.di.key.ViewModelKey
import com.rafaelfelipeac.improov.features.settings.data.LanguageDataSource
import com.rafaelfelipeac.improov.features.settings.data.ThemeDataSource
import com.rafaelfelipeac.improov.features.settings.domain.repository.LanguageRepository
import com.rafaelfelipeac.improov.features.settings.domain.repository.ThemeRepository
import com.rafaelfelipeac.improov.features.settings.presentation.settings.SettingsViewModel
import com.rafaelfelipeac.improov.features.settings.presentation.settingslanguage.SettingsLanguageViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class SettingsModule {

    @Binds
    abstract fun languageRepository(languageDataSource: LanguageDataSource): LanguageRepository

    @Binds
    abstract fun themeRepository(themeDataSource: ThemeDataSource): ThemeRepository

    @Binds
    @IntoMap
    @ViewModelKey(SettingsViewModel::class)
    abstract fun bindSettingsViewModel(settingsViewModel: SettingsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SettingsLanguageViewModel::class)
    abstract fun bindSettingsLanguageViewModel(settingsLanguageViewModel: SettingsLanguageViewModel): ViewModel
}
