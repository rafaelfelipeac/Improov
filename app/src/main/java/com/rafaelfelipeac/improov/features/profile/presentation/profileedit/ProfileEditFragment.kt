package com.rafaelfelipeac.improov.features.profile.presentation.profileedit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.rafaelfelipeac.improov.R
import com.rafaelfelipeac.improov.core.extension.checkIfFieldIsEmptyOrZero
import com.rafaelfelipeac.improov.core.extension.fieldIsEmptyOrZero
import com.rafaelfelipeac.improov.core.extension.observe
import com.rafaelfelipeac.improov.core.platform.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_profile_edit.*

class ProfileEditFragment : BaseFragment() {

    private val viewModel by lazy { viewModelFactory.get<ProfileEditViewModel>(this) }

    private val stateObserver = Observer<ProfileEditViewModel.ViewState> { response ->
        profile_edit_name.setText(response.name)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        injector.inject(this)

        main.openToolbar()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observe(viewModel.stateLiveData, stateObserver)
        viewModel.loadData()

        profile_edit_save_button.setOnClickListener {
            when {
                verifyElements() -> {
                }
                else -> {
                    hideSoftKeyboard()

                    viewModel.onSaveName(profile_edit_name.text.toString())
                    navController.navigateUp()
                }
            }
        }
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

    private fun observeViewModel() {
        TODO("Not yet implemented")
    }

    private fun setErrorMessage(message: String) {
        profile_edit_error_message.text = message
    }
}
