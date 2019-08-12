package com.rafaelfelipeac.mountains.ui.fragments.createUser

import androidx.lifecycle.MutableLiveData
import com.rafaelfelipeac.mountains.models.FirebaseResult
import com.rafaelfelipeac.mountains.ui.base.BaseViewModel
import javax.inject.Inject

class CreateUserViewModel @Inject constructor() : BaseViewModel() {

    var createResult: MutableLiveData<FirebaseResult> = MutableLiveData()

    fun createUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            val firebaseResult = FirebaseResult()
            firebaseResult.isSuccessful = task.isSuccessful

            if (task.isSuccessful) {
                createResult.value = firebaseResult
            } else {
                firebaseResult.message = task.exception?.message!!

                createResult.value = firebaseResult
            }
        }
    }
}