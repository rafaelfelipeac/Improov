package com.rafaelfelipeac.improov.features.settings.presentation.settingslanguage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rafaelfelipeac.improov.features.settings.domain.usecase.GetLanguageUseCase
import com.rafaelfelipeac.improov.features.settings.domain.usecase.SaveLanguageUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import javax.inject.Inject

class SettingsLanguageViewModel @Inject constructor(
    private val saveLanguageUseCase: SaveLanguageUseCase,
    private val getLanguageUseCase: GetLanguageUseCase
) : ViewModel() {

    val saved: Flow<Unit> get() = _saved.filterNotNull()
    private val _saved = MutableStateFlow<Unit?>(null)
    val language: Flow<String> get() = _language.filterNotNull()
    private val _language = MutableStateFlow<String?>(null)

    fun loadData() {
        getLanguage()
    }

    fun saveLanguage(language: String) {
        if (language != _language.value) {
            viewModelScope.launch {
                _saved.value = saveLanguageUseCase(language)
            }
        }
    }

    private fun getLanguage() {
        viewModelScope.launch {
            _language.value = getLanguageUseCase()
        }
    }
}
