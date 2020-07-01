package com.rafaelfelipeac.improov.features.goal.domain.usecase.item

import com.rafaelfelipeac.improov.features.goal.domain.model.Item
import com.rafaelfelipeac.improov.features.goal.domain.repository.ItemRepository
import javax.inject.Inject

class GetItemUseCase @Inject constructor(
    private val itemRepository: ItemRepository
) {
    suspend operator fun invoke(itemId: Long): Item {
        return itemRepository.getItem(itemId)
    }
}
