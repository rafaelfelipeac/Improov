package com.rafaelfelipeac.improov.core.di.modules

import androidx.lifecycle.ViewModel
import com.rafaelfelipeac.improov.core.di.modules.viewModel.ViewModelKey
import com.rafaelfelipeac.improov.features.welcome.WelcomeViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class WelcomeModule {

    @Binds
    @IntoMap
    @ViewModelKey(WelcomeViewModel::class)
    abstract fun bindWelcomeViewModel(welcomeViewModel: WelcomeViewModel): ViewModel
}
