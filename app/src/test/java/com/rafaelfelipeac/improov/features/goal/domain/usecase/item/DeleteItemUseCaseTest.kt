package com.rafaelfelipeac.improov.features.goal.domain.usecase.item

import com.rafaelfelipeac.improov.base.DataProviderTest.createItem
import com.rafaelfelipeac.improov.base.equalTo
import com.rafaelfelipeac.improov.features.goal.domain.repository.ItemRepository
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class DeleteItemUseCaseTest {

    @Mock
    internal lateinit var mockItemRepository: ItemRepository

    private lateinit var deleteItemUseCase: DeleteItemUseCase

    private var itemId = 1L

    @Before
    fun setup() {
        deleteItemUseCase = DeleteItemUseCase(mockItemRepository)
    }

    @Test
    fun `GIVEN a itemId WHEN use deleteItemUseCase THEN return just a Unit value`() {
        runBlocking {
            // given
            val item = createItem(itemId)
            given(mockItemRepository.delete(item))
                .willReturn(Unit)

            // when
            val result = deleteItemUseCase(item)

            // then
            result equalTo Unit
        }
    }
}
