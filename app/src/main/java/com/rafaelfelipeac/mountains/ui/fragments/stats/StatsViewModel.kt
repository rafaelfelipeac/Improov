package com.rafaelfelipeac.mountains.ui.fragments.stats

import androidx.lifecycle.LiveData
import com.rafaelfelipeac.mountains.models.Goal
import com.rafaelfelipeac.mountains.models.Repetition
import com.rafaelfelipeac.mountains.ui.base.BaseViewModel

class StatsViewModel: BaseViewModel() {

    private var goals: LiveData<List<Goal>>? = null
    private var repetitions: LiveData<List<Repetition>>? = null

    init {
        goals = goalRepository.getGoals()
        repetitions = repetitionRepository.getRepetitions()
    }

    // Goal
    fun getGoals(): LiveData<List<Goal>>? {
        return goals
    }

    // Repetition
    fun getRepetitions(): LiveData<List<Repetition>>? {
        return repetitions
    }
}