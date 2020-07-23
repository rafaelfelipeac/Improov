package com.rafaelfelipeac.improov.features.goal.domain.usecase.firsttimelist

import com.rafaelfelipeac.improov.features.goal.domain.repository.FirstTimeListRepository
import javax.inject.Inject

class GetFirstTimeListUseCase @Inject constructor(
    private val firstTimeListRepository: FirstTimeListRepository
) {
    suspend operator fun invoke(): Boolean {
        return firstTimeListRepository.getFirstTimeList()
    }
}
