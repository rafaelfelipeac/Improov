package com.rafaelfelipeac.improov.features.profile.domain.usecase

import com.rafaelfelipeac.improov.features.profile.domain.repository.FirstTimeListRepository
import javax.inject.Inject

class SaveFirstTimeListUseCase @Inject constructor(
    private val firstTimeListRepository: FirstTimeListRepository
) {
    suspend operator fun invoke(saveFistTimeList: Boolean) {
        return firstTimeListRepository.save(saveFistTimeList)
    }
}
