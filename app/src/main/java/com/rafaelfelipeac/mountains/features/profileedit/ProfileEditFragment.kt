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

        profile_edit_name.setText(userFirebase?.displayName)
        profile_edit_email.setText(userFirebase?.email)

        observeViewModel()

        profile_edit_create_button.setOnClickListener {
            if (verifyElements()) {
                if (updateName()) {
                    profileEditViewModel.updateName(profile_edit_name.text.toString())
                    cont++
                }
                if (updatePassword()) {
                    profileEditViewModel.updatePassword(profile_edit_password.text.toString())
                    cont++
                }
                if (updateEmail()) {
                    profileEditViewModel.updateEmail(profile_edit_email.text.toString())
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
        (activity as MainActivity).supportActionBar?.title = getString(R.string.fragment_edit_profile_title)

        hideNavigation()

        return inflater.inflate(R.layout.fragment_profile_edit, container, false)
    }

    private fun updateEmail(): Boolean {
      return userFirebase?.email != profile_edit_email.text.toString()
    }

    private fun updatePassword(): Boolean {
        return profile_edit_password.text.toString().isNotEmpty() && profile_edit_confirm_password.text.toString().isNotEmpty() &&
                profile_edit_password.text.toString() == profile_edit_confirm_password.text.toString() &&
                profile_edit_password.text.toString().length >= 6
    }

    private fun updateName(): Boolean {
        return userFirebase?.displayName != profile_edit_name.text.toString()
    }

    private fun verifyElements(): Boolean {
        when {
            profile_edit_email.isEmpty() -> {
                setErrorMessage(getString(R.string.error_message_empty_fields))
                return false
            }
            profile_edit_email.emailIsInvalid() -> {
                setErrorMessage(getString(R.string.error_message_invalid_email))
                return false
            }
            (profile_edit_password.text.toString().isNotEmpty() || profile_edit_confirm_password.text.toString().isNotEmpty()) &&
                    profile_edit_password.text.toString() != profile_edit_confirm_password.text.toString() -> {
                setErrorMessage(getString(R.string.error_message_different_passwords))
                return false
            }
            (profile_edit_password.text.toString().isNotEmpty() || profile_edit_confirm_password.text.toString().isNotEmpty()) &&
                    profile_edit_password.text.toString() == profile_edit_confirm_password.text.toString() &&
                    profile_edit_password.text.toString().length < 6 -> {
                setErrorMessage(getString(R.string.error_message_min_characters))
                return false
            }
        }

        return true
    }

    private fun observeViewModel() {
        profileEditViewModel.updateUser.observe(this, Observer { createResult ->
            when {
                createResult.isSuccessful -> {
                    if (--cont == 0) {
                        hideProgressBar()
                    }

                    profile_edit_password.setText("")
                    profile_edit_confirm_password.setText("")

                    showSnackBar("Perfil editado.")
                }
                createResult.message == getString(R.string.result_error_message_email_already_in_use) -> {
                    hideProgressBar()
                    setErrorMessage(getString(R.string.snackbar_error_email_already_in_use))
                }
                createResult.message == "This operation is sensitive and requires recent authentication. Log in again before retrying this request." -> {
                    hideProgressBar()
                    setErrorMessage("Por questões de segurança, você precisa deslogar e logar novamente no app antes de fazer essa mudança.")
                }
                else -> {
                    hideProgressBar()
                    setErrorMessage(getString(R.string.empty_string))
                    showSnackBar("Erro ao atualizar informações do usuário.")
                }
            }
        })
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
