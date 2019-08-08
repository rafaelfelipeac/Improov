package com.rafaelfelipeac.mountains.ui.fragments.goals

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.rafaelfelipeac.mountains.models.Goal
import com.rafaelfelipeac.mountains.models.Routine
import com.rafaelfelipeac.mountains.models.User
import com.rafaelfelipeac.mountains.ui.base.BaseViewModel

class GoalsViewModel : BaseViewModel() {
    private var goals: LiveData<List<Goal>>? = null
    private var routines: LiveData<List<Routine>>? = null

    var user: MutableLiveData<User>? = MutableLiveData()

    init {
        verifyUser()

        goals = goalRepository.getGoals()
        routines = routineRepository.getRoutines()
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

    // Routine
    fun getRoutines(): LiveData<List<Routine>>? {
        return routines
    }

    fun saveRoutine(routine: Routine) {
        routineRepository.save(routine)

        routines = routineRepository.getRoutines()
    }
}