package com.rafaelfelipeac.improov.features.profile.presentation.profile

import androidx.lifecycle.viewModelScope
import com.rafaelfelipeac.improov.core.platform.base.BaseAction
import com.rafaelfelipeac.improov.core.platform.base.BaseViewModel
import com.rafaelfelipeac.improov.core.platform.base.BaseViewState
import com.rafaelfelipeac.improov.features.profile.domain.usecase.GetNameUseCase
import com.rafaelfelipeac.improov.features.profile.domain.usecase.SaveFirstTimeAddUseCase
import com.rafaelfelipeac.improov.features.profile.domain.usecase.SaveFirstTimeListUseCase
import com.rafaelfelipeac.improov.features.profile.domain.usecase.SaveWelcomeUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val saveWelcomeUseCase: SaveWelcomeUseCase,
    private val getNameUseCase: GetNameUseCase,
    private val saveFirstTimeAddUseCase: SaveFirstTimeAddUseCase,
    private val saveFirstTimeListUseCase: SaveFirstTimeListUseCase
) :
    BaseViewModel<ProfileViewModel.ViewState, ProfileViewModel.Action>(
        ViewState()
    ) {

    override fun onLoadData() {
        getName()
    }

    fun onSaveWelcome(welcome: Boolean) {
        saveWelcome(welcome)
    }

    fun onSaveFirstTimeAdd(firstTimeAdd: Boolean) {
        saveFirstTimeAdd(firstTimeAdd)
    }

    fun onSaveFirstTimeList(firstTimeList: Boolean) {
        saveFirstTimeList(firstTimeList)
    }

    private fun getName() {
        viewModelScope.launch {
            getNameUseCase.execute().also {
                sendAction(Action.NameLoaded(it))
            }
        }
    }

    private fun saveWelcome(welcome: Boolean) {
        viewModelScope.launch {
            saveWelcomeUseCase.execute(welcome).also {
                sendAction(Action.WelcomeSaved)
            }
        }
    }

    private fun saveFirstTimeAdd(saveFirstTimeAdd: Boolean) {
        viewModelScope.launch {
            saveFirstTimeAddUseCase.execute(saveFirstTimeAdd).also {
                sendAction(Action.FirstTimeAddSaved)
            }
        }
    }

    private fun saveFirstTimeList(saveFirstTimeList: Boolean) {
        viewModelScope.launch {
            saveFirstTimeListUseCase.execute(saveFirstTimeList).also {
                sendAction(Action.FirstTimeListSaved)
            }
        }
    }

    override fun onReduceState(viewAction: Action) = when (viewAction) {
        is Action.NameLoaded -> state.copy(
            name = viewAction.name
        )
        is Action.WelcomeSaved -> state.copy(
            welcomeSaved = true
        )
        is Action.FirstTimeAddSaved -> state.copy(
            firstTimeAddSaved = true
        )
        is Action.FirstTimeListSaved -> state.copy(
            firstTimeListSaved = true
        )
    }

    data class ViewState(
        val name: String = "",
        val welcomeSaved: Boolean = false,
        val firstTimeAddSaved: Boolean = false,
        val firstTimeListSaved: Boolean = false
    ) : BaseViewState

    sealed class Action : BaseAction {
        class NameLoaded(val name: String) : Action()
        object WelcomeSaved : Action()
        object FirstTimeAddSaved : Action()
        object FirstTimeListSaved : Action()
    }
}
