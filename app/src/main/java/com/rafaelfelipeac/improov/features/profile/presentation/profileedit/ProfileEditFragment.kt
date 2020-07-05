package com.rafaelfelipeac.improov.features.profile.presentation.profileedit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rafaelfelipeac.improov.R
import com.rafaelfelipeac.improov.core.extension.checkIfFieldIsEmptyOrZero
import com.rafaelfelipeac.improov.core.extension.fieldIsEmptyOrZero
import com.rafaelfelipeac.improov.core.extension.observe
import com.rafaelfelipeac.improov.core.platform.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_profile_edit.*

class ProfileEditFragment : BaseFragment() {

    private val viewModel by lazy { viewModelFactory.get<ProfileEditViewModel>(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        injector.inject(this)

        main.openToolbar()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        main.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        main.supportActionBar?.title = getString(R.string.profile_edit_title)

        hideNavigation()

        return inflater.inflate(R.layout.fragment_profile_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.loadData()

        setupLayout()
        observeViewModel()
    }

    private fun setupLayout() {
        profile_edit_save_button.setOnClickListener {
            when {
                verifyElements() -> {
                }
                else -> {
                    hideSoftKeyboard()
                    viewModel.saveName(profile_edit_name.text.toString())
                }
            }
        }
    }

    private fun observeViewModel() {
        viewModel.saved.observe(this) {
            navController.navigateUp()
        }

        viewModel.name.observe(this) {
            profile_edit_name.setText(it)
        }
    }

    private fun verifyElements(): Boolean {
        return when {
            profile_edit_name.checkIfFieldIsEmptyOrZero() -> {
                profile_edit_name.fieldIsEmptyOrZero(this, false)

                setErrorMessage(getString(R.string.profile_edit_empty_fields))
                true
            }
            else -> false
        }
    }

    private fun setErrorMessage(message: String) {
        profile_edit_error_message.text = message
    }
}
