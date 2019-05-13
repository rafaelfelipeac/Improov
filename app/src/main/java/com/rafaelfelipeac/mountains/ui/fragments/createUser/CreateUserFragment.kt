package com.rafaelfelipeac.mountains.ui.fragments.createUser

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.rafaelfelipeac.mountains.R
import com.rafaelfelipeac.mountains.app.prefs
import com.rafaelfelipeac.mountains.extension.emailIsInvalid
import com.rafaelfelipeac.mountains.extension.isEmpty
import com.rafaelfelipeac.mountains.ui.activities.MainActivity
import com.rafaelfelipeac.mountains.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_create_user.*

class CreateUserFragment : BaseFragment() {

    private lateinit var viewModel: CreateUserViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        (activity as MainActivity).supportActionBar?.title = getString(R.string.fragment_create_user_title)

        viewModel = ViewModelProviders.of(this).get(CreateUserViewModel::class.java)

        hideNavigation()

        return inflater.inflate(R.layout.fragment_create_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()

        create_user_create_button.setOnClickListener {
            if (verifyElements()) {
                viewModel.createUser(create_user_email.text.toString(), create_user_password.text.toString())
            }
        }
    }

    private fun observeViewModel() {
        viewModel.createResult.observe(this, Observer { createResult ->
            when {
                createResult.isSuccessful -> {
                    prefs.login = true
                    navController.navigate(CreateUserFragmentDirections.actionNavigationCreateUserToNavigationGoals())
                }
                createResult.message == getString(R.string.result_error_message_email_already_in_use) -> {
                    showSnackBar(getString(R.string.snackbar_error_email_already_in_use))
                }
                else -> {
                    setErrorMessage(getString(R.string.empty_string))
                    showSnackBar(getString(R.string.snackbar_error_create_user))
                }
            }
        })
    }

    private fun verifyElements(): Boolean {
        when {
            create_user_name.isEmpty() || create_user_email.isEmpty() ||
                    create_user_password.isEmpty() || create_user_confirm_password.isEmpty() -> {
                setErrorMessage(getString(R.string.error_message_empty_fields))
                return false
            }
            create_user_email.emailIsInvalid() -> {
                setErrorMessage(getString(R.string.error_message_invalid_email))
                return false
            }
            create_user_password.text.toString() != create_user_confirm_password.text.toString() -> {
                setErrorMessage(getString(R.string.error_message_different_passwords))
                return false
            }
            create_user_password.text.toString() == create_user_confirm_password.text.toString() &&
                    create_user_password.text.toString().length < 6 -> {
                setErrorMessage(getString(R.string.error_message_min_characters))
                return false
            }
        }

        return true
    }

    private fun setErrorMessage(message: String) {
        create_user_error_message.text = message
    }
}
