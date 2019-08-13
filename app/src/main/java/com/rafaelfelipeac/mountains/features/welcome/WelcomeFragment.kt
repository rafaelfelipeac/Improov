package com.rafaelfelipeac.mountains.features.welcome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.rafaelfelipeac.mountains.R
import com.rafaelfelipeac.mountains.app.prefs
import com.rafaelfelipeac.mountains.core.platform.base.BaseFragment
import com.rafaelfelipeac.mountains.features.main.MainActivity
import kotlinx.android.synthetic.main.fragment_welcome.*

class WelcomeFragment : BaseFragment() {

    private val welcomeViewModel by lazy { viewModelFactory.get<WelcomeViewModel>(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        injector.inject(this)

        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        (activity as MainActivity).supportActionBar?.title = getString(R.string.fragment_welcome_title)

        hideNavigation()

        (activity as MainActivity).closeToolbar()

        return inflater.inflate(R.layout.fragment_welcome, container, false)
    }

    override fun onStart() {
        super.onStart()

        val account = GoogleSignIn.getLastSignedInAccount(context!!)
        if (account != null || prefs.login) {
            navController.navigate(WelcomeFragmentDirections.actionNavigationWelcomeToNavigationList())
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        welcome_sign_in.setOnClickListener {
            navController.navigate(WelcomeFragmentDirections.actionNavigationWelcomeToNavigationLogin())
        }

        welcome_create_account.setOnClickListener {
            navController.navigate(WelcomeFragmentDirections.actionNavigationWelcomeToNavigationCreateUser())
        }

        viewpager.adapter = WelcomeAdapter(fragmentManager!!)
        dots.setupWithViewPager(viewpager, true)
    }
}