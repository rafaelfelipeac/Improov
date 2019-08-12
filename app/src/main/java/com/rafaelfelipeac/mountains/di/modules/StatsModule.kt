package com.rafaelfelipeac.mountains.di.modules

import androidx.lifecycle.ViewModel
import com.rafaelfelipeac.mountains.di.modules.viewModel.ViewModelKey
import com.rafaelfelipeac.mountains.ui.fragments.stats.StatsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class StatsModule {

    @Binds
    @IntoMap
    @ViewModelKey(StatsViewModel::class)
    abstract fun bindStatsViewModel(statsViewModel: StatsViewModel): ViewModel
}
