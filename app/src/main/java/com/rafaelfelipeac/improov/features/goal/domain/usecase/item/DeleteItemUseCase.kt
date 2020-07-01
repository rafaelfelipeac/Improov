package com.rafaelfelipeac.improov.features.goal.domain.usecase.item

import com.rafaelfelipeac.improov.features.goal.domain.model.Item
import com.rafaelfelipeac.improov.features.goal.domain.repository.ItemRepository
import javax.inject.Inject

class DeleteItemUseCase @Inject constructor(
    private val itemRepository: ItemRepository
) {
    suspend operator fun invoke(item: Item) {
        return itemRepository.delete(item)
    }
}
