package com.rafaelfelipeac.improov.features.profile.presentation.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rafaelfelipeac.improov.R
import com.rafaelfelipeac.improov.core.extension.gone
import com.rafaelfelipeac.improov.core.extension.observe
import com.rafaelfelipeac.improov.core.extension.visible
import com.rafaelfelipeac.improov.core.platform.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : BaseFragment() {

    private val viewModel by lazy { viewModelFactory.get<ProfileViewModel>(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        injector.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        setScreen()

        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.loadData()

        setupLayout()
        observeViewModel()
    }

    private fun setScreen() {
        hideToolbar()
        showNavigation()
        hideBottomSheetTips()
    }

    private fun setupLayout() {
        fab.setOnClickListener {
            hideBottomSheetTips()

            navController.navigate(ProfileFragmentDirections.profileToGoalForm())
        }

        profile_show_welcome_button.setOnClickListener {
            viewModel.saveWelcome(false)
            viewModel.saveFirstTimeAdd(true)
            viewModel.saveFirstTimeList(false)
        }

        profile_edit_profile_button.setOnClickListener {
            navController.navigate(ProfileFragmentDirections.profileToProfileEdit())
        }

        profile_settings_button.setOnClickListener {
            navController.navigate(ProfileFragmentDirections.profileToSettings())
        }
    }

    private fun observeViewModel() {
        viewModel.saved.observe(this) {
            navController.navigate(ProfileFragmentDirections.profileToWelcome())
        }

        viewModel.name.observe(this) {
            if (it.isNotEmpty()) {
                profile_user_name.text = it
                profile_user_name.visible()
                profile_edit_profile_button.text = getString(R.string.profile_edit_name_message)
            } else {
                profile_user_name.gone()
                profile_edit_profile_button.text = getString(R.string.profile_add_name_message)
            }
        }
    }
}
