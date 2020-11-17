package com.rafaelfelipeac.improov.features.splash.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rafaelfelipeac.improov.features.splash.domain.usecase.GetWelcomeUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import javax.inject.Inject

class SplashViewModel @Inject constructor(
    private val getWelcomeUseCase: GetWelcomeUseCase
) : ViewModel() {

    val welcome: Flow<Boolean> get() = _welcome.filterNotNull()
    private val _welcome = MutableStateFlow<Boolean?>(null)

    fun loadData() {
        getWelcome()
    }

    private fun getWelcome() {
        viewModelScope.launch {
            _welcome.value = getWelcomeUseCase()
        }
    }
}
