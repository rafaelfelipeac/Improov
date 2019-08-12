package com.rafaelfelipeac.mountains.di.modules

import androidx.lifecycle.ViewModel
import com.rafaelfelipeac.mountains.di.modules.viewModel.ViewModelKey
import com.rafaelfelipeac.mountains.ui.fragments.forgotPassword.ForgotPasswordViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ForgotPasswordModule {

    @Binds
    @IntoMap
    @ViewModelKey(ForgotPasswordViewModel::class)
    abstract fun bindForgotPassowrdViewModel(forgotPasswordViewModel: ForgotPasswordViewModel): ViewModel
}
