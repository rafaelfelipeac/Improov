package com.rafaelfelipeac.mountains.di.component

import com.rafaelfelipeac.mountains.di.module.NetworkModule
import com.rafaelfelipeac.mountains.ui.base.BaseViewModel
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
