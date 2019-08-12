package com.rafaelfelipeac.mountains.di.modules

import androidx.lifecycle.ViewModel
import com.rafaelfelipeac.mountains.di.modules.viewModel.ViewModelKey
import com.rafaelfelipeac.mountains.ui.fragments.editProfile.EditProfileViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class EditProfileModule {

    @Binds
    @IntoMap
    @ViewModelKey(EditProfileViewModel::class)
    abstract fun bindEditProfileViewModel(editProfileViewModel: EditProfileViewModel): ViewModel
}
