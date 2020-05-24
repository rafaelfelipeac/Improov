package com.rafaelfelipeac.improov.features.profile.presentation.profileedit

import androidx.lifecycle.viewModelScope
import com.rafaelfelipeac.improov.core.platform.base.BaseAction
import com.rafaelfelipeac.improov.core.platform.base.BaseViewModel
import com.rafaelfelipeac.improov.core.platform.base.BaseViewState
import com.rafaelfelipeac.improov.features.profile.domain.usecase.GetNameUseCase
import com.rafaelfelipeac.improov.features.profile.domain.usecase.SaveNameUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProfileEditViewModel @Inject constructor(
    private val saveNameUseCase: SaveNameUseCase,
    private val getNameUseCase: GetNameUseCase
) :
    BaseViewModel<ProfileEditViewModel.ViewState, ProfileEditViewModel.Action>(
        ViewState()
    ) {

    override fun onLoadData() {
        getName()
    }

    fun onSaveName(name: String) {
        saveName(name)
    }

    private fun getName() {
        viewModelScope.launch {
            getNameUseCase.execute().also {
                sendAction(Action.NameLoaded(it))
            }
        }
    }

    private fun saveName(name: String) {
        viewModelScope.launch {
            saveNameUseCase.execute(name).also {
                sendAction(Action.NameSaved)
            }
        }
    }

    override fun onReduceState(viewAction: Action) = when (viewAction) {
        is Action.NameLoaded -> state.copy(
            name = viewAction.name
        )
        is Action.NameSaved -> state.copy(
            nameSaved = true
        )
    }

    data class ViewState(
        val name: String = "",
        val nameSaved: Boolean = false
    ) : BaseViewState

    sealed class Action : BaseAction {
        class NameLoaded(val name: String) : Action()
        object NameSaved : Action()
    }
}
