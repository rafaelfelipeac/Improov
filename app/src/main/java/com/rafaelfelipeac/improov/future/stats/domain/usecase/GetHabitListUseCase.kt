package com.rafaelfelipeac.improov.future.stats.domain.usecase

import com.rafaelfelipeac.improov.features.commons.domain.repository.HabitRepository
import javax.inject.Inject

class GetHabitListUseCase @Inject constructor(
    private val habitRepository: HabitRepository
) {
    suspend operator fun invoke(): Int {
        return habitRepository.getHabits()
            .filter { it.doneToday }
            .size
    }
}
