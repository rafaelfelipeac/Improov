package com.rafaelfelipeac.improov.future.habit.domain.usecase

import com.rafaelfelipeac.improov.features.commons.domain.model.Habit
import com.rafaelfelipeac.improov.future.habit.domain.repository.HabitRepository
import javax.inject.Inject

class SaveHabitUseCase @Inject constructor(
    private val habitRepository: HabitRepository
) {
    suspend operator fun invoke(habit: Habit): Long {
        return habitRepository.save(habit)
    }
}
