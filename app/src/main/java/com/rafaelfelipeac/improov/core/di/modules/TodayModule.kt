package com.rafaelfelipeac.improov.core.di.modules

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.rafaelfelipeac.improov.core.di.key.FragmentKey
import com.rafaelfelipeac.improov.core.di.key.ViewModelKey
import com.rafaelfelipeac.improov.future.today.presentation.TodayFragment
import com.rafaelfelipeac.improov.future.today.presentation.TodayViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class TodayModule {

    @Binds
    @IntoMap
    @FragmentKey(TodayFragment::class)
    abstract fun bindTodayFragment(todayFragment: TodayFragment): Fragment

    @Binds
    @IntoMap
    @ViewModelKey(TodayViewModel::class)
    abstract fun bindTodayViewModel(todayViewModel: TodayViewModel): ViewModel
}
