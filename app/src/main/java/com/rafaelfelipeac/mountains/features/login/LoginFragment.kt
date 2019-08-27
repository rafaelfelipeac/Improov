package com.rafaelfelipeac.mountains.features.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.rafaelfelipeac.mountains.R
import com.rafaelfelipeac.mountains.core.extension.*
import com.rafaelfelipeac.mountains.core.platform.base.BaseFragment
import com.rafaelfelipeac.mountains.features.main.MainActivity
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : BaseFragment() {

    private val GOOGLE_SIGN_IN = 100

    private val loginViewModel by lazy { viewModelFactory.get<LoginViewModel>(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        injector.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        (activity as MainActivity).supportActionBar?.title = getString(R.string.login_title)

        hideNavigation()

        (activity as MainActivity).openToolbar()

        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()

        login_google_sign_in_button.setOnClickListener {
            signInGoogle()
        }

        login_sign_in_button.setOnClickListener {
            if (verifyElements()) {
                showProgressBar()
                loginViewModel.signIn(login_email.text.toString(), login_password.text.toString())
                hideSoftKeyboard()
            }
        }

        login_forgot_password_button.setOnClickListener {
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

                loginViewModel.signInGoogle(account)
            } catch (e: ApiException) {
                setErrorMessage(getString(R.string.login_google_failed_message))
            }
        }
    }

    private fun observeViewModel() {
        loginViewModel.loginResult.observe(this, Observer { loginResult ->
            hideProgressBar()

            when {
                loginResult.isSuccessful -> {
                    preferences.login = true
                    navController.navigate(LoginFragmentDirections.actionNavigationLoginToNavigationList())
                }
                loginResult.message.contains(getString(R.string.firebase_result_error_invalid_password)) -> {
                    setErrorMessage(getString(R.string.login_wrong_password_message))
                }
                loginResult.message.contains(getString(R.string.firebase_result_error_no_user)) -> {
                    setErrorMessage(getString(R.string.login_user_not_registered_message))
                }
                else -> {
                    setErrorMessage(getString(R.string.empty_string))
                    showSnackBar(getString(R.string.login_error_message))
                }
            }
        })
    }

    private fun verifyElements(): Boolean {
        when {
            login_email.isEmpty() || login_password.isEmpty() -> {
                setErrorMessage(getString(R.string.login_email_password_message))
                return false
            }
            login_email.emailIsInvalid() -> {
                setErrorMessage(getString(R.string.login_invalid_email_message))
                return false
            }
            login_password.text.toString().length < 6 -> {
                setErrorMessage(getString(R.string.login_min_characters_message))
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

