package com.rafaelfelipeac.mountains.features.goal.presentation.goalform

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.rafaelfelipeac.mountains.features.commons.UserRepository
import com.rafaelfelipeac.mountains.features.commons.User
import com.rafaelfelipeac.mountains.core.platform.base.BaseViewModel
import com.rafaelfelipeac.mountains.features.commons.Goal
import com.rafaelfelipeac.mountains.features.commons.GoalRepository
import com.rafaelfelipeac.mountains.features.commons.Habit
import com.rafaelfelipeac.mountains.features.commons.HabitRepository
import javax.inject.Inject

class GoalFormViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val goalRepository: GoalRepository,
    private val habitRepository: HabitRepository
) : BaseViewModel() {

    private var goal: LiveData<Goal>? = null
    private var goals: LiveData<List<Goal>>
    private var habits: LiveData<List<Habit>>

    var user: MutableLiveData<User>? = MutableLiveData()

    var goalIdInserted: MutableLiveData<Long> = MutableLiveData()

    init {
        getUser()

        goals = goalRepository.getGoals()
        habits = habitRepository.getHabits()
    }

    fun init(goalId: Long) {
        goal = goalRepository.getGoal(goalId)
    }

    // User
    private fun getUser() {
        user?.value = userRepository.getUserByUUI(auth.currentUser?.uid!!)
    }

    // Goal
    fun getGoals(): LiveData<List<Goal>>? {
        return goals
    }

    fun getGoal(): LiveData<Goal>? {
        return goal
    }

    fun saveGoal(goal: Goal) {
        goalIdInserted.value = goalRepository.save(goal)
    }

    // Habit
    fun getHabits(): LiveData<List<Habit>>? {
        return habits
    }
}