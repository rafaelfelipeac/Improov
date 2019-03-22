package com.rafaelfelipeac.mountains.ui.base

import androidx.lifecycle.ViewModel
import com.rafaelfelipeac.mountains.di.component.DaggerViewModelInjector
import com.rafaelfelipeac.mountains.di.component.ViewModelInjector
import com.rafaelfelipeac.mountains.di.module.NetworkModule

abstract class BaseViewModel : ViewModel() {

    private val injector: ViewModelInjector = DaggerViewModelInjector
        .builder()
        .networkModule(NetworkModule)
        .build()

    init {
        inject()
    }

    private fun inject() {
        injector.inject(this)
    }
}