package com.rafaelfelipeac.mountains.ui.fragments.routine

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.rafaelfelipeac.mountains.models.Routine
import com.rafaelfelipeac.mountains.models.User
import com.rafaelfelipeac.mountains.ui.base.BaseViewModel

class RoutineViewModel: BaseViewModel() {

    private var routine: LiveData<Routine>? = null

    var user: MutableLiveData<User>? = MutableLiveData()

    init {
        getUser()
    }

    fun init(routineId: Long) {
        routine = routineRepository.getRoutine(routineId)
    }

    // User
    private fun getUser() {
        user?.value = userRepository.getUserByUUI(auth.currentUser?.uid!!)
    }

    // Routine
    fun getRoutines(): LiveData<Routine>? {
        return routine
    }

    fun saveRoutine(routine: Routine) {
        routineRepository.save(routine)
    }
}