package com.rafaelfelipeac.mountains.features.forgotpassword

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.rafaelfelipeac.mountains.R
import com.rafaelfelipeac.mountains.core.extension.emailIsInvalid
import com.rafaelfelipeac.mountains.core.extension.gone
import com.rafaelfelipeac.mountains.core.extension.isEmpty
import com.rafaelfelipeac.mountains.core.extension.visible
import com.rafaelfelipeac.mountains.core.platform.base.BaseFragment
import com.rafaelfelipeac.mountains.features.main.MainActivity
import kotlinx.android.synthetic.main.fragment_forgot_password.*

class ForgotPasswordFragment : BaseFragment() {

    private val forgotPasswordViewModel by lazy { viewModelFactory.get<ForgotPasswordViewModel>(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        injector.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        (activity as MainActivity).supportActionBar?.title = getString(R.string.fragment_forgot_password_title)

        hideNavigation()

        return inflater.inflate(R.layout.fragment_forgot_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()

        forgot_password_send_button.setOnClickListener {
            if (verifyElements()) {
                showProgressBar()
                forgotPasswordViewModel.resetPassword(forgot_password_email.text.toString())
                hideSoftKeyboard()
            }
        }
    }

    private fun observeViewModel() {
        forgotPasswordViewModel.forgotResult.observe(this, Observer { sendResult ->
            hideProgressBar()

            when {
                sendResult.isSuccessful -> {
                    setErrorMessage(getString(R.string.empty_string))
                    showSnackBar(getString(R.string.snackbar_success_send_email))
                }
                sendResult.message.contains(getString(R.string.result_error_message_no_user)) -> {
                    setErrorMessage(getString(R.string.error_message_email_not_registered))
                }
                else -> {
                    setErrorMessage(getString(R.string.empty_string))
                    showSnackBar(getString(R.string.snackbar_error_send_email))
                }
            }
        })
    }

    private fun verifyElements(): Boolean {
        when {
            forgot_password_email.isEmpty() -> {
                setErrorMessage(getString(R.string.error_message_email))
                return false
            }
            forgot_password_email.emailIsInvalid() -> {
                setErrorMessage(getString(R.string.error_message_invalid_email))
                return false
            }
        }

        return true
    }

    private fun setErrorMessage(message: String) {
        forgot_password_error_message.text = message
    }

    private fun showProgressBar() {
        forgot_progress_bar.visible()
    }

    private fun hideProgressBar() {
        forgot_progress_bar.gone()
    }
}
