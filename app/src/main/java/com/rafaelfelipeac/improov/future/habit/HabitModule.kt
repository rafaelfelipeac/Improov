package com.rafaelfelipeac.improov.future.habit

import androidx.lifecycle.ViewModel
import com.rafaelfelipeac.improov.core.di.modules.viewModel.ViewModelKey
import com.rafaelfelipeac.improov.future.habit.HabitViewModel
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
