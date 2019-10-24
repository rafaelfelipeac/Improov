package com.rafaelfelipeac.mountains.features.profileedit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.rafaelfelipeac.mountains.R
import com.rafaelfelipeac.mountains.core.extension.*
import com.rafaelfelipeac.mountains.core.platform.base.BaseFragment
import com.rafaelfelipeac.mountains.features.main.MainActivity
import kotlinx.android.synthetic.main.fragment_profile_edit.*

class ProfileEditFragment : BaseFragment() {

    private val profileEditViewModel by lazy { viewModelFactory.get<ProfileEditViewModel>(this) }

    private var cont = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        injector.inject(this)

        (activity as MainActivity).openToolbar()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //profile_edit_name.setText(userFirebase?.displayName)
        //profile_edit_email.setText(userFirebase?.email)

        observeViewModel()

        profile_edit_save_button.setOnClickListener {
            if (verifyElements()) {
                if (updateName()) {
                    //profileEditViewModel.updateName(profile_edit_name.text.toString())
                    cont++
                }

                if (cont > 0) {
                    showProgressBar()
                    hideSoftKeyboard()
                }
            }
        }

        profile_edit_eye.setOnClickListener {
            profile_edit_password.showOrHidePassword()
            profile_edit_confirm_password.showOrHidePassword()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        (activity as MainActivity).supportActionBar?.title = getString(R.string.profile_edit_title)

        hideNavigation()

        return inflater.inflate(R.layout.fragment_profile_edit, container, false)
    }

    private fun updateName(): Boolean {
        //return userFirebase?.displayName != profile_edit_name.text.toString()
        return false
    }

    private fun verifyElements(): Boolean {
        when {
            profile_edit_email.isEmpty() -> {
                setErrorMessage(getString(R.string.profile_edit_empty_fields))
                return false
            }
            profile_edit_email.emailIsInvalid() -> {
                setErrorMessage(getString(R.string.profile_edit_invalid_email_message))
                return false
            }
            (profile_edit_password.text.toString().isNotEmpty() || profile_edit_confirm_password.text.toString().isNotEmpty()) &&
                    profile_edit_password.text.toString() != profile_edit_confirm_password.text.toString() -> {
                setErrorMessage(getString(R.string.profile_edit_different_passwords))
                return false
            }
            (profile_edit_password.text.toString().isNotEmpty() || profile_edit_confirm_password.text.toString().isNotEmpty()) &&
                    profile_edit_password.text.toString() == profile_edit_confirm_password.text.toString() &&
                    profile_edit_password.text.toString().length < 6 -> {
                setErrorMessage(getString(R.string.profile_edit_min_characters))
                return false
            }
        }

        return true
    }

    private fun observeViewModel() {
//        profileEditViewModel.updateUser.observe(this, Observer { createResult ->
//            when {
//                createResult.isSuccessful -> {
//                    if (--cont == 0) {
//                        hideProgressBar()
//                    }
//
//                    profile_edit_password.resetValue()
//                    profile_edit_confirm_password.resetValue()
//
//                    showSnackBar(getString(R.string.profile_edit_success))
//                }
//                createResult.message == getString(R.string.firebase_result_error_email_already_in_use) -> {
//                    hideProgressBar()
//                    setErrorMessage(getString(R.string.profile_edit_snackbar_error_email_already_in_use))
//                }
//                createResult.message == getString(R.string.firebase_result_error_need_login_again) -> {
//                    hideProgressBar()
//                    setErrorMessage(getString(R.string.profile_edit_need_login_again))
//                }
//                else -> {
//                    hideProgressBar()
//                    setErrorMessage(getString(R.string.empty_string))
//                    showSnackBar(getString(R.string.profile_edit_error))
//                }
//            }
//        })
    }

    private fun setErrorMessage(message: String) {
        profile_edit_error_message.text = message
    }

    private fun showProgressBar() {
        profile_edit_progress_bar.visible()
        profile_edit_eye.gone()
    }

    private fun hideProgressBar() {
        profile_edit_progress_bar.gone()
        profile_edit_eye.visible()
    }
}
