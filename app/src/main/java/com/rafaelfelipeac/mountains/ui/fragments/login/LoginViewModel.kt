package com.rafaelfelipeac.mountains.ui.fragments.login

import androidx.lifecycle.MutableLiveData
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.GoogleAuthProvider
import com.rafaelfelipeac.mountains.models.FirebaseResult
import com.rafaelfelipeac.mountains.models.User
import com.rafaelfelipeac.mountains.ui.base.BaseViewModel

class LoginViewModel : BaseViewModel() {

    var loginResult: MutableLiveData<FirebaseResult> = MutableLiveData()

    fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            val firebaseResult = FirebaseResult()
            firebaseResult.isSuccessful = task.isSuccessful

            if (task.isSuccessful) {
                loginResult.value = firebaseResult

                val user = userRepository.getItemByUUI(auth.currentUser?.uid!!)

                user.let {
                    it.name = auth.currentUser?.displayName!!
                    it.email = auth.currentUser?.email!!

                    userRepository.save(it)
                }

//                if (user == null) {
//                    user.name = auth.currentUser?.displayName!!
//                    user.email = auth.currentUser?.email!!
//
//                    userRepository.save(user)
//                }

            } else {
                firebaseResult.message = task.exception?.message!!

                loginResult.value = firebaseResult
            }
        }
    }

    fun signInGoogle(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)

        auth.signInWithCredential(credential).addOnCompleteListener { task ->
            val firebaseResult = FirebaseResult()
            firebaseResult.isSuccessful = task.isSuccessful

            if (task.isSuccessful) {
                loginResult.value = firebaseResult
            } else {
                firebaseResult.message = task.exception?.message!!

                loginResult.value = firebaseResult
            }
        }
    }
}