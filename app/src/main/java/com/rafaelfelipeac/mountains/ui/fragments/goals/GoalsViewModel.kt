package com.rafaelfelipeac.mountains.ui.fragments.goals

import com.rafaelfelipeac.mountains.app.App
import com.rafaelfelipeac.mountains.database.goal.GoalDAO
import com.rafaelfelipeac.mountains.database.goal.GoalRepository
import com.rafaelfelipeac.mountains.models.Goal
import com.rafaelfelipeac.mountains.ui.base.BaseViewModel

class GoalsViewModel : BaseViewModel() {

    private val goalDAO: GoalDAO = App.database?.goalDAO()!!
    private val goalRepository: GoalRepository = GoalRepository(goalDAO)

    fun getGoals(): List<Goal> {
        return goalRepository.getGoals()
    }
}