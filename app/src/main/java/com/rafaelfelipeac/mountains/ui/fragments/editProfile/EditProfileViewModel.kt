package com.rafaelfelipeac.mountains.ui.fragments.editProfile

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.UserProfileChangeRequest
import com.rafaelfelipeac.mountains.models.FirebaseResult
import com.rafaelfelipeac.mountains.ui.base.BaseViewModel
import javax.inject.Inject

class EditProfileViewModel @Inject constructor() : BaseViewModel() {
    var updateUser: MutableLiveData<FirebaseResult> = MutableLiveData()

    fun updateName(name: String) {
        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName(name)
            .build()

        auth.currentUser?.updateProfile(profileUpdates)
            ?.addOnCompleteListener { task ->
                val firebaseResult = FirebaseResult()
                firebaseResult.isSuccessful = task.isSuccessful

                if (task.isSuccessful) {
                    updateUser.value = firebaseResult
                } else {
                    firebaseResult.message = task.exception?.message!!

                    updateUser.value = firebaseResult
                }
            }
    }

    fun updatePassword(password: String) {
        auth.currentUser?.updatePassword(password)
            ?.addOnCompleteListener { task ->
                val firebaseResult = FirebaseResult()
                firebaseResult.isSuccessful = task.isSuccessful

                if (task.isSuccessful) {
                    updateUser.value = firebaseResult
                } else {
                    firebaseResult.message = task.exception?.message!!

                    updateUser.value = firebaseResult
                }
            }
    }

    fun updateEmail(email: String) {
        auth.currentUser?.updateEmail(email)
            ?.addOnCompleteListener { task ->
                val firebaseResult = FirebaseResult()
                firebaseResult.isSuccessful = task.isSuccessful

                if (task.isSuccessful) {
                    updateUser.value = firebaseResult
                } else {
                    firebaseResult.message = task.exception?.message!!

                    updateUser.value = firebaseResult
                }
            }
    }
}