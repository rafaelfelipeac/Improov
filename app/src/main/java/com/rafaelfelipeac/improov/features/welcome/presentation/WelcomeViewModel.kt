package com.rafaelfelipeac.improov.features.welcome.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.rafaelfelipeac.improov.core.platform.base.BaseViewModel
import com.rafaelfelipeac.improov.features.welcome.domain.usecase.SaveWelcomeUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class WelcomeViewModel @Inject constructor(
    private val saveWelcomeUseCase: SaveWelcomeUseCase
) : BaseViewModel() {

    val saved: LiveData<Unit> get() = _saved
    private val _saved = MutableLiveData<Unit>()

    fun saveWelcome(welcome: Boolean) {
        viewModelScope.launch {
            _saved.postValue(saveWelcomeUseCase(welcome))
        }
    }
}
