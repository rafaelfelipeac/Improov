package com.rafaelfelipeac.mountains.ui.fragments.forgotPassword

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.rafaelfelipeac.mountains.ui.base.BaseViewModel

class ForgotPasswordViewModel: BaseViewModel() {

    var emailSent: MutableLiveData<Boolean> = MutableLiveData()

    var auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun resetPassword(email: String) {
        auth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    var user = auth.currentUser

                    emailSent.value = true
                } else {
                    emailSent.value = false
                }
            }
    }

}