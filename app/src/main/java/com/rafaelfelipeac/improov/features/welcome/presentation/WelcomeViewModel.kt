package com.rafaelfelipeac.improov.features.welcome.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rafaelfelipeac.improov.features.welcome.domain.usecase.SaveWelcomeUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import javax.inject.Inject

class WelcomeViewModel @Inject constructor(
    private val saveWelcomeUseCase: SaveWelcomeUseCase
) : ViewModel() {

    val saved: Flow<Unit> get() = _saved.filterNotNull()
    private val _saved = MutableStateFlow<Unit?>(null)

    fun saveWelcome(welcome: Boolean) {
        viewModelScope.launch {
            _saved.value = saveWelcomeUseCase(welcome)
        }
    }
}
