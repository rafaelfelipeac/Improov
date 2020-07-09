package com.rafaelfelipeac.improov.core.di.modules

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.rafaelfelipeac.improov.core.di.modules.viewModel.FragmentKey
import com.rafaelfelipeac.improov.core.di.modules.viewModel.ViewModelKey
import com.rafaelfelipeac.improov.features.splash.data.repository.WelcomeRepositoryImpl
import com.rafaelfelipeac.improov.features.splash.domain.repository.WelcomeRepository
import com.rafaelfelipeac.improov.features.splash.presentation.SplashFragment
import com.rafaelfelipeac.improov.features.splash.presentation.SplashViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class SplashModule {

    @Binds
    abstract fun welcomeRepository(welcomeRepositoryImpl: WelcomeRepositoryImpl): WelcomeRepository

    @Binds
    @IntoMap
    @FragmentKey(SplashFragment::class)
    abstract fun bindSplashFragment(splashFragment: SplashFragment): Fragment

    @Binds
    @IntoMap
    @ViewModelKey(SplashViewModel::class)
    abstract fun bindSplashViewModel(splashViewModel: SplashViewModel): ViewModel
}
