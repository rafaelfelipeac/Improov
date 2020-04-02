package com.rafaelfelipeac.improov.core.di.modules

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.rafaelfelipeac.improov.core.di.modules.viewModel.ViewModelKey
import com.rafaelfelipeac.improov.features.welcome.presentation.WelcomeFragment
import com.rafaelfelipeac.improov.features.welcome.presentation.WelcomeItemFragment
import com.rafaelfelipeac.improov.features.welcome.presentation.WelcomeViewModel
import dagger.Binds
import dagger.Module
import dagger.android.support.FragmentKey
import dagger.multibindings.IntoMap

@Module
abstract class WelcomeModule {

    @Binds
    @IntoMap
    @FragmentKey(WelcomeFragment::class)
    abstract fun bindWelcomeFragment(welcomeFragment: WelcomeFragment): Fragment

    @Binds
    @IntoMap
    @ViewModelKey(WelcomeViewModel::class)
    abstract fun bindWelcomeViewModel(welcomeViewModel: WelcomeViewModel): ViewModel

    @Binds
    @IntoMap
    @FragmentKey(WelcomeItemFragment::class)
    abstract fun bindWelcomeItemFragment(welcomeItemFragment: WelcomeItemFragment): Fragment
}
