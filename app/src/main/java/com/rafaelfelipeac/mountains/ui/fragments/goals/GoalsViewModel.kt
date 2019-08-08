package com.rafaelfelipeac.mountains.ui.fragments.goals

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.rafaelfelipeac.mountains.models.Goal
import com.rafaelfelipeac.mountains.models.Repetition
import com.rafaelfelipeac.mountains.models.User
import com.rafaelfelipeac.mountains.ui.base.BaseViewModel

class GoalsViewModel : BaseViewModel() {
    private var goals: LiveData<List<Goal>>? = null
    private var repetitions: LiveData<List<Repetition>>? = null

    var user: MutableLiveData<User>? = MutableLiveData()

    init {
        verifyUser()

        goals = goalRepository.getGoals()
        repetitions = repetitionRepository.getRepetitions()
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

    fun deleteGoal(goal: Goal) {
        goalRepository.delete(goal)

        goals = goalRepository.getGoals()
    }

    // Repetition
    fun getRepetitions(): LiveData<List<Repetition>>? {
        return repetitions
    }

    fun saveRepetition(repetition: Repetition) {
        repetitionRepository.save(repetition)

        repetitions = repetitionRepository.getRepetitions()
    }
}