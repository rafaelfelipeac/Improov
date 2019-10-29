package com.rafaelfelipeac.mountains.core.di.modules

import androidx.lifecycle.ViewModel
import com.rafaelfelipeac.mountains.core.di.modules.viewModel.ViewModelKey
import com.rafaelfelipeac.mountains.features.profilename.ProfileNameViewModel
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
