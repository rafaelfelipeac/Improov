package com.rafaelfelipeac.mountains.database.repetition

import androidx.lifecycle.LiveData
import com.rafaelfelipeac.mountains.models.Repetition
import javax.inject.Inject

class RepetitionRepository @Inject constructor(private val repetitionDAO: RepetitionDAO) {

    fun getRepetitions(): LiveData<List<Repetition>> {
        return repetitionDAO.getAll()
    }

    fun getRepetition(repetitionId: Long): LiveData<Repetition> {
        return repetitionDAO.get(repetitionId)
    }

    fun save(repetition: Repetition): Long {
        return repetitionDAO.save(repetition)
    }

    fun delete(repetition: Repetition) {
        return repetitionDAO.delete(repetition)
    }
}