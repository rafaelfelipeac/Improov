package com.rafaelfelipeac.improov.core.di.modules

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.rafaelfelipeac.improov.core.di.key.FragmentKey
import com.rafaelfelipeac.improov.core.di.key.ViewModelKey
import com.rafaelfelipeac.improov.features.commons.data.datasource.HabitDataSource
import com.rafaelfelipeac.improov.features.commons.domain.repository.HabitRepository
import com.rafaelfelipeac.improov.future.habit.presentation.HabitFormBottomSheet
import com.rafaelfelipeac.improov.future.habit.presentation.HabitFormViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class HabitModule {

    @Binds
    abstract fun habitRepository(habitDataSource: HabitDataSource): HabitRepository

    @Binds
    @IntoMap
    @FragmentKey(HabitFormBottomSheet::class)
    abstract fun bindHabitFormFragment(habitFormBottomSheet: HabitFormBottomSheet): Fragment

    @Binds
    @IntoMap
    @ViewModelKey(HabitFormViewModel::class)
    abstract fun bindHabitFormViewModel(habitFormViewModel: HabitFormViewModel): ViewModel

}
