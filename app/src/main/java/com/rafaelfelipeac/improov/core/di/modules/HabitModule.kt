package com.rafaelfelipeac.improov.core.di.modules

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.rafaelfelipeac.improov.core.di.key.FragmentKey
import com.rafaelfelipeac.improov.core.di.key.ViewModelKey
import com.rafaelfelipeac.improov.future.habit.data.HabitDataSource
import com.rafaelfelipeac.improov.future.habit.domain.repository.HabitRepository
import com.rafaelfelipeac.improov.future.habit.presentation.habit.HabitFragment
import com.rafaelfelipeac.improov.future.habit.presentation.habit.HabitViewModel
import com.rafaelfelipeac.improov.future.habit.presentation.habitform.HabitFormFragment
import com.rafaelfelipeac.improov.future.habit.presentation.habitform.HabitFormViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class HabitModule {

    @Binds
    abstract fun habitRepository(habitDataSource: HabitDataSource): HabitRepository

    @Binds
    @IntoMap
    @FragmentKey(HabitFragment::class)
    abstract fun bindHabitFragment(habitFragment: HabitFragment): Fragment

    @Binds
    @IntoMap
    @ViewModelKey(HabitViewModel::class)
    abstract fun bindHabitViewModel(habitViewModel: HabitViewModel): ViewModel

    @Binds
    @IntoMap
    @FragmentKey(HabitFormFragment::class)
    abstract fun bindHabitFormFragment(habitFormFragment: HabitFormFragment): Fragment

    @Binds
    @IntoMap
    @ViewModelKey(HabitFormViewModel::class)
    abstract fun bindHabitFormViewModel(habitFormViewModel: HabitFormViewModel): ViewModel

}
