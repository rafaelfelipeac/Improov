package com.rafaelfelipeac.improov.features.profile.presentation.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.rafaelfelipeac.improov.core.platform.base.NewBaseViewModel
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
) : NewBaseViewModel() {

    val name: LiveData<String> get() = _name
    private val _name = MutableLiveData<String>()
    val saved: LiveData<Unit> get() = _saved
    private val _saved = MutableLiveData<Unit>()

    override fun loadData() {
        getName()
    }

    private fun getName() {
        viewModelScope.launch {
            getNameUseCase.execute().also {
                _name.postValue(it)
            }
        }
    }

    fun saveWelcome(welcome: Boolean) {
        viewModelScope.launch {
            saveWelcomeUseCase.execute(welcome).also {
                _saved.postValue(it)
            }
        }
    }

    fun saveFirstTimeAdd(saveFirstTimeAdd: Boolean) {
        viewModelScope.launch {
            saveFirstTimeAddUseCase.execute(saveFirstTimeAdd).also {
                _saved.postValue(it)
            }
        }
    }

    fun saveFirstTimeList(saveFirstTimeList: Boolean) {
        viewModelScope.launch {
            saveFirstTimeListUseCase.execute(saveFirstTimeList).also {
                _saved.postValue(it)
            }
        }
    }
}
