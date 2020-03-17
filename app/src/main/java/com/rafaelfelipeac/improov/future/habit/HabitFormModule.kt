package com.rafaelfelipeac.improov.future.habit

import androidx.lifecycle.ViewModel
import com.rafaelfelipeac.improov.core.di.modules.viewModel.ViewModelKey
import com.rafaelfelipeac.improov.future.habit.HabitFormViewModel
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
