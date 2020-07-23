package com.rafaelfelipeac.improov.features.goal.domain.usecase.firsttimelist

import com.rafaelfelipeac.improov.features.goal.domain.repository.FirstTimeListRepository
import javax.inject.Inject

class SaveFirstTimeListUseCase @Inject constructor(
    private val firstTimeListRepository: FirstTimeListRepository
) {
    suspend operator fun invoke(saveFistTimeList: Boolean) {
        return firstTimeListRepository.save(saveFistTimeList)
    }
}
