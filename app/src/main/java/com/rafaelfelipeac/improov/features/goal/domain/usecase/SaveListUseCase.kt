package com.rafaelfelipeac.improov.features.goal.domain.usecase

import com.rafaelfelipeac.improov.features.goal.domain.model.Goal
import com.rafaelfelipeac.improov.features.goal.domain.repository.GoalRepository
import javax.inject.Inject

class SaveListUseCase @Inject constructor(
    private val goalRepository: GoalRepository
) {
    suspend fun execute(goal: Goal): Long {
        return goalRepository.save(goal)
    }
}
