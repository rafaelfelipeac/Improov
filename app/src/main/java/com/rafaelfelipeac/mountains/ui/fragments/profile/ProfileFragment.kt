package com.rafaelfelipeac.mountains.ui.fragments.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.rafaelfelipeac.mountains.R
import com.rafaelfelipeac.mountains.app.prefs
import com.rafaelfelipeac.mountains.ui.activities.MainActivity
import com.rafaelfelipeac.mountains.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : BaseFragment() {

    override fun onResume() {
        super.onResume()

        profile_name.text = userFirebase?.displayName
        profile_email.text = userFirebase?.email

        (activity as MainActivity).closeToolbar()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        (activity as MainActivity).supportActionBar?.title = getString(R.string.fragment_profile_title)

        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showNavigation()

        profile_logout_button.setOnClickListener {
            showDialogWithAction("Tem certeza que deseja sair do app?", ::logout)
        }

        profile_edit_profile.setOnClickListener {
            navController.navigate(R.id.action_navigation_profile_to_editProfileFragment)
        }
    }

    private fun logout() {
        prefs.login = false
        FirebaseAuth.getInstance().signOut()

        mGoogleSignInClient?.signOut()?.addOnCompleteListener {
            if (it.isSuccessful) {
                navController.navigate(ProfileFragmentDirections.actionNavigationProfileToNavigationWelcome())
            }
        }
    }
}
