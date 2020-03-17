package com.rafaelfelipeac.improov.features.profile

import com.rafaelfelipeac.improov.core.platform.base.BaseViewModel
import com.rafaelfelipeac.improov.features.goal.presentation.goallist.GoalListViewModel
import javax.inject.Inject

class ProfileViewModel @Inject constructor() : BaseViewModel<GoalListViewModel.ViewState, GoalListViewModel.Action>(
    GoalListViewModel.ViewState()
) {
    override fun onReduceState(viewAction: GoalListViewModel.Action): GoalListViewModel.ViewState {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}