package com.rafaelfelipeac.improov.features.profile.presentation.profileedit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rafaelfelipeac.improov.features.profile.domain.usecase.GetNameUseCase
import com.rafaelfelipeac.improov.features.profile.domain.usecase.SaveNameUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProfileEditViewModel @Inject constructor(
    private val saveNameUseCase: SaveNameUseCase,
    private val getNameUseCase: GetNameUseCase
) : ViewModel() {

    val saved: Flow<Unit> get() = _saved.filterNotNull()
    private val _saved = MutableStateFlow<Unit?>(null)
    val name: Flow<String> get() = _name.filterNotNull()
    private val _name = MutableStateFlow<String?>(null)

    fun loadData() {
        getName()
    }

    fun saveName(name: String) {
        viewModelScope.launch {
            _saved.value = saveNameUseCase(name)
        }
    }

    private fun getName() {
        viewModelScope.launch {
            _name.value = getNameUseCase()
        }
    }
}
