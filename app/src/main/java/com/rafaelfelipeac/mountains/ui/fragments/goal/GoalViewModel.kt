package com.rafaelfelipeac.mountains.ui.fragments.goal

import androidx.lifecycle.LiveData
import com.rafaelfelipeac.mountains.models.Goal
import com.rafaelfelipeac.mountains.ui.base.BaseViewModel


class GoalViewModel: BaseViewModel() {
    private var goalId: String? = null

    private val goal: LiveData<Goal>? = null

    fun getGoal(): LiveData<Goal>? {
        return this.goal
    }

    fun init(goalId: String) {
        this.goalId = goalId
    }

}