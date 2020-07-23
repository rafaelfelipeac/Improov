package com.rafaelfelipeac.improov.features.profile.presentation.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.rafaelfelipeac.improov.core.platform.base.BaseViewModel
import com.rafaelfelipeac.improov.features.profile.domain.usecase.GetNameUseCase
import com.rafaelfelipeac.improov.features.profile.domain.usecase.SaveFirstTimeAddUseCase
import com.rafaelfelipeac.improov.features.profile.domain.usecase.SaveFirstTimeListUseCase
import com.rafaelfelipeac.improov.features.profile.domain.usecase.SaveWelcomeUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val saveWelcomeUseCase: SaveWelcomeUseCase,
    private val saveFirstTimeAddUseCase: SaveFirstTimeAddUseCase,
    private val saveFirstTimeListUseCase: SaveFirstTimeListUseCase,
    private val getNameUseCase: GetNameUseCase
) : BaseViewModel() {

    val saved: LiveData<Unit> get() = _saved
    private val _saved = MutableLiveData<Unit>()
    val name: LiveData<String> get() = _name
    private val _name = MutableLiveData<String>()

    override fun loadData() {
        getName()
    }

    fun saveWelcome(welcome: Boolean) {
        viewModelScope.launch {
            _saved.postValue(saveWelcomeUseCase(welcome))
        }
    }

    fun saveFirstTimeAdd(saveFirstTimeAdd: Boolean) {
        viewModelScope.launch {
            _saved.postValue(saveFirstTimeAddUseCase(saveFirstTimeAdd))
        }
    }

    fun saveFirstTimeList(saveFirstTimeList: Boolean) {
        viewModelScope.launch {
            _saved.postValue(saveFirstTimeListUseCase(saveFirstTimeList))
        }
    }

    private fun getName() {
        viewModelScope.launch {
            _name.postValue(getNameUseCase())
        }
    }
}
