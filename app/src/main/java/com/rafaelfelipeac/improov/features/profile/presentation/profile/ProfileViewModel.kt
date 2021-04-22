package com.rafaelfelipeac.improov.features.profile.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rafaelfelipeac.improov.features.profile.domain.usecase.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val saveWelcomeUseCase: SaveWelcomeUseCase,
    private val saveFirstTimeAddUseCase: SaveFirstTimeAddUseCase,
    private val saveFirstTimeListUseCase: SaveFirstTimeListUseCase,
    private val getNameUseCase: GetNameUseCase,
    private val generateDataUseCase: GenerateDataUseCase,
    private val clearDataUseCase: ClearDataUseCase

) : ViewModel() {

    val saved: Flow<Unit> get() = _saved.filterNotNull()
    private val _saved = MutableStateFlow<Unit?>(null)
    val generated: Flow<Unit> get() = _generated.filterNotNull()
    private val _generated = MutableStateFlow<Unit?>(null)
    val clean: Flow<Unit> get() = _clean.filterNotNull()
    private val _clean = MutableStateFlow<Unit?>(null)
    val name: Flow<String> get() = _name.filterNotNull()
    private val _name = MutableStateFlow<String?>(null)

    fun loadData() {
        getName()
    }

    fun saveWelcome(welcome: Boolean) {
        viewModelScope.launch {
            _saved.value = saveWelcomeUseCase(welcome)
        }
    }

    fun saveFirstTimeAdd(saveFirstTimeAdd: Boolean) {
        viewModelScope.launch {
            _saved.value = (saveFirstTimeAddUseCase(saveFirstTimeAdd))
        }
    }

    fun saveFirstTimeList(saveFirstTimeList: Boolean) {
        viewModelScope.launch {
            _saved.value = saveFirstTimeListUseCase(saveFirstTimeList)
        }
    }

    private fun getName() {
        viewModelScope.launch {
            _name.value = getNameUseCase()
        }
    }

    fun generateData() {
        viewModelScope.launch {
            _generated.value = generateDataUseCase()
        }
    }

    fun clearData() {
        viewModelScope.launch {
            _clean.value = clearDataUseCase()
        }
    }
}
