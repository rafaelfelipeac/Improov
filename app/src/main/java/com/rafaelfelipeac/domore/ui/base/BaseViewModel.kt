package com.rafaelfelipeac.domore.ui.base

import androidx.lifecycle.ViewModel
import com.rafaelfelipeac.domore.di.component.DaggerViewModelInjector
import com.rafaelfelipeac.domore.di.component.ViewModelInjector
import com.rafaelfelipeac.domore.di.module.NetworkModule

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