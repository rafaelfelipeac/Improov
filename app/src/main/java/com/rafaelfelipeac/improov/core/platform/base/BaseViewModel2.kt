package com.rafaelfelipeac.improov.core.platform.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rafaelfelipeac.improov.core.extension.toLiveData
import kotlin.properties.Delegates

abstract class BaseViewModel2<ViewState : BaseViewState, ViewAction : BaseAction>(initialState: ViewState) :
    ViewModel() {

    private val stateMutableLiveData = MutableLiveData<ViewState>()
    val stateLiveData = stateMutableLiveData.toLiveData()

    protected var state by Delegates.observable(initialState) { _, _, new ->
        stateMutableLiveData.value = new
    }

    fun sendAction(viewAction: ViewAction) {
        state = onReduceState(viewAction)
    }

    fun loadData() {
        onLoadData()
    }

    protected open fun onLoadData() {}

    protected abstract fun onReduceState(viewAction: ViewAction): ViewState
}

interface BaseViewState

interface BaseAction

