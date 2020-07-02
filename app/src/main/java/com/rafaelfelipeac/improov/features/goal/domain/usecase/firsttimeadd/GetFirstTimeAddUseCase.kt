package com.rafaelfelipeac.improov.features.goal.domain.usecase.firsttimeadd

import com.rafaelfelipeac.improov.features.goal.domain.repository.FirstTimeAddRepository
import javax.inject.Inject

class GetFirstTimeAddUseCase @Inject constructor(
    private val firstTimeAddRepository: FirstTimeAddRepository
) {
    suspend operator fun invoke(): Boolean {
        return firstTimeAddRepository.getFirstTimeAdd()
    }
}
