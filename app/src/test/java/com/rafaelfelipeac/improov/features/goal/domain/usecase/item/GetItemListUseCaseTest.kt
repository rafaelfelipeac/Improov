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
class GetItemListUseCaseTest {

    @Mock
    internal lateinit var mockItemRepository: ItemRepository

    private lateinit var getItemListUseCase: GetItemListUseCase

    @Before
    fun setup() {
        getItemListUseCase = GetItemListUseCase(mockItemRepository)
    }

    @Test
    fun `GIVEN a list of items WHEN getItemListUseCase is called THEN return a filtered and ordered list of items`() {
        runBlocking {
            // given
            val goalIdCustom = 3L
            val items = listOf(
                createItem(itemId = 1, goalId = 1, order = 1),
                createItem(itemId = 2, goalId = 1, order = 12),
                createItem(itemId = 3, goalId = 1, order = 3),
                createItem(itemId = 4, goalId = goalIdCustom, order = 43),
                createItem(itemId = 5, goalId = 2, order = 5),
                createItem(itemId = 6, goalId = 2, order = 26),
                createItem(itemId = 7, goalId = goalIdCustom, order = 2),
                createItem(itemId = 8, goalId = goalIdCustom, order = 44),
                createItem(itemId = 9, goalId = goalIdCustom, order = 12))

            val filteredAndOrderedList = items
                .filter { it.goalId == goalIdCustom }
                .sortedBy { it.order }

            given(mockItemRepository.getItems())
                .willReturn(items)

            // when
            val result = getItemListUseCase(goalIdCustom)

            // then
            result equalTo filteredAndOrderedList
        }
    }
}
