package com.rafaelfelipeac.mountains.ui.fragments.createUser

import android.content.ContentValues.TAG
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.auth.ActionCodeSettings
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.rafaelfelipeac.mountains.R
import com.rafaelfelipeac.mountains.ui.activities.MainActivity
import com.rafaelfelipeac.mountains.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_create_user.*

class CreateUserFragment : BaseFragment() {

    private var auth: FirebaseAuth? = null

    private lateinit var viewModel: CreateUserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //injector.inject(this)

        auth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        (activity as MainActivity).supportActionBar?.title = getString(R.string.fragment_create_user_title)

        viewModel = ViewModelProviders.of(this).get(CreateUserViewModel::class.java)

        hideNavigation()

        return inflater.inflate(R.layout.fragment_create_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()

        create_user_create_button.setOnClickListener {
            navController.navigate(CreateUserFragmentDirections.actionNavigationCreateUserToNavigationGoals())
        }
    }

    private fun observeViewModel() {

    }

    private fun createUserWithEmailAndPassword() {
//        auth?.createUserWithEmailAndPassword(email, password)
//            .addOnCompleteListener(this) { task ->
//                if (task.isSuccessful) {
//                    // Sign in success, update UI with the signed-in user's information
//                    Log.d(TAG, "createUserWithEmail:success")
//                    val user = auth.currentUser
//                    updateUI(user)
//                } else {
//                    // If sign in fails, display a message to the user.
//                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
//                    Toast.makeText(baseContext, "Authentication failed.",
//                        Toast.LENGTH_SHORT).show()
//                    updateUI(null)
//                }
//
//                // ...
//            }
    }

    private fun checkCurrentUser() {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            // User is signed in
        } else {
            // No user is signed in
        }
    }

    private fun getUserProfile() {
        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            // Name, email address, and profile photo Url
            val name = user.displayName
            val email = user.email
            val photoUrl = user.photoUrl

            // Check if user's email is verified
            val emailVerified = user.isEmailVerified

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getToken() instead.
            val uid = user.uid
        }
    }

    private fun getProviderData() {
        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            for (profile in it.providerData) {
                // Id of the provider (ex: google.com)
                val providerId = profile.providerId

                // UID specific to the provider
                val uid = profile.uid

                // Name, email address, and profile photo Url
                val name = profile.displayName
                val email = profile.email
                val photoUrl = profile.photoUrl
            }
        }
    }

    private fun updateProfile() {
        val user = FirebaseAuth.getInstance().currentUser

        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName("Jane Q. User")
            .setPhotoUri(Uri.parse("https://example.com/jane-q-user/profile.jpg"))
            .build()

        user?.updateProfile(profileUpdates)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "User profile updated.")
                }
            }
    }

    private fun updateEmail() {
        val user = FirebaseAuth.getInstance().currentUser

        user?.updateEmail("user@example.com")
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "User email address updated.")
                }
            }
    }

    private fun updatePassword() {
        val user = FirebaseAuth.getInstance().currentUser
        val newPassword = "SOME-SECURE-PASSWORD"

        user?.updatePassword(newPassword)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "User password updated.")
                }
            }
    }

    private fun sendEmailVerification() {
        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        user?.sendEmailVerification()
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Email sent.")
                }
            }
    }

    private fun sendEmailVerificationWithContinueUrl() {
        val auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        val url = "http://www.example.com/verify?uid=" + user?.uid
        val actionCodeSettings = ActionCodeSettings.newBuilder()
            .setUrl(url)
            .setIOSBundleId("com.example.ios")
            // The default for this is populated with the current android package name.
            .setAndroidPackageName("com.example.android", false, null)
            .build()

        user?.sendEmailVerification(actionCodeSettings)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Email sent.")
                }
            }

        auth.setLanguageCode("fr")
        // To apply the default app language instead of explicitly setting it.
        // auth.useAppLanguage()
    }

    private fun sendPasswordReset() {
        val auth = FirebaseAuth.getInstance()
        val emailAddress = "user@example.com"

        auth.sendPasswordResetEmail(emailAddress)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Email sent.")
                }
            }
    }

    private fun deleteUser() {
        val user = FirebaseAuth.getInstance().currentUser

        user?.delete()
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "User account deleted.")
                }
            }
    }

    private fun reauthenticate() {
        val user = FirebaseAuth.getInstance().currentUser

        // Get auth credentials from the user for re-authentication. The example below shows
        // email and password credentials but there are multiple possible providers,
        // such as GoogleAuthProvider or FacebookAuthProvider.
        val credential = EmailAuthProvider
            .getCredential("user@example.com", "password1234")

        // Prompt the user to re-provide their sign-in credentials
        user?.reauthenticate(credential)
            ?.addOnCompleteListener { Log.d(TAG, "User re-authenticated.") }
    }
}
