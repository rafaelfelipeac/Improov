package com.rafaelfelipeac.mountains.core.di.modules

import androidx.lifecycle.ViewModel
import com.rafaelfelipeac.mountains.core.di.modules.viewModel.ViewModelKey
import com.rafaelfelipeac.mountains.features.goal.presentation.GoalViewModel
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
