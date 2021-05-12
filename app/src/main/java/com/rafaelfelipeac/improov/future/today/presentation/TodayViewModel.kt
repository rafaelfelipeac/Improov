package com.rafaelfelipeac.improov.future.today.presentation

import androidx.lifecycle.LiveData
import com.rafaelfelipeac.improov.core.platform.base.BaseViewModel
import com.rafaelfelipeac.improov.features.goal.domain.model.Goal
import com.rafaelfelipeac.improov.features.goal.domain.repository.GoalRepository
import com.rafaelfelipeac.improov.features.goal.presentation.goallist.GoalListViewModel
import com.rafaelfelipeac.improov.future.habit.Habit
import com.rafaelfelipeac.improov.future.habit.HabitRepository
import javax.inject.Inject

class TodayViewModel @Inject constructor(
    private val goalRepository: GoalRepository,
    private val habitRepository: HabitRepository
) : BaseViewModel<GoalListViewModel.ViewState, GoalListViewModel.Action>(
    GoalListViewModel.ViewState()
) {
    private var habits: LiveData<List<Habit>>? = null
    private var goals: LiveData<List<Goal>>? = null

    init {
        habits = habitRepository.getHabits()
       // goals = goalRepository.getGoals()
    }

    // Goal
    fun getGoals(): LiveData<List<Goal>>? {
        return goals
    }

    fun saveGoal(goal: Goal) {
        //goalRepository.save(goal)

        //this.goals = goalRepository.getGoals()
    }

    // Habit
    fun getHabits(): LiveData<List<Habit>>? {
        return habits
    }

    fun saveHabit(habit: Habit) {
        habitRepository.save(habit)

        this.habits = habitRepository.getHabits()
    }

    override fun onReduceState(viewAction: GoalListViewModel.Action): GoalListViewModel.ViewState {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
