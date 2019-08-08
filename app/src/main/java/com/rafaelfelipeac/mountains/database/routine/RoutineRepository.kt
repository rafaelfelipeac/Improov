package com.rafaelfelipeac.mountains.database.routine

import androidx.lifecycle.LiveData
import com.rafaelfelipeac.mountains.models.Routine
import javax.inject.Inject

class RoutineRepository @Inject constructor(private val routineDAO: RoutineDAO) {

    fun getRoutines(): LiveData<List<Routine>> {
        return routineDAO.getAll()
    }

    fun getRoutine(routineId: Long): LiveData<Routine> {
        return routineDAO.get(routineId)
    }

    fun save(routine: Routine): Long {
        return routineDAO.save(routine)
    }

    fun delete(routine: Routine) {
        return routineDAO.delete(routine)
    }
}