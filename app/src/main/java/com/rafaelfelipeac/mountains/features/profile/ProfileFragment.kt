package com.rafaelfelipeac.mountains.features.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.rafaelfelipeac.mountains.R
import com.rafaelfelipeac.mountains.core.platform.base.BaseFragment
import com.rafaelfelipeac.mountains.features.main.MainActivity
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : BaseFragment() {

    private val profileViewModel by lazy { viewModelFactory.get<ProfileViewModel>(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        injector.inject(this)
    }

    override fun onResume() {
        super.onResume()

        profile_user_name.text = userFirebase?.displayName
        profile_user_email.text = userFirebase?.email

        (activity as MainActivity).closeToolbar()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        (activity as MainActivity).supportActionBar?.title = getString(R.string.profile_title)

        showNavigation()

        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profile_logout_button.setOnClickListener {
            val dialog = ProfileLogoutDialog()

            dialog.setOnClickListener(object : ProfileLogoutDialog.OnClickListener {
                override fun onClickCancel() {
                    dialog.dismiss()
                }

                override fun onClickOK() {
                    dialog.dismiss()
                    logout()
                }
            })

            dialog.show(fragmentManager!!, "tag")
        }

        fab.setOnClickListener {
            navController.navigate(ProfileFragmentDirections.actionNavigationProfileToNavigationAdd())
        }

        profile_edit_profile_button.setOnClickListener {
            navController.navigate(R.id.action_navigation_profile_to_navigation_profile_edit)
        }

        profile_settings_button.setOnClickListener {
            navController.navigate(R.id.action_navigation_profile_to_navigation_settings)
        }
    }

    private fun logout() {
        preferences.login = false
        FirebaseAuth.getInstance().signOut()

        mGoogleSignInClient?.signOut()?.addOnCompleteListener {
            if (it.isSuccessful) {
                navController.navigate(ProfileFragmentDirections.actionNavigationProfileToNavigationWelcome())
            }
        }
    }
}
