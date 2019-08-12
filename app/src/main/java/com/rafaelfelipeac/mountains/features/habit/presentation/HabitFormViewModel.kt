package com.rafaelfelipeac.mountains.features.habit.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.rafaelfelipeac.mountains.others.models.user.UserRepository
import com.rafaelfelipeac.mountains.others.models.User
import com.rafaelfelipeac.mountains.core.platform.BaseViewModel
import com.rafaelfelipeac.mountains.features.habit.Habit
import com.rafaelfelipeac.mountains.features.habit.HabitRepository
import javax.inject.Inject

class HabitFormViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val habitRepository: HabitRepository
) : BaseViewModel() {

    private var habit: LiveData<Habit>? = null
    private var habits: LiveData<List<Habit>>

    var user: MutableLiveData<User>? = MutableLiveData()

    var habitIdInserted: MutableLiveData<Long> = MutableLiveData()

    init {
        getUser()

        habits = habitRepository.getHabits()
    }

    fun init(habitId: Long) {
        habit = habitRepository.getHabit(habitId)
    }

    // User
    private fun getUser() {
        user?.value = userRepository.getUserByUUI(auth.currentUser?.uid!!)
    }

    // Habit
    fun getHabits(): LiveData<List<Habit>>? {
        return habits
    }

    fun getHabit(): LiveData<Habit>? {
        return habit
    }

    fun saveHabit(habit: Habit) {
        habitIdInserted.value = habitRepository.save(habit)
    }
}