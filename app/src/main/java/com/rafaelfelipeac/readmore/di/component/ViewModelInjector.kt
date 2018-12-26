package com.rafaelfelipeac.readmore.di.component

import com.rafaelfelipeac.readmore.di.module.NetworkModule
import com.rafaelfelipeac.readmore.ui.base.BaseViewModel
import com.rafaelfelipeac.readmore.ui.fragments.home.HomeViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [(NetworkModule::class)])
interface ViewModelInjector {

    fun inject(homeViewModel: BaseViewModel)

    @Component.Builder
    interface Builder {
        fun build(): ViewModelInjector

        fun networkModule(networkModule: NetworkModule): Builder
    }
}
