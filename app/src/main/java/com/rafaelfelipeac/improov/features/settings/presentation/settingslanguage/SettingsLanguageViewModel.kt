package com.rafaelfelipeac.improov.features.settings.presentation.settingslanguage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rafaelfelipeac.improov.features.settings.domain.usecase.GetLanguageUseCase
import com.rafaelfelipeac.improov.features.settings.domain.usecase.SaveLanguageUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class SettingsLanguageViewModel @Inject constructor(
    private val saveLanguageUseCase: SaveLanguageUseCase,
    private val getLanguageUseCase: GetLanguageUseCase
) : ViewModel() {

    val saved: LiveData<Unit> get() = _saved
    private val _saved = MutableLiveData<Unit>()
    val language: LiveData<String> get() = _language
    private val _language = MutableLiveData<String>()

    fun loadData() {
        getLanguage()
    }

    fun saveLanguage(language: String) {
        viewModelScope.launch {
            _saved.postValue(saveLanguageUseCase(language))
        }
    }

    private fun getLanguage() {
        viewModelScope.launch {
            _language.postValue(getLanguageUseCase())
        }
    }
}
