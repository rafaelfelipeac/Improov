package com.rafaelfelipeac.mountains.features.createuser

import androidx.lifecycle.MutableLiveData
import com.rafaelfelipeac.mountains.network.firebase.FirebaseResult
import com.rafaelfelipeac.mountains.core.platform.base.BaseViewModel
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