package com.rafaelfelipeac.mountains.ui.fragments.stats

import androidx.lifecycle.LiveData
import com.rafaelfelipeac.mountains.models.Goal
import com.rafaelfelipeac.mountains.ui.base.BaseViewModel

class StatsViewModel: BaseViewModel() {

    private var goals: LiveData<List<Goal>>? = null

    init {
        goals = goalRepository.getGoals()
    }

    // Goal
    fun getGoals(): LiveData<List<Goal>>? {
        return goals
    }
}