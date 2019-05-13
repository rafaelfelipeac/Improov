package com.rafaelfelipeac.mountains.ui.fragments.forgotPassword

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.rafaelfelipeac.mountains.models.FirebaseResult
import com.rafaelfelipeac.mountains.ui.base.BaseViewModel

class ForgotPasswordViewModel : BaseViewModel() {

    var forgotResult: MutableLiveData<FirebaseResult> = MutableLiveData()

    var auth: FirebaseAuth = FirebaseAuth.getInstance()

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