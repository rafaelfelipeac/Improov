package com.rafaelfelipeac.improov.features.splash.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rafaelfelipeac.improov.features.splash.domain.usecase.GetWelcomeUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class SplashViewModel @Inject constructor(
    private val getWelcomeUseCase: GetWelcomeUseCase
) : ViewModel() {

    val welcome: LiveData<Boolean> get() = _welcome
    private val _welcome = MutableLiveData<Boolean>()

    fun loadData() {
        getWelcome()
    }

    private fun getWelcome() {
        viewModelScope.launch {
            _welcome.postValue(getWelcomeUseCase())
        }
    }
}
