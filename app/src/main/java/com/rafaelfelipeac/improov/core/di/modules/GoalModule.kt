package com.rafaelfelipeac.improov.core.di.modules

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.rafaelfelipeac.improov.core.di.modules.viewModel.ViewModelKey
import com.rafaelfelipeac.improov.features.goal.domain.repository.GoalRepository
import com.rafaelfelipeac.improov.features.goal.data.repository.GoalRepositoryImpl
import com.rafaelfelipeac.improov.features.goal.presentation.goallist.GoalListFragment
import com.rafaelfelipeac.improov.features.goal.presentation.goallist.GoalListViewModel
import dagger.Binds
import dagger.Module
import dagger.android.support.FragmentKey
import dagger.multibindings.IntoMap

@Module
abstract class ListModule {

    @Binds
    @IntoMap
    @ViewModelKey(GoalListViewModel::class)
    abstract fun bindListViewModel(goalListViewModel: GoalListViewModel): ViewModel


    @Binds
    abstract fun postsRepository(postsRepositoryImpl: GoalRepositoryImpl): GoalRepository

    @Binds
    @IntoMap
    @FragmentKey(GoalListFragment::class)
    abstract fun postListFragment(goalListFragment: GoalListFragment): Fragment
}
