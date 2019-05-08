package com.rafaelfelipeac.mountains.ui.fragments.welcome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rafaelfelipeac.mountains.R
import com.rafaelfelipeac.mountains.ui.activities.MainActivity
import com.rafaelfelipeac.mountains.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_welcome.*

class WelcomeFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (activity as MainActivity).supportActionBar?.title = ""

        hideNavigation()

        return inflater.inflate(R.layout.fragment_welcome, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        welcome_sign_in.setOnClickListener {
            navController.navigate(WelcomeFragmentDirections.actionNavigationWelcomeToNavigationLogin())
        }

        welcome_create_account.setOnClickListener {
            navController.navigate(WelcomeFragmentDirections.actionNavigationWelcomeToNavigationCreateUser())
        }
    }
}
