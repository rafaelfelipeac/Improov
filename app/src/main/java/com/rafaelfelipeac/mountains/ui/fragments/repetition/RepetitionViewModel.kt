package com.rafaelfelipeac.mountains.ui.fragments.repetition

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.rafaelfelipeac.mountains.models.Repetition
import com.rafaelfelipeac.mountains.models.User
import com.rafaelfelipeac.mountains.ui.base.BaseViewModel

class RepetitionViewModel: BaseViewModel() {

    private var repetition: LiveData<Repetition>? = null

    var user: MutableLiveData<User>? = MutableLiveData()

    init {
        getUser()
    }

    fun init(repetitionId: Long) {
        repetition = repetitionRepository.getRepetition(repetitionId)
    }

    // User
    private fun getUser() {
        user?.value = userRepository.getUserByUUI(auth.currentUser?.uid!!)
    }

    // Repetition
    fun getRepetition(): LiveData<Repetition>? {
        return repetition
    }

    fun saveRepetition(repetition: Repetition) {
        repetitionRepository.save(repetition)
    }
}