package com.rafaelfelipeac.improov.future.habit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.rafaelfelipeac.improov.core.platform.base.BaseViewModel
import com.rafaelfelipeac.improov.features.goal.presentation.goallist.GoalListViewModel
import javax.inject.Inject

class HabitFormViewModel @Inject constructor(
    private val habitRepository: HabitRepository
) : BaseViewModel<GoalListViewModel.ViewState, GoalListViewModel.Action>(
    GoalListViewModel.ViewState()
) {

    private var habit: LiveData<Habit>? = null

    private var habits: LiveData<List<Habit>>? = habitRepository.getHabits()

    var habitIdInserted: MutableLiveData<Long> = MutableLiveData()

    fun init(habitId: Long) {
        habit = habitRepository.getHabit(habitId)
    }

    // Habit
    fun getHabits(): LiveData<List<Habit>>? {
        return habits
    }

    fun getHabit(): LiveData<Habit>? {
        return habit
    }

    fun saveHabit(habit: Habit) {
        habitIdInserted.value = habitRepository.save(habit)
    }

    override fun onReduceState(viewAction: GoalListViewModel.Action): GoalListViewModel.ViewState {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
