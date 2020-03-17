package com.rafaelfelipeac.improov.features.goals.domain.usecase

import com.rafaelfelipeac.improov.features.goals.Goal
import com.rafaelfelipeac.improov.features.goals.GoalRepository
import javax.inject.Inject

class SaveListUseCase @Inject constructor(
    private val goalRepository: GoalRepository
) {
    suspend fun execute(): Long {
        return goalRepository.save(Goal())
    }
}
