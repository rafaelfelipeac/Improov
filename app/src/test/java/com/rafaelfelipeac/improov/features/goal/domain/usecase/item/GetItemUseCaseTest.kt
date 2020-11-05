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
class GetItemUseCaseTest {

    @Mock
    internal lateinit var mockItemRepository: ItemRepository

    private lateinit var getItemUseCase: GetItemUseCase

    private val itemId = 1L

    @Before
    fun setup() {
        getItemUseCase = GetItemUseCase(mockItemRepository)
    }

    @Test
    fun `GIVEN a itemId WHEN getItemUseCase is called THEN return a item with the specific itemId`() {
        runBlocking {
            // given
            val item = createItem(itemId)
            given(mockItemRepository.getItem(itemId))
                .willReturn(item)

            // when
            val result = getItemUseCase(itemId)

            // then
            result equalTo item
        }
    }
}
