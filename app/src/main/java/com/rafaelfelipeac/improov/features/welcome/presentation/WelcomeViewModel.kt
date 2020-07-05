package com.rafaelfelipeac.improov.features.welcome.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.rafaelfelipeac.improov.core.platform.base.BaseViewModel
import com.rafaelfelipeac.improov.features.welcome.domain.usecase.GetWelcomeUseCase
import com.rafaelfelipeac.improov.features.welcome.domain.usecase.SaveWelcomeUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class WelcomeViewModel @Inject constructor(
    private val saveWelcomeUseCase: SaveWelcomeUseCase,
    private val getWelcomeUseCase: GetWelcomeUseCase
) : BaseViewModel() {

    val saved: LiveData<Unit> get() = _saved
    private val _saved = MutableLiveData<Unit>()
    val welcome: LiveData<Boolean> get() = _welcome
    private val _welcome = MutableLiveData<Boolean>()

    override fun loadData() {
        getWelcome()
    }

    fun saveWelcome(welcome: Boolean) {
        viewModelScope.launch {
            _saved.postValue(saveWelcomeUseCase(welcome))
        }
    }

    private fun getWelcome() {
        viewModelScope.launch {
            _welcome.postValue(getWelcomeUseCase())
        }
    }
}
