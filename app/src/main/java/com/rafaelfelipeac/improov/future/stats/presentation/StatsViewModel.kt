package com.rafaelfelipeac.improov.future.stats.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.rafaelfelipeac.improov.features.goal.domain.repository.GoalRepository
import com.rafaelfelipeac.improov.features.goal.presentation.goallist.GoalListViewModel
import com.rafaelfelipeac.improov.future.habit.Habit
import javax.inject.Inject

class StatsViewModel @Inject constructor(): ViewModel()

//class StatsViewModel @Inject constructor(
//    private val goalRepository: GoalRepository,
//    private val habitRepository: HabitRepository
//) : BaseViewModel<GoalListViewModel.ViewState, GoalListViewModel.Action>(
//    GoalListViewModel.ViewState()
//) {
//    private var goals: LiveData<List<Goal>>? = null
//    private var habits: LiveData<List<Habit>>? = null
//
//    init {
//        //goals = goalRepository.getGoals()
//        habits = habitRepository.getHabits()
//    }
//
//    // Goal
//    fun getGoals(): LiveData<List<Goal>>? {
//        return goals
//    }
//
//    // Habit
//    fun getHabits(): LiveData<List<Habit>>? {
//        return habits
//    }
//
//    override fun onReduceState(viewAction: GoalListViewModel.Action): GoalListViewModel.ViewState {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//}
