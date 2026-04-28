package com.rafaelfelipeac.improov.core.di.modules

import androidx.lifecycle.ViewModel
import com.rafaelfelipeac.improov.core.di.key.ViewModelKey
import com.rafaelfelipeac.improov.features.splash.data.WelcomeDataSource
import com.rafaelfelipeac.improov.features.splash.domain.repository.WelcomeRepository
import com.rafaelfelipeac.improov.features.splash.presentation.SplashViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class SplashModule {

    @Binds
    abstract fun welcomeRepository(welcomeDataSource: WelcomeDataSource): WelcomeRepository

    @Binds
    @IntoMap
    @ViewModelKey(SplashViewModel::class)
    abstract fun bindSplashViewModel(splashViewModel: SplashViewModel): ViewModel
}
