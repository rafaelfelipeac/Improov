package com.rafaelfelipeac.mountains.ui.fragments.goalForm

import com.rafaelfelipeac.mountains.app.App
import com.rafaelfelipeac.mountains.database.goal.GoalDAO
import com.rafaelfelipeac.mountains.database.goal.GoalRepository
import com.rafaelfelipeac.mountains.models.Goal
import com.rafaelfelipeac.mountains.ui.base.BaseViewModel

class GoalFormViewModel : BaseViewModel() {

    private val goalDAO: GoalDAO = App.database?.goalDAO()!!

    private val goalRepository: GoalRepository = GoalRepository(goalDAO)

    fun saveGoal(goal: Goal) {
        goalRepository.insert(goal)
    }

    fun updateGoal(goal: Goal) {
        goalRepository.update(goal)
    }
}