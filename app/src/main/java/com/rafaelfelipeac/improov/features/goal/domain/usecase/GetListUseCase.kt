package com.rafaelfelipeac.improov.features.goals.domain.usecase

import com.rafaelfelipeac.improov.features.goals.Goal
import com.rafaelfelipeac.improov.features.goals.GoalRepository
import javax.inject.Inject

class GetListUseCase @Inject constructor(
    private val goalRepository: GoalRepository
) {
    suspend fun execute(): List<Goal> {
        return goalRepository.getGoals()
    }
}
