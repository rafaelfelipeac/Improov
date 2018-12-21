package com.rafaelfelipeac.readmore.ui.fragments.goals

import com.rafaelfelipeac.readmore.app.App
import com.rafaelfelipeac.readmore.database.goal.GoalDAO
import com.rafaelfelipeac.readmore.database.goal.GoalRepository
import com.rafaelfelipeac.readmore.models.Goal
import com.rafaelfelipeac.readmore.ui.base.BaseViewModel

class GoalsViewModel : BaseViewModel() {

    private val goalDAO: GoalDAO = App.database?.goalDAO()!!
    private val goalRepository: GoalRepository = GoalRepository(goalDAO)

    fun getGoals(): List<Goal> {
        return goalRepository.getGoals()
    }
}