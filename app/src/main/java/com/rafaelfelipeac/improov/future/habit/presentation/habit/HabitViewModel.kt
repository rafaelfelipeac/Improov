package com.rafaelfelipeac.improov.future.habit.presentation.habit

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.rafaelfelipeac.improov.features.goal.presentation.goallist.GoalListViewModel
import javax.inject.Inject

class HabitViewModel @Inject constructor() : ViewModel() {

}

//class HabitViewModel @Inject constructor(
//    private val habitRepository: HabitRepository
//) : BaseViewModel<GoalListViewModel.ViewState, GoalListViewModel.Action>(
//    GoalListViewModel.ViewState()
//) {
//    private var habit: LiveData<Habit>? = null
//
//    fun init(habitId: Long) {
//        habit = habitRepository.getHabit(habitId)
//    }
//
//    // Habit
//    fun getHabits(): LiveData<Habit>? {
//        return habit
//    }
//
//    fun saveHabit(habit: Habit) {
//        habitRepository.save(habit)
//    }
//
//    override fun onReduceState(viewAction: GoalListViewModel.Action): GoalListViewModel.ViewState {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//}
