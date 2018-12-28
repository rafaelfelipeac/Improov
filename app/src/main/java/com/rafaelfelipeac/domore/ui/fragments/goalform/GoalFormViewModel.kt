package com.rafaelfelipeac.domore.ui.fragments.goalform

import com.rafaelfelipeac.domore.app.App
import com.rafaelfelipeac.domore.database.goal.GoalDAO
import com.rafaelfelipeac.domore.database.goal.GoalRepository
import com.rafaelfelipeac.domore.models.Goal
import com.rafaelfelipeac.domore.ui.base.BaseViewModel

class GoalFormViewModel : BaseViewModel() {

    private val goalDAO: GoalDAO = App.database?.goalDAO()!!

    private val goalRepository: GoalRepository = GoalRepository(goalDAO)

    fun saveGoal(goal: Goal) {
        goalRepository.insert(goal)
    }
}