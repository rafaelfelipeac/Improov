package com.rafaelfelipeac.mountains.ui.fragments.forgotPassword

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.rafaelfelipeac.mountains.R
import com.rafaelfelipeac.mountains.extension.emailIsInvalid
import com.rafaelfelipeac.mountains.extension.isEmpty
import com.rafaelfelipeac.mountains.extension.visible
import com.rafaelfelipeac.mountains.ui.activities.MainActivity
import com.rafaelfelipeac.mountains.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_forgot_password.*

class ForgotPasswordFragment : BaseFragment() {

    private lateinit var viewModel: ForgotPasswordViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        (activity as MainActivity).supportActionBar?.title = getString(R.string.fragment_forgot_password_title)

        viewModel = ViewModelProviders.of(this).get(ForgotPasswordViewModel::class.java)

        hideNavigation()

        return inflater.inflate(R.layout.fragment_forgot_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()

        forgot_password_send_button.setOnClickListener {
            // verificar email
            // verificar se email existe

            if (verifyElements()) {
                viewModel.resetPassword(forgot_password_email.text.toString())
            } else {
                forgot_password_error_message.visible()
            }
        }
    }

    private fun verifyElements(): Boolean {
        // email nao é vazio
        // email é valido
        // email existe no sistema

        when {
            forgot_password_email.isEmpty() -> {
                forgot_password_error_message.text = "email vazio"
                return false
            }
            forgot_password_email.emailIsInvalid()  -> {
                forgot_password_error_message.text =  "email invalido"
                return false
            }
            emailExists() -> {
                forgot_password_error_message.text = "email nao existe"
                return false
            }
        }

        return true
    }

    private fun emailExists(): Boolean {
        return false
    }

    private fun observeViewModel() {
        viewModel.emailSent.observe(this, Observer { emailSent ->
            if(emailSent) {
                showSnackBar("Email enviado.")
            } else {
                showSnackBar("sent error")
            }
        })
    }
}
