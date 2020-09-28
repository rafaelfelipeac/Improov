package com.rafaelfelipeac.improov.features.goal.domain.usecase.historic

import com.rafaelfelipeac.improov.features.commons.domain.model.Historic
import com.rafaelfelipeac.improov.features.goal.domain.repository.HistoricRepository
import javax.inject.Inject

class SaveHistoricUseCase @Inject constructor(
    private val historicRepository: HistoricRepository
) {
    suspend operator fun invoke(historic: Historic): Long {
        return historicRepository.save(historic)
    }
}
