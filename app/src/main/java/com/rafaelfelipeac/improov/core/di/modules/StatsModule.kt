package com.rafaelfelipeac.improov.core.di.modules

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.rafaelfelipeac.improov.core.di.key.FragmentKey
import com.rafaelfelipeac.improov.core.di.key.ViewModelKey
import com.rafaelfelipeac.improov.future.stats.presentation.StatsFragment
import com.rafaelfelipeac.improov.future.stats.presentation.StatsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class StatsModule {

    @Binds
    @IntoMap
    @FragmentKey(StatsFragment::class)
    abstract fun bindStatsFragment(statsFragment: StatsFragment): Fragment

    @Binds
    @IntoMap
    @ViewModelKey(StatsViewModel::class)
    abstract fun bindStatsViewModel(statsViewModel: StatsViewModel): ViewModel
}
