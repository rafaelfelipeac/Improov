package com.rafaelfelipeac.improov.features.welcome.presentation

import androidx.lifecycle.viewModelScope
import com.rafaelfelipeac.improov.core.platform.base.BaseAction
import com.rafaelfelipeac.improov.core.platform.base.BaseViewModel
import com.rafaelfelipeac.improov.core.platform.base.BaseViewState
import com.rafaelfelipeac.improov.features.welcome.domain.usecase.GetWelcomeUseCase
import com.rafaelfelipeac.improov.features.welcome.domain.usecase.SaveWelcomeUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class WelcomeViewModel @Inject constructor(
    private val saveWelcomeUseCase: SaveWelcomeUseCase,
    private val getWelcomeUseCase: GetWelcomeUseCase
) : BaseViewModel<WelcomeViewModel.ViewState, WelcomeViewModel.Action>(
    ViewState()
) {

    override fun onLoadData() {
        getWelcome()
    }

    fun onSaveWelcome(welcome: Boolean) {
        saveWelcome(welcome)
    }

    private fun saveWelcome(welcome: Boolean) {
        viewModelScope.launch {
            saveWelcomeUseCase.execute(welcome).also {
                sendAction(Action.WelcomeSaved)
            }
        }
    }

    private fun getWelcome() {
        viewModelScope.launch {
            getWelcomeUseCase.execute().also {
                sendAction(Action.WelcomeLoaded(it))
            }
        }
    }

    override fun onReduceState(viewAction: Action) = when (viewAction) {
        is Action.WelcomeLoaded -> state.copy(
            welcome = viewAction.welcome
        )
        is Action.WelcomeSaved -> state.copy(
            welcomeSaved = true
        )
    }

    data class ViewState(
        val welcomeSaved: Boolean = false,
        val welcome: Boolean = false
    ) : BaseViewState

    sealed class Action : BaseAction {
        object WelcomeSaved : Action()
        class WelcomeLoaded(val welcome: Boolean) : Action()
    }
}
