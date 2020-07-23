package com.rafaelfelipeac.improov.features.profile.presentation.profileedit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.rafaelfelipeac.improov.core.platform.base.BaseViewModel
import com.rafaelfelipeac.improov.features.profile.domain.usecase.GetNameUseCase
import com.rafaelfelipeac.improov.features.profile.domain.usecase.SaveNameUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProfileEditViewModel @Inject constructor(
    private val saveNameUseCase: SaveNameUseCase,
    private val getNameUseCase: GetNameUseCase
) : BaseViewModel() {

    val saved: LiveData<Unit> get() = _saved
    private val _saved = MutableLiveData<Unit>()
    val name: LiveData<String> get() = _name
    private val _name = MutableLiveData<String>()

    override fun loadData() {
        getName()
    }

    fun saveName(name: String) {
        viewModelScope.launch {
            _saved.postValue(saveNameUseCase(name))
        }
    }

    private fun getName() {
        viewModelScope.launch {
            _name.postValue(getNameUseCase())
        }
    }
}
