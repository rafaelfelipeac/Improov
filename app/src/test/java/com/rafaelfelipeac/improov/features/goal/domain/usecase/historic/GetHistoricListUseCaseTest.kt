package com.rafaelfelipeac.improov.features.goal.domain.usecase.historic

import com.rafaelfelipeac.improov.base.DataProviderTest.createHistoric
import com.rafaelfelipeac.improov.base.DataProviderTest.shouldBeEqualTo
import com.rafaelfelipeac.improov.features.goal.domain.repository.HistoricRepository
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GetHistoricListUseCaseTest {

    @Mock
    internal lateinit var mockHistoricRepository: HistoricRepository

    private lateinit var getHistoricListUseCase: GetHistoricListUseCase

    @Before
    fun setup() {
        getHistoricListUseCase = GetHistoricListUseCase(mockHistoricRepository)
    }

    @Test
    fun `GIVEN a list of historics WHEN use getHistoricListUseCase THEN return a filtered and reversed list`() {
        runBlocking {
            // given
            val goalIdCustom = 3L
            val historics = listOf(
                createHistoric(historicId = 1, goalId = 1),
                createHistoric(historicId = 2, goalId = 1),
                createHistoric(historicId = 3, goalId = 1),
                createHistoric(historicId = 4, goalId = 2),
                createHistoric(historicId = 5, goalId = 2),
                createHistoric(historicId = 6, goalId = 2),
                createHistoric(historicId = 7, goalId = goalIdCustom),
                createHistoric(historicId = 8, goalId = goalIdCustom),
                createHistoric(historicId = 9, goalId = goalIdCustom)
            )

            val filteredAndReversedList = historics
                .filter { it.goalId == goalIdCustom }
                .reversed()

            given(mockHistoricRepository.getHistorics())
                .willReturn(historics)

            // when
            val result = getHistoricListUseCase(goalIdCustom)

            // then
            result shouldBeEqualTo filteredAndReversedList
        }
    }
}
