package com.rafaelfelipeac.improov.features.goal.domain.usecase.historic

import com.rafaelfelipeac.improov.features.goal.domain.model.Historic
import com.rafaelfelipeac.improov.features.goal.domain.repository.HistoricRepository
import javax.inject.Inject

class GetHistoricListUseCase @Inject constructor(
    private val historicRepository: HistoricRepository
) {
    suspend fun execute(goalId: Long): List<Historic> {
        return historicRepository.getHistorics()
            .filter { it.goalId == goalId }
            .reversed()
    }
}
