package com.rafaelfelipeac.mountains.ui.fragments.otherGoalForm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.rafaelfelipeac.mountains.models.Goal
import com.rafaelfelipeac.mountains.models.User
import com.rafaelfelipeac.mountains.ui.base.BaseViewModel

class OtherGoalFormViewModel : BaseViewModel() {
    private var goal: LiveData<Goal>? = null
    private var goals: LiveData<List<Goal>>? = null

    var user: MutableLiveData<User>? = MutableLiveData()

    var goalIdInserted: MutableLiveData<Long> = MutableLiveData()

    init {
        getUser()

        goals = goalRepository.getGoals()
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
}