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
import com.google.android.gms.common.api.ApiException
import com.rafaelfelipeac.mountains.R
import com.rafaelfelipeac.mountains.app.prefs
import com.rafaelfelipeac.mountains.extension.*
import com.rafaelfelipeac.mountains.ui.activities.MainActivity
import com.rafaelfelipeac.mountains.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : BaseFragment() {

    private val GOOGLE_SIGN_IN = 100

    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        injector.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        (activity as MainActivity).supportActionBar?.title = getString(R.string.fragment_login_title)

        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)

        hideNavigation()
        (activity as MainActivity).openToolbar()

        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()

        login_sign_in_google_button.setOnClickListener {
            signInGoogle()
        }

        login_sign_in_button.setOnClickListener {
            if (verifyElements()) {
                showProgressBar()
                viewModel.signIn(login_email.text.toString(), login_password.text.toString())
            }
        }

        login_forgot_password.setOnClickListener {
            navController.navigate(LoginFragmentDirections.actionNavigationLoginToNavigationForgotPassword())
        }

        login_eye.setOnClickListener {
            login_password.showOrHidePassword()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GOOGLE_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)

            try {
                val account = task.getResult(ApiException::class.java)!!

                showProgressBar()

                viewModel.signInGoogle(account)
            } catch (e: ApiException) {
                Log.w(TAG, "Google sign in failed", e)
            }
        }
    }

    private fun observeViewModel() {
        viewModel.loginResult.observe(this, Observer { loginResult ->
            hideProgressBar()

            when {
                loginResult.isSuccessful -> {
                    prefs.login = true
                    navController.navigate(LoginFragmentDirections.actionNavigationLoginToNavigationList())
                }
                loginResult.message.contains(getString(R.string.result_error_message_invalid_password)) -> {
                    setErrorMessage(getString(R.string.error_message_wrong_password))
                }
                loginResult.message.contains(getString(R.string.result_error_message_no_user)) -> {
                    setErrorMessage(getString(R.string.error_message_user_not_registered))
                }
                else -> {
                    setErrorMessage(getString(R.string.empty_string))
                    showSnackBar(getString(R.string.snackbar_error_login))
                }
            }
        })
    }

    private fun verifyElements(): Boolean {
        when {
            login_email.isEmpty() || login_password.isEmpty() -> {
                setErrorMessage(getString(R.string.error_message_email_password))
                return false
            }
            login_email.emailIsInvalid() -> {
                setErrorMessage(getString(R.string.error_message_invalid_email))
                return false
            }
            login_password.text.toString().length < 6 -> {
                setErrorMessage(getString(R.string.error_message_min_characters))
                return false
            }
        }

        return true
    }

    private fun signInGoogle() {
        val signInIntent = mGoogleSignInClient?.signInIntent
        startActivityForResult(signInIntent, GOOGLE_SIGN_IN)
    }

    private fun setErrorMessage(message: String) {
        login_sign_in_error_message.text = message
    }

    private fun showProgressBar() {
        login_progress_bar.visible()
        login_eye.gone()
    }

    private fun hideProgressBar() {
        login_progress_bar.gone()
        login_eye.visible()
    }
}

