package com.rafaelfelipeac.mountains.ui.fragments.repetitionForm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.rafaelfelipeac.mountains.models.Repetition
import com.rafaelfelipeac.mountains.models.User
import com.rafaelfelipeac.mountains.ui.base.BaseViewModel

class RepetitionFormViewModel : BaseViewModel() {
    private var repetition: LiveData<Repetition>? = null
    private var repetitions: LiveData<List<Repetition>>? = null

    var user: MutableLiveData<User>? = MutableLiveData()

    var repetitionIdInserted: MutableLiveData<Long> = MutableLiveData()

    init {
        getUser()

        repetitions = repetitionRepository.getRepetitions()
    }

    fun init(goalId: Long) {
        repetition = repetitionRepository.getRepetition(goalId)
    }

    // User
    private fun getUser() {
        user?.value = userRepository.getUserByUUI(auth.currentUser?.uid!!)
    }

    // Repetition
    fun getRepetitions(): LiveData<List<Repetition>>? {
        return repetitions
    }

    fun getRepetition(): LiveData<Repetition>? {
        return repetition
    }

    fun saveRepetition(repetition: Repetition) {
        repetitionIdInserted.value = repetitionRepository.save(repetition)
    }
}