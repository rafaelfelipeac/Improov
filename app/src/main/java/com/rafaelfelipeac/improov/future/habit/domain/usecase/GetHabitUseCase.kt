package com.rafaelfelipeac.improov.future.habit.domain.usecase

import com.rafaelfelipeac.improov.features.commons.domain.model.Habit
import com.rafaelfelipeac.improov.features.commons.domain.repository.HabitRepository
import javax.inject.Inject

class GetHabitUseCase @Inject constructor(
    private val habitRepository: HabitRepository
) {
    suspend operator fun invoke(habitId: Long): Habit {
        return habitRepository.getHabit(habitId)
    }
}
