package com.rafaelfelipeac.mountains.di.modules

import androidx.lifecycle.ViewModel
import com.rafaelfelipeac.mountains.di.modules.viewModel.ViewModelKey
import com.rafaelfelipeac.mountains.ui.fragments.goal.GoalViewModel
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
