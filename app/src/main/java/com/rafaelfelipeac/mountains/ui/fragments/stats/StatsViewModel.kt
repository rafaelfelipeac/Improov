package com.rafaelfelipeac.mountains.ui.fragments.stats

import androidx.lifecycle.LiveData
import com.rafaelfelipeac.mountains.models.Goal
import com.rafaelfelipeac.mountains.ui.base.BaseViewModel

class StatsViewModel: BaseViewModel() {

    fun getGoals(): LiveData<List<Goal>> {
        return goalRepository.getGoals()
    }
}