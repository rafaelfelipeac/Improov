package com.rafaelfelipeac.mountains.ui.fragments.createUser

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.rafaelfelipeac.mountains.ui.base.BaseViewModel

class CreateUserViewModel: BaseViewModel() {

    var createUserSuccess: MutableLiveData<Boolean> = MutableLiveData()

    var auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun createUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            run {
                if (task.isSuccessful) {
                    var user = auth.currentUser

                    createUserSuccess.value = true
                } else {
                    createUserSuccess.value = false
                }
            }
        }
    }
}