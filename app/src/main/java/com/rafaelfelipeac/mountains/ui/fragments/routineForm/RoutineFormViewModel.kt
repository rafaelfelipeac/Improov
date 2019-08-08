package com.rafaelfelipeac.mountains.ui.fragments.routineForm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.rafaelfelipeac.mountains.models.Routine
import com.rafaelfelipeac.mountains.models.User
import com.rafaelfelipeac.mountains.ui.base.BaseViewModel

class RoutineFormViewModel : BaseViewModel() {
    private var routine: LiveData<Routine>? = null
    private var routines: LiveData<List<Routine>>? = null

    var user: MutableLiveData<User>? = MutableLiveData()

    var routineIdInserted: MutableLiveData<Long> = MutableLiveData()

    init {
        getUser()

        routines = routineRepository.getRoutines()
    }

    fun init(routineId: Long) {
        routine = routineRepository.getRoutine(routineId)
    }

    // User
    private fun getUser() {
        user?.value = userRepository.getUserByUUI(auth.currentUser?.uid!!)
    }

    // Routine
    fun getRoutines(): LiveData<List<Routine>>? {
        return routines
    }

    fun getRoutine(): LiveData<Routine>? {
        return routine
    }

    fun saveRoutine(routine: Routine) {
        routineIdInserted.value = routineRepository.save(routine)
    }
}