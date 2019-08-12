package com.rafaelfelipeac.mountains.ui.fragments.habitForm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.rafaelfelipeac.mountains.models.Habit
import com.rafaelfelipeac.mountains.models.User
import com.rafaelfelipeac.mountains.ui.base.BaseViewModel
import javax.inject.Inject

class HabitFormViewModel @Inject constructor() : BaseViewModel() {
    private var habit: LiveData<Habit>? = null
    private var habits: LiveData<List<Habit>>? = null

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