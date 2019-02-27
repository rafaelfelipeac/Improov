package com.rafaelfelipeac.domore.ui.fragments.goals

import com.rafaelfelipeac.domore.app.App
import com.rafaelfelipeac.domore.database.goal.GoalDAO
import com.rafaelfelipeac.domore.database.goal.GoalRepository
import com.rafaelfelipeac.domore.models.Goal
import com.rafaelfelipeac.domore.ui.base.BaseViewModel

class GoalsViewModel : BaseViewModel() {

    private val goalDAO: GoalDAO = App.database?.goalDAO()!!
    private val goalRepository: GoalRepository = GoalRepository(goalDAO)

    fun getGoals(): List<Goal> {
        return goalRepository.getGoals()
    }
}