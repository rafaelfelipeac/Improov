package com.rafaelfelipeac.improov.future.stats.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rafaelfelipeac.improov.future.stats.domain.usecase.GetSingleValueUseCase
import com.rafaelfelipeac.improov.future.stats.domain.usecase.GetBronzeValueUseCase
import com.rafaelfelipeac.improov.future.stats.domain.usecase.GetSilverValueUseCase
import com.rafaelfelipeac.improov.future.stats.domain.usecase.GetGoldValueUseCase
import com.rafaelfelipeac.improov.future.stats.domain.usecase.GetHabitListUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import javax.inject.Inject

class StatsViewModel @Inject constructor(
    private val getSingleValueUseCase: GetSingleValueUseCase,
    private val getBronzeValueUseCase: GetBronzeValueUseCase,
    private val getSilverValueUseCase: GetSilverValueUseCase,
    private val getGoldValueUseCase: GetGoldValueUseCase,
    private val getHabitListUseCase: GetHabitListUseCase
) : ViewModel() {

    val singleValue: Flow<Int> get() = _singleValue.filterNotNull()
    private val _singleValue = MutableStateFlow<Int?>(null)
    val bronzeValue: Flow<Int> get() = _bronzeValue.filterNotNull()
    private val _bronzeValue = MutableStateFlow<Int?>(null)
    val silverValue: Flow<Int> get() = _silverValue.filterNotNull()
    private val _silverValue = MutableStateFlow<Int?>(null)
    val goldValue: Flow<Int> get() = _goldValue.filterNotNull()
    private val _goldValue = MutableStateFlow<Int?>(null)
    val habitsValue: Flow<Int> get() = _habitsValue.filterNotNull()
    private val _habitsValue = MutableStateFlow<Int?>(null)
    
    init {
        getSingleValue()
        getBronzeValue()
        getSilverValue()
        getGoldValue()
        getHabitsValue()
    }

    private fun getSingleValue() {
        viewModelScope.launch {
            _singleValue.value = getSingleValueUseCase()
        }
    }
    
    private fun getBronzeValue() {
        viewModelScope.launch {
            _bronzeValue.value = getBronzeValueUseCase()
        }
    }

    private fun getSilverValue() {
        viewModelScope.launch {
            _silverValue.value = getSilverValueUseCase()
        }
    }

    private fun getGoldValue() {
        viewModelScope.launch {
            _goldValue.value = getGoldValueUseCase()
        }
    }
    
    private fun getHabitsValue() {
        viewModelScope.launch {
            _habitsValue.value = getHabitListUseCase()
        }
    }
}
