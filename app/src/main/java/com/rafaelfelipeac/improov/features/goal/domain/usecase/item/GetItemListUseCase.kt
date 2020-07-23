package com.rafaelfelipeac.improov.features.goal.domain.usecase.item

import com.rafaelfelipeac.improov.features.goal.domain.model.Item
import com.rafaelfelipeac.improov.features.goal.domain.repository.ItemRepository
import javax.inject.Inject

class GetItemListUseCase @Inject constructor(
    private val itemRepository: ItemRepository
) {
    suspend operator fun invoke(goalId: Long): List<Item> {
        return itemRepository.getItems()
            .filter { it.goalId == goalId }
            .sortedBy { it.order }
    }
}
