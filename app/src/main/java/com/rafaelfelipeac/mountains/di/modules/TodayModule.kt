package com.rafaelfelipeac.mountains.di.modules

import androidx.lifecycle.ViewModel
import com.rafaelfelipeac.mountains.di.modules.viewModel.ViewModelKey
import com.rafaelfelipeac.mountains.ui.fragments.today.TodayViewModel
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
