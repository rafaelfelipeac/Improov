package com.rafaelfelipeac.mountains.ui.fragments.editProfile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.auth.FirebaseAuth
import com.rafaelfelipeac.mountains.R
import com.rafaelfelipeac.mountains.extension.*
import com.rafaelfelipeac.mountains.ui.activities.MainActivity
import com.rafaelfelipeac.mountains.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_edit_profile.*

class EditProfileFragment : BaseFragment() {

    private lateinit var viewModel: EditProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (activity as MainActivity).openToolbar()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // update 'user' when open GoalsFragment and on success from update

        edit_profile_name.setText(FirebaseAuth.getInstance().currentUser?.displayName)
        edit_profile_email.setText(FirebaseAuth.getInstance().currentUser?.email)

        observeViewModel()

        edit_profile_create_button.setOnClickListener {
            if (verifyElements()) {


                if (updateName()) {
                    viewModel.updateName(edit_profile_name.text.toString())
                    showProgressBar()
                }
                if (updatePassword()) {
                    viewModel.updatePassword(edit_profile_password.text.toString())
                    showProgressBar()
                }
                if (updateEmail()) {
                    viewModel.updateEmail(edit_profile_email.text.toString())
                    showProgressBar()
                }
            }
        }

        edit_profile_eye.setOnClickListener {
            edit_profile_password.showOrHidePassword()
            edit_profile_confirm_password.showOrHidePassword()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        (activity as MainActivity).supportActionBar?.title = getString(R.string.fragment_edit_profile_title)

        viewModel = ViewModelProviders.of(this).get(EditProfileViewModel::class.java)

        hideNavigation()

        return inflater.inflate(R.layout.fragment_edit_profile, container, false)
    }


    private fun updateEmail(): Boolean {
      return user.email != edit_profile_email.text.toString()
    }

    private fun updatePassword(): Boolean {
        return edit_profile_password.text.toString().isNotEmpty() && edit_profile_confirm_password.text.toString().isNotEmpty() &&
                edit_profile_password.text.toString() == edit_profile_confirm_password.text.toString() &&
                edit_profile_password.text.toString().length >= 6
    }

    private fun updateName(): Boolean {
        return user.name != edit_profile_name.text.toString()
    }

    private fun verifyElements(): Boolean {
        when {
            edit_profile_email.isEmpty() -> {
                setErrorMessage(getString(R.string.error_message_empty_fields))
                return false
            }
            edit_profile_email.emailIsInvalid() -> {
                setErrorMessage(getString(R.string.error_message_invalid_email))
                return false
            }
            (edit_profile_password.text.toString().isNotEmpty() || edit_profile_confirm_password.text.toString().isNotEmpty()) &&
                    edit_profile_password.text.toString() != edit_profile_confirm_password.text.toString() -> {
                setErrorMessage(getString(R.string.error_message_different_passwords))
                return false
            }
            (edit_profile_password.text.toString().isNotEmpty() || edit_profile_confirm_password.text.toString().isNotEmpty()) &&
                    edit_profile_password.text.toString() == edit_profile_confirm_password.text.toString() &&
                    edit_profile_password.text.toString().length < 6 -> {
                setErrorMessage(getString(R.string.error_message_min_characters))
                return false
            }
        }

        return true
    }

    private fun observeViewModel() {
        viewModel.updateUser.observe(this, Observer { createResult ->
            hideProgressBar()

            when {
                createResult.isSuccessful -> {

                }
                createResult.message == getString(R.string.result_error_message_email_already_in_use) -> {
                    setErrorMessage(getString(R.string.snackbar_error_email_already_in_use))
                }
                else -> {
                    setErrorMessage(getString(R.string.empty_string))
                    showSnackBar(getString(R.string.snackbar_error_create_user))
                }
            }
        })
    }

    private fun setErrorMessage(message: String) {
        edit_profile_error_message.text = message
    }

    private fun showProgressBar() {
        edit_profile_progress_bar.visible()
        edit_profile_eye.gone()
    }

    private fun hideProgressBar() {
        edit_profile_progress_bar.gone()
        edit_profile_eye.visible()
    }
}
