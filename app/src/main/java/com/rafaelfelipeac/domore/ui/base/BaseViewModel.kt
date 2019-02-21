package com.rafaelfelipeac.domore.ui.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.view.View
import com.rafaelfelipeac.domore.di.component.DaggerViewModelInjector
import com.rafaelfelipeac.domore.di.component.ViewModelInjector
import com.rafaelfelipeac.domore.di.module.NetworkModule

abstract class BaseViewModel : ViewModel() {

    private val result: MutableLiveData<Result> = MutableLiveData()

    var loadingVisibility: MutableLiveData<Int> = MutableLiveData()

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

    fun onStart() {
        loadingVisibility.value = View.VISIBLE
    }

    fun onFinish() {
        loadingVisibility.value = View.GONE
    }

    fun onSuccess(successMessage: String = "") {
        val resultSuccess = Result("success", successMessage)
        result.value = resultSuccess
    }

    fun onError(errorMessage: String) {
        val resultError = Result("error", errorMessage)
        result.value = resultError
    }
}

data class Result(var state: String, var message: String) : MutableLiveData<Result>()
