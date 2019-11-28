package com.rafaelfelipeac.improov.core.di.modules

import androidx.lifecycle.ViewModel
import com.rafaelfelipeac.improov.core.di.modules.viewModel.ViewModelKey
import com.rafaelfelipeac.improov.features.goal.presentation.goal.GoalViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class GoalModule {

    @Binds
    @IntoMap
    @ViewModelKey(GoalViewModel::class)
    abstract fun bindGoalViewModel(goalViewModel: GoalViewModel): ViewModel
}
