package com.rafaelfelipeac.improov.core.di.modules

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.rafaelfelipeac.improov.core.di.modules.viewModel.FragmentKey
import com.rafaelfelipeac.improov.core.di.modules.viewModel.ViewModelKey
import com.rafaelfelipeac.improov.features.profile.presentation.profile.ProfileFragment
import com.rafaelfelipeac.improov.features.profile.presentation.profile.ProfileViewModel
import com.rafaelfelipeac.improov.features.profile.presentation.profilename.ProfileNameFragment
import com.rafaelfelipeac.improov.features.profile.presentation.profilename.ProfileNameViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ProfileModule {

    @Binds
    @IntoMap
    @FragmentKey(ProfileFragment::class)
    abstract fun bindProfileFragment(profileFragment: ProfileFragment): Fragment

    @Binds
    @IntoMap
    @ViewModelKey(ProfileViewModel::class)
    abstract fun bindProfileViewModel(profileViewModel: ProfileViewModel): ViewModel

    @Binds
    @IntoMap
    @FragmentKey(ProfileNameFragment::class)
    abstract fun bindProfileNameFragment(profileNameFragment: ProfileNameFragment): Fragment

    @Binds
    @IntoMap
    @ViewModelKey(ProfileNameViewModel::class)
    abstract fun bindProfileEditViewModel(profileNameViewModel: ProfileNameViewModel): ViewModel
}
