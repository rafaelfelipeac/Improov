package com.rafaelfelipeac.improov.features.settings.presentation.settings

import com.rafaelfelipeac.improov.core.platform.base.BaseAction
import com.rafaelfelipeac.improov.core.platform.base.BaseViewModel
import com.rafaelfelipeac.improov.core.platform.base.BaseViewState
import javax.inject.Inject

class SettingsViewModel @Inject constructor() : BaseViewModel<SettingsViewModel.ViewState, SettingsViewModel.Action>(
    ViewState()
) {

    override fun onLoadData() {
        super.onLoadData()
    }

    override fun onReduceState(viewAction: Action) = when (viewAction) {
        is Action.Settings -> state.copy(
            settings = true
        )
    }

    data class ViewState(
        val settings: Boolean = false
    ) : BaseViewState

    sealed class Action : BaseAction {
        object Settings : Action()
    }
}
