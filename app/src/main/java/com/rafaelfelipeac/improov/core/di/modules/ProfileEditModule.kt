package com.rafaelfelipeac.improov.core.di.modules

import androidx.lifecycle.ViewModel
import com.rafaelfelipeac.improov.core.di.modules.viewModel.ViewModelKey
import com.rafaelfelipeac.improov.features.profilename.ProfileNameViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ProfileEditModule {

    @Binds
    @IntoMap
    @ViewModelKey(ProfileNameViewModel::class)
    abstract fun bindProfileEditViewModel(profileNameViewModel: ProfileNameViewModel): ViewModel
}
