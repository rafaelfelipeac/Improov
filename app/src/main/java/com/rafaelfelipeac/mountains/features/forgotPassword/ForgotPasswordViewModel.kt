package com.rafaelfelipeac.mountains.features.forgotPassword

import androidx.lifecycle.MutableLiveData
import com.rafaelfelipeac.mountains.others.models.FirebaseResult
import com.rafaelfelipeac.mountains.core.platform.BaseViewModel
import javax.inject.Inject

class ForgotPasswordViewModel @Inject constructor() : BaseViewModel() {

    var forgotResult: MutableLiveData<FirebaseResult> = MutableLiveData()

    fun resetPassword(email: String) {
        auth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
            val firebaseResult = FirebaseResult()
            firebaseResult.isSuccessful = task.isSuccessful

            if (task.isSuccessful) {
                forgotResult.value = firebaseResult
            } else {
                firebaseResult.message = task.exception?.localizedMessage!!

                forgotResult.value = firebaseResult
            }
        }
    }
}