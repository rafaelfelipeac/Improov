package com.rafaelfelipeac.improov.future.stats.domain.usecase

import com.rafaelfelipeac.improov.features.commons.domain.model.Habit
import com.rafaelfelipeac.improov.future.habit.domain.repository.HabitRepository
import javax.inject.Inject

class GetHabitListUseCase @Inject constructor(
    private val habitRepository: HabitRepository
) {
    suspend operator fun invoke(): List<Habit> {
        return habitRepository.getHabits() // order
    }
}
