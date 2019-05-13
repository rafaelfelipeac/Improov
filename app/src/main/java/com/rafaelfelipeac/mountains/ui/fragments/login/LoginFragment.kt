package com.rafaelfelipeac.mountains.ui.fragments.login

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.rafaelfelipeac.mountains.R
import com.rafaelfelipeac.mountains.extension.visible
import com.rafaelfelipeac.mountains.ui.activities.MainActivity
import com.rafaelfelipeac.mountains.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : BaseFragment() {

    private var mGoogleSignInClient: GoogleSignInClient? = null

    private val GOOGLE_SIGN_IN = 100

    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupGoogleClient()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        (activity as MainActivity).supportActionBar?.title = getString(R.string.fragment_login_title)

        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)

        hideNavigation()

        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()

        login_sign_in_google_button.setOnClickListener {
            signInGoogle()
        }

        login_sign_in_button.setOnClickListener {
            // verificar email
            // verificar se email existe
            // verificar senha (min)

            if (verifyEmailAndPassword()) {
                viewModel.signIn(login_email.text.toString(), login_password.text.toString())
            } else {
                login_sign_in_error_message.visible()
            }
        }

        login_forgot_password.setOnClickListener {
            navController.navigate(LoginFragmentDirections.actionNavigationLoginToForgotPasswordFragment())
        }
    }

    private fun verifyEmailAndPassword(): Boolean {
        return login_email.text.toString().isNotEmpty() && login_password.text.toString().isNotEmpty()
    }

    private fun observeViewModel() {
        viewModel.loginSuccess.observe(this, Observer { loginResult ->
            if (loginResult) {
                navController.navigate(LoginFragmentDirections.actionNavigationLoginToNavigationGoals())
            } else {
                login_sign_in_error_message.visible()
                showSnackBar("loginError")
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GOOGLE_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)

            try {
                val account = task.getResult(ApiException::class.java)!!

                viewModel.signInGoogle(account)
            } catch (e: ApiException) {
                Log.w(TAG, "Google sign in failed", e)
            }
        }
    }

    private fun setupGoogleClient() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(context!!, gso)
    }

    private fun signInGoogle() {
        val signInIntent = mGoogleSignInClient?.signInIntent
        startActivityForResult(signInIntent, GOOGLE_SIGN_IN)
    }
}
