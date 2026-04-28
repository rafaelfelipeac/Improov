package com.rafaelfelipeac.improov.core.di.modules

import androidx.lifecycle.ViewModel
import com.rafaelfelipeac.improov.core.di.key.ViewModelKey
import com.rafaelfelipeac.improov.features.welcome.data.WelcomeDataSource
import com.rafaelfelipeac.improov.features.welcome.domain.repository.WelcomeRepository
import com.rafaelfelipeac.improov.features.welcome.presentation.WelcomeViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class WelcomeModule {

    @Binds
    abstract fun welcomeRepository(welcomeDataSource: WelcomeDataSource): WelcomeRepository

    @Binds
    @IntoMap
    @ViewModelKey(WelcomeViewModel::class)
    abstract fun bindWelcomeViewModel(welcomeViewModel: WelcomeViewModel): ViewModel
}
