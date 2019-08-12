package com.rafaelfelipeac.mountains.di.modules

import androidx.lifecycle.ViewModel
import com.rafaelfelipeac.mountains.di.modules.viewModel.ViewModelKey
import com.rafaelfelipeac.mountains.ui.fragments.goalForm.GoalFormViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class GoalFormModule {

    @Binds
    @IntoMap
    @ViewModelKey(GoalFormViewModel::class)
    abstract fun bindGoalFormViewModel(goalFormViewModel: GoalFormViewModel): ViewModel
}
