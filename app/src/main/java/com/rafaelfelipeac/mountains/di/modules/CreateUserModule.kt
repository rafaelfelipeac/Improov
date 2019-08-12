package com.rafaelfelipeac.mountains.di.modules

import androidx.lifecycle.ViewModel
import com.rafaelfelipeac.mountains.di.modules.viewModel.ViewModelKey
import com.rafaelfelipeac.mountains.ui.fragments.createUser.CreateUserViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class CreateUserModule {

    @Binds
    @IntoMap
    @ViewModelKey(CreateUserViewModel::class)
    abstract fun bindCreateUserViewModel(createUserViewModel: CreateUserViewModel): ViewModel
}
