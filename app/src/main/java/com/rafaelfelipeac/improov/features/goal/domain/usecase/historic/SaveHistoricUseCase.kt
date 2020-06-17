package com.rafaelfelipeac.improov.features.goal.domain.usecase.historic

import com.rafaelfelipeac.improov.features.goal.domain.model.Historic
import com.rafaelfelipeac.improov.features.goal.domain.repository.HistoricRepository
import javax.inject.Inject

class SaveHistoricUseCase @Inject constructor(
    private val historicRepository: HistoricRepository
) {
    suspend fun execute(historic: Historic): Long {
        return historicRepository.save(historic)
    }
}
