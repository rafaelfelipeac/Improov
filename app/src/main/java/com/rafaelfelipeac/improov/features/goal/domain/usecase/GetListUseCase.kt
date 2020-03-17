package com.rafaelfelipeac.improov.features.goal.domain.usecase

import com.rafaelfelipeac.improov.features.goal.domain.model.Goal
import com.rafaelfelipeac.improov.features.goal.domain.repository.GoalRepository
import javax.inject.Inject

class GetListUseCase @Inject constructor(
    private val goalRepository: GoalRepository
) {
    suspend fun execute(): List<Goal> {
        return goalRepository.getGoals()
    }
}
