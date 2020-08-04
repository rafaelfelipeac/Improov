package com.rafaelfelipeac.improov.core.di.modules

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.rafaelfelipeac.improov.core.di.key.FragmentKey
import com.rafaelfelipeac.improov.core.di.key.ViewModelKey
import com.rafaelfelipeac.improov.features.settings.data.repository.LanguageRepositoryImpl
import com.rafaelfelipeac.improov.features.settings.domain.repository.LanguageRepository
import com.rafaelfelipeac.improov.features.settings.presentation.settings.SettingsFragment
import com.rafaelfelipeac.improov.features.settings.presentation.settings.SettingsViewModel
import com.rafaelfelipeac.improov.features.settings.presentation.settingslanguage.SettingsLanguageFragment
import com.rafaelfelipeac.improov.features.settings.presentation.settingslanguage.SettingsLanguageViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class SettingsModule {

    @Binds
    abstract fun languageRepository(languageRepositoryImpl: LanguageRepositoryImpl): LanguageRepository

    @Binds
    @IntoMap
    @FragmentKey(SettingsFragment::class)
    abstract fun bindSettingsFragment(settingsFragment: SettingsFragment): Fragment

    @Binds
    @IntoMap
    @ViewModelKey(SettingsViewModel::class)
    abstract fun bindSettingsViewModel(settingsViewModel: SettingsViewModel): ViewModel

    @Binds
    @IntoMap
    @FragmentKey(SettingsLanguageFragment::class)
    abstract fun bindSettingsLanguageFragment(settingsLanguageFragment: SettingsLanguageFragment): Fragment

    @Binds
    @IntoMap
    @ViewModelKey(SettingsLanguageViewModel::class)
    abstract fun bindSettingsLanguageViewModel(settingsLanguageViewModel: SettingsLanguageViewModel): ViewModel
}
