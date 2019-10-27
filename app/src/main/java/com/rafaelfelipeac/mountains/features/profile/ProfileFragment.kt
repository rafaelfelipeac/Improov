package com.rafaelfelipeac.mountains.features.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
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

        profile_user_name.text = preferences.name

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

        profile_show_welcome_button.setOnClickListener {
            preferences.welcome = false

            navController.navigate(ProfileFragmentDirections.actionNavigationProfileToNavigationWelcome())
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
}
