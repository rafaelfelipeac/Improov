package com.rafaelfelipeac.improov.features.goal.domain.usecase.firsttimeadd

import com.rafaelfelipeac.improov.features.goal.domain.repository.FirstTimeAddRepository
import javax.inject.Inject

class SaveFirstTimeAddUseCase @Inject constructor(
    private val firstTimeAddRepository: FirstTimeAddRepository
) {
    suspend operator fun invoke(firstTimeAdd: Boolean) {
        return firstTimeAddRepository.save(firstTimeAdd)
    }
}
