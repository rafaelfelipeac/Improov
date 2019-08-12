package com.rafaelfelipeac.mountains.di.modules

import androidx.lifecycle.ViewModel
import com.rafaelfelipeac.mountains.di.modules.viewModel.ViewModelKey
import com.rafaelfelipeac.mountains.ui.fragments.habit.HabitViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class HabitModule {

    @Binds
    @IntoMap
    @ViewModelKey(HabitViewModel::class)
    abstract fun bindHabitViewModel(habitViewModel: HabitViewModel): ViewModel
}
