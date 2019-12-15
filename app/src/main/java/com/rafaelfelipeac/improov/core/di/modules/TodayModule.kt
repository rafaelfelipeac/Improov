package com.rafaelfelipeac.improov.core.di.modules

import androidx.lifecycle.ViewModel
import com.rafaelfelipeac.improov.core.di.modules.viewModel.ViewModelKey
import com.rafaelfelipeac.improov.features.today.presentation.TodayViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class TodayModule {

    @Binds
    @IntoMap
    @ViewModelKey(TodayViewModel::class)
    abstract fun bindTodayViewModel(todayViewModel: TodayViewModel): ViewModel
}