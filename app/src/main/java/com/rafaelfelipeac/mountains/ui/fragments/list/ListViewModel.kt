package com.rafaelfelipeac.mountains.ui.fragments.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.rafaelfelipeac.mountains.database.goal.GoalRepository
import com.rafaelfelipeac.mountains.database.habit.HabitRepository
import com.rafaelfelipeac.mountains.database.user.UserRepository
import com.rafaelfelipeac.mountains.models.Goal
import com.rafaelfelipeac.mountains.models.Habit
import com.rafaelfelipeac.mountains.models.User
import com.rafaelfelipeac.mountains.ui.base.BaseViewModel
import javax.inject.Inject

class ListViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val goalRepository: GoalRepository,
    private val habitRepository: HabitRepository) : BaseViewModel() {

    private var goals: LiveData<List<Goal>>
    private var habits: LiveData<List<Habit>>

    var user: MutableLiveData<User>? = MutableLiveData()

    init {
        verifyUser()

        goals = goalRepository.getGoals()
        habits = habitRepository.getHabits()
    }

    private fun verifyUser() {
        if (userRepository.getUserByUUI(auth.currentUser?.uid!!) == null) {
            val userToSave = User()

            userToSave.uui = auth.currentUser?.uid!!
            userToSave.email = auth.currentUser?.email!!

            userRepository.save(userToSave)
        }

        user?.value = userRepository.getUserByUUI(auth.currentUser?.uid!!)
    }

    // Goal
    fun getGoals(): LiveData<List<Goal>>? {
        return goals
    }

    fun saveGoal(goal: Goal) {
        goalRepository.save(goal)

        goals = goalRepository.getGoals()
    }

    // Habit
    fun getHabits(): LiveData<List<Habit>>? {
        return habits
    }

    fun saveHabit(habit: Habit) {
        habitRepository.save(habit)

        habits = habitRepository.getHabits()
    }
}