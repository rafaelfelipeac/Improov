package com.rafaelfelipeac.improov.core.di.modules

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.rafaelfelipeac.improov.core.di.modules.viewModel.ViewModelKey
import com.rafaelfelipeac.improov.features.goal.domain.repository.GoalRepository
import com.rafaelfelipeac.improov.features.goal.data.repository.GoalRepositoryImpl
import com.rafaelfelipeac.improov.features.goal.data.repository.HistoricRepositoryImpl
import com.rafaelfelipeac.improov.features.goal.data.repository.ItemRepositoryImpl
import com.rafaelfelipeac.improov.features.goal.domain.repository.HistoricRepository
import com.rafaelfelipeac.improov.features.goal.domain.repository.ItemRepository
import com.rafaelfelipeac.improov.features.goal.presentation.goalform.GoalFormViewModel
import com.rafaelfelipeac.improov.features.goal.presentation.goallist.GoalListFragment
import com.rafaelfelipeac.improov.features.goal.presentation.goallist.GoalListViewModel
import dagger.Binds
import dagger.Module
import dagger.android.support.FragmentKey
import dagger.multibindings.IntoMap

@Module
abstract class GoalModule {

    @Binds
    @IntoMap
    @FragmentKey(GoalListFragment::class)
    abstract fun bindListFragment(goalListFragment: GoalListFragment): Fragment

    @Binds
    @IntoMap
    @ViewModelKey(GoalListViewModel::class)
    abstract fun bindListViewModel(goalListViewModel: GoalListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(GoalFormViewModel::class)
    abstract fun bindGoalFormViewModel(goalFormViewModel: GoalFormViewModel): ViewModel

    @Binds
    abstract fun goalRepository(goalRepositoryImpl: GoalRepositoryImpl): GoalRepository

    @Binds
    abstract fun itemRepository(itemRepositoryImpl: ItemRepositoryImpl): ItemRepository

    @Binds
    abstract fun historicRepository(historicRepositoryImpl: HistoricRepositoryImpl): HistoricRepository

}
