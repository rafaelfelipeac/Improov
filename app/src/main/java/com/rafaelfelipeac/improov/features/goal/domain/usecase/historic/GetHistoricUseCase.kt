package com.rafaelfelipeac.improov.features.goal.domain.usecase.historic

import com.rafaelfelipeac.improov.features.commons.domain.model.Historic
import com.rafaelfelipeac.improov.features.goal.domain.repository.HistoricRepository
import javax.inject.Inject

class GetHistoricUseCase @Inject constructor(
    private val historicRepository: HistoricRepository
) {
    suspend operator fun invoke(historicId: Long): Historic {
        return historicRepository.getHistoric(historicId)
    }
}
