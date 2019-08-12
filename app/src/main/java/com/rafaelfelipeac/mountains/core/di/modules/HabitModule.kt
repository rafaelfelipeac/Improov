package com.rafaelfelipeac.mountains.core.di.modules

import androidx.lifecycle.ViewModel
import com.rafaelfelipeac.mountains.core.di.modules.viewModel.ViewModelKey
import com.rafaelfelipeac.mountains.features.habit.presentation.HabitViewModel
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
