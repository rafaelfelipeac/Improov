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
class SaveItemUseCaseTest {

    @Mock
    internal lateinit var mockItemRepository: ItemRepository

    private lateinit var saveItemUseCase: SaveItemUseCase

    private var itemId = 1L

    @Before
    fun setup() {
        saveItemUseCase = SaveItemUseCase(mockItemRepository)
    }

    @Test
    fun `GIVEN a itemId WHEN use saveItemUseCase THEN return the same itemId as a confirmation`() {
        runBlocking {
            // given
            val item = createItem(itemId)
            given(mockItemRepository.save(item))
                .willReturn(itemId)

            // when
            val result = saveItemUseCase(item)

            // then
            result equalTo itemId
        }
    }
}
