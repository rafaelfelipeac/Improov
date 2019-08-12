package com.rafaelfelipeac.mountains.di.modules

import androidx.lifecycle.ViewModel
import com.rafaelfelipeac.mountains.di.modules.viewModel.ViewModelKey
import com.rafaelfelipeac.mountains.ui.fragments.habitForm.HabitFormViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class HabitFormModule {

    @Binds
    @IntoMap
    @ViewModelKey(HabitFormViewModel::class)
    abstract fun bindHabitFormViewModel(habitFormViewModel: HabitFormViewModel): ViewModel
}
