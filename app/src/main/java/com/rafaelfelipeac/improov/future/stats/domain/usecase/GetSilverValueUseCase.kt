package com.rafaelfelipeac.improov.future.stats.domain.usecase

import com.rafaelfelipeac.improov.features.commons.domain.repository.GoalRepository
import javax.inject.Inject

class GetSilverValueUseCase @Inject constructor(
    private val goalRepository: GoalRepository
) {
    suspend operator fun invoke(): Int {
        return goalRepository.getGoals()
            .filter { it.divideAndConquer && it.value >= it.silverValue }
            .size
    }
}
