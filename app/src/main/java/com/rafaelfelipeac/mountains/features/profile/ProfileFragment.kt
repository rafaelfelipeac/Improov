package com.rafaelfelipeac.mountains.features.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.rafaelfelipeac.mountains.BuildConfig
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

        profile_name.text = userFirebase?.displayName
        profile_email.text = userFirebase?.email

        (activity as MainActivity).closeToolbar()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        (activity as MainActivity).supportActionBar?.title = getString(R.string.fragment_profile_title)

        showNavigation()

        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profile_logout_button.setOnClickListener {
            val dialog = LogoutDialogFragment()

            dialog.setOnClickListener(object : LogoutDialogFragment.OnClickListener {
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

        profile_version.text = String.format("%s", "Versão: " + BuildConfig.VERSION_NAME)

        fab.setOnClickListener {
            navController.navigate(ProfileFragmentDirections.actionNavigationProfileToNavigationAdd())
        }

        profile_edit_profile.setOnClickListener {
            navController.navigate(R.id.action_navigation_profile_to_navigation_profile_edit)
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
