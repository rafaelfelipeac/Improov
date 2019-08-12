package com.rafaelfelipeac.mountains.features.today

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.rafaelfelipeac.mountains.others.models.user.UserRepository
import com.rafaelfelipeac.mountains.others.models.User
import com.rafaelfelipeac.mountains.core.platform.BaseViewModel
import com.rafaelfelipeac.mountains.features.goal.Goal
import com.rafaelfelipeac.mountains.features.goal.GoalRepository
import com.rafaelfelipeac.mountains.features.habit.Habit
import com.rafaelfelipeac.mountains.features.habit.HabitRepository
import javax.inject.Inject

class TodayViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val goalRepository: GoalRepository,
    private val habitRepository: HabitRepository
) : BaseViewModel() {

    private var habits: LiveData<List<Habit>>? = null
    private var goals: LiveData<List<Goal>>? = null

    var user: MutableLiveData<User>? = MutableLiveData()

    init {
        verifyUser()

        habits = habitRepository.getHabits()
        goals = goalRepository.getGoals()
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

        this.goals = goalRepository.getGoals()
    }

    // Habit
    fun getHabits(): LiveData<List<Habit>>? {
        return habits
    }

    fun saveHabit(habit: Habit) {
        habitRepository.save(habit)

        this.habits = habitRepository.getHabits()
    }
}