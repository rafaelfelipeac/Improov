package com.rafaelfelipeac.mountains.ui.fragments.stats

import androidx.lifecycle.LiveData
import com.rafaelfelipeac.mountains.models.Goal
import com.rafaelfelipeac.mountains.models.Routine
import com.rafaelfelipeac.mountains.ui.base.BaseViewModel

class StatsViewModel: BaseViewModel() {

    private var goals: LiveData<List<Goal>>? = null
    private var routines: LiveData<List<Routine>>? = null

    init {
        goals = goalRepository.getGoals()
        routines = routineRepository.getRoutines()
    }

    // Goal
    fun getGoals(): LiveData<List<Goal>>? {
        return goals
    }

    // Routine
    fun getRoutines(): LiveData<List<Routine>>? {
        return routines
    }
}