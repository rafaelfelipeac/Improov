package com.rafaelfelipeac.improov.future.today.domain.usecase

import com.rafaelfelipeac.improov.features.commons.domain.model.Habit
import com.rafaelfelipeac.improov.features.commons.domain.repository.HabitRepository
import javax.inject.Inject

class GetHabitListUseCase @Inject constructor(
    private val habitRepository: HabitRepository
) {
    suspend operator fun invoke(): List<Habit> {
        return habitRepository.getHabits()
            .sortedBy { it.order }
    }
}
