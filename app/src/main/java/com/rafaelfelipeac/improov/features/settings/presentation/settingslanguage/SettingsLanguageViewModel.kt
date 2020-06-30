package com.rafaelfelipeac.improov.features.settings.presentation.settingslanguage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.rafaelfelipeac.improov.core.platform.base.NewBaseViewModel
import com.rafaelfelipeac.improov.features.settings.domain.usecase.GetLanguageUseCase
import com.rafaelfelipeac.improov.features.settings.domain.usecase.SaveLanguageUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class SettingsLanguageViewModel @Inject constructor(
    private val saveLanguageUseCase: SaveLanguageUseCase,
    private val getLanguageUseCase: GetLanguageUseCase
) : NewBaseViewModel() {

    val language: LiveData<String> get() = _language
    private val _language = MutableLiveData<String>()
    val saved: LiveData<Unit> get() = _saved
    private val _saved = MutableLiveData<Unit>()

    override fun loadData() {
        getLanguage()
    }

    private fun getLanguage() {
        viewModelScope.launch {
            getLanguageUseCase.execute().also {
                _language.postValue(it)
            }
        }
    }

    fun saveLanguage(language: String) {
        viewModelScope.launch {
            saveLanguageUseCase.execute(language).also {
                _saved.postValue(Unit)
            }
        }
    }
}
