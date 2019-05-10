package com.rafaelfelipeac.mountains.ui.fragments.login

import androidx.lifecycle.MutableLiveData
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.rafaelfelipeac.mountains.ui.base.BaseViewModel

class LoginViewModel : BaseViewModel() {

    var loginSuccess: MutableLiveData<Boolean> = MutableLiveData()

    var auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            run {
                if (task.isSuccessful) {
                    var user = auth.currentUser

                    loginSuccess.value = true
                } else {
                    loginSuccess.value = false
                }
            }
        }
    }

    fun signInGoogle(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)

        auth.signInWithCredential(credential).addOnCompleteListener { task ->
            run {
                if (task.isSuccessful) {
                    val user = auth.currentUser

                    loginSuccess.value = true
                } else {
                    loginSuccess.value = false
                }
            }
        }
    }
}