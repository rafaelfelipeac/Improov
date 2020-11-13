package com.rafaelfelipeac.improov.features.profile.presentation.profileedit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rafaelfelipeac.improov.R
import com.rafaelfelipeac.improov.core.extension.isEmptyOrZero
import com.rafaelfelipeac.improov.core.extension.focusOnEmptyOrZero
import com.rafaelfelipeac.improov.core.extension.observe
import com.rafaelfelipeac.improov.core.extension.viewBinding
import com.rafaelfelipeac.improov.core.platform.base.BaseFragment
import com.rafaelfelipeac.improov.databinding.FragmentProfileEditBinding

class ProfileEditFragment : BaseFragment() {

    private val viewModel by lazy { viewModelProvider.profileEditViewModel() }

    private var binding by viewBinding<FragmentProfileEditBinding>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        setScreen()

        return FragmentProfileEditBinding.inflate(inflater, container, false).run {
            binding = this
            binding.root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.loadData()

        setupLayout()
        observeViewModel()
    }

    private fun setScreen() {
        setTitle(getString(R.string.profile_edit_title))
        showBackArrow()
        hideNavigation()
    }

    private fun setupLayout() {
        binding.profileEditSaveButton.setOnClickListener {
            when {
                verifyElements() -> {
                }
                else -> {
                    hideSoftKeyboard()
                    viewModel.saveName(binding.profileEditName.text.toString())
                }
            }
        }
    }

    private fun observeViewModel() {
        viewModel.saved.observe(this) {
            navController.navigateUp()
        }

        viewModel.name.observe(this) {
            binding.profileEditName.setText(it)
        }
    }

    private fun verifyElements(): Boolean {
        return when {
            binding.profileEditName.isEmptyOrZero() -> {
                binding.profileEditName.focusOnEmptyOrZero(this, false)

                setErrorMessage(getString(R.string.profile_edit_empty_fields))
                true
            }
            else -> false
        }
    }

    private fun setErrorMessage(message: String) {
        binding.profileEditErrorMessage.text = message
    }
}
