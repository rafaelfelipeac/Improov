package com.rafaelfelipeac.improov.features.profile.presentation.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.rafaelfelipeac.improov.R
import com.rafaelfelipeac.improov.core.extension.invisible
import com.rafaelfelipeac.improov.core.extension.viewBinding
import com.rafaelfelipeac.improov.core.extension.visible
import com.rafaelfelipeac.improov.core.platform.base.BaseFragment
import com.rafaelfelipeac.improov.databinding.FragmentProfileBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ProfileFragment : BaseFragment() {

    private val viewModel by lazy { viewModelProvider.profileViewModel() }

    private var binding by viewBinding<FragmentProfileBinding>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        setScreen()

        return FragmentProfileBinding.inflate(inflater, container, false).run {
            binding = this
            binding.root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.loadData()

        setupBehaviours()
        observeViewModel()
    }

    private fun setScreen() {
        hideToolbar()
        showNavigation()
        hideBottomSheetTips()
    }

    private fun setupBehaviours() {
        fab.setOnClickListener {
            hideBottomSheetTips()

            navController.navigate(ProfileFragmentDirections.profileToGoalForm())
        }

        binding.profileShowWelcomeButton.setOnClickListener {
            viewModel.saveWelcome(false)
            viewModel.saveFirstTimeAdd(true)
            viewModel.saveFirstTimeList(false)
        }

        binding.profileBackup.setOnClickListener {
            navController.navigate(ProfileFragmentDirections.profileToBackup())
        }

        binding.profileEditProfileButton.setOnClickListener {
            navController.navigate(ProfileFragmentDirections.profileToProfileEdit())
        }

        binding.profileSettingsButton.setOnClickListener {
            navController.navigate(ProfileFragmentDirections.profileToSettings())
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.saved.collect {
                if (navController.currentDestination?.id == R.id.navigationProfile) {
                    navController.navigate(ProfileFragmentDirections.profileToWelcome())
                }
            }
        }

        lifecycleScope.launch {
            viewModel.name.collect {
                if (it.isNotEmpty()) {
                    binding.profileUserName.text = it
                    binding.profileUserName.visible()
                    binding.profileEditProfileButton.text = getString(R.string.profile_edit_name_message)
                } else {
                    binding.profileUserName.invisible()
                    binding.profileEditProfileButton.text = getString(R.string.profile_add_name_message)
                }
            }
        }
    }
}
