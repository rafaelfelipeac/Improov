package com.rafaelfelipeac.improov.features.settings.presentation.settingslanguage

import androidx.lifecycle.viewModelScope
import com.rafaelfelipeac.improov.core.platform.base.BaseAction
import com.rafaelfelipeac.improov.core.platform.base.BaseViewModel
import com.rafaelfelipeac.improov.core.platform.base.BaseViewState
import com.rafaelfelipeac.improov.features.settings.domain.usecase.GetLanguageUseCase
import com.rafaelfelipeac.improov.features.settings.domain.usecase.SaveLanguageUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class SettingsLanguageViewModel @Inject constructor(
    private val saveLanguageUseCase: SaveLanguageUseCase,
    private val getLanguageUseCase: GetLanguageUseCase
) : BaseViewModel<SettingsLanguageViewModel.ViewState, SettingsLanguageViewModel.Action>(
    ViewState()
) {

    override fun onLoadData() {
        getLanguage()
    }

    fun onSaveLanguage(language: String) {
        saveLanguage(language)
    }

    private fun getLanguage() {
        viewModelScope.launch {
            getLanguageUseCase.execute().also {
                sendAction(Action.LanguageLoaded(it))
            }
        }
    }

    private fun saveLanguage(language: String) {
        viewModelScope.launch {
            saveLanguageUseCase.execute(language).also {
                sendAction(Action.LanguageSaved)
            }
        }
    }

    override fun onReduceState(viewAction: Action) = when (viewAction) {
        is Action.LanguageLoaded -> state.copy(
            language = viewAction.language
        )
        is Action.LanguageSaved -> state.copy(
            languageSaved = true
        )
    }

    data class ViewState(
        val language: String = "",
        val languageSaved: Boolean = false
    ) : BaseViewState

    sealed class Action : BaseAction {
        class LanguageLoaded(val language: String) : Action()
        object LanguageSaved : Action()
    }
}
