package com.rafaelfelipeac.mountains.features.editprofile

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
import kotlinx.android.synthetic.main.fragment_edit_profile.*

class EditProfileFragment : BaseFragment() {

    private val editProfileViewModel by lazy { viewModelFactory.get<EditProfileViewModel>(this) }

    private var cont = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        injector.inject(this)

        (activity as MainActivity).openToolbar()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        edit_profile_name.setText(userFirebase?.displayName)
        edit_profile_email.setText(userFirebase?.email)

        observeViewModel()

        edit_profile_create_button.setOnClickListener {
            if (verifyElements()) {
                if (updateName()) {
                    editProfileViewModel.updateName(edit_profile_name.text.toString())
                    cont++
                }
                if (updatePassword()) {
                    editProfileViewModel.updatePassword(edit_profile_password.text.toString())
                    cont++
                }
                if (updateEmail()) {
                    editProfileViewModel.updateEmail(edit_profile_email.text.toString())
                    cont++
                }

                if (cont > 0) {
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

        hideNavigation()

        return inflater.inflate(R.layout.fragment_edit_profile, container, false)
    }

    private fun updateEmail(): Boolean {
      return userFirebase?.email != edit_profile_email.text.toString()
    }

    private fun updatePassword(): Boolean {
        return edit_profile_password.text.toString().isNotEmpty() && edit_profile_confirm_password.text.toString().isNotEmpty() &&
                edit_profile_password.text.toString() == edit_profile_confirm_password.text.toString() &&
                edit_profile_password.text.toString().length >= 6
    }

    private fun updateName(): Boolean {
        return userFirebase?.displayName != edit_profile_name.text.toString()
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
        editProfileViewModel.updateUser.observe(this, Observer { createResult ->
            when {
                createResult.isSuccessful -> {
                    if (--cont == 0) {
                        hideProgressBar()
                    }

                    edit_profile_password.setText("")
                    edit_profile_confirm_password.setText("")

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
