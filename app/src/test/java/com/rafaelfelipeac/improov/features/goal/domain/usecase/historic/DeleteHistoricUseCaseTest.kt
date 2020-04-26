package com.rafaelfelipeac.improov.features.goal.domain.usecase.historic

import com.rafaelfelipeac.improov.base.DataProvider.createHistoric
import com.rafaelfelipeac.improov.base.DataProvider.shouldBeEqualTo
import com.rafaelfelipeac.improov.features.goal.domain.repository.HistoricRepository
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class DeleteHistoricUseCaseTest {

    @Mock
    internal lateinit var mockHistoricRepository: HistoricRepository

    private lateinit var deleteHistoricUseCase: DeleteHistoricUseCase

    private var historicId = 1L

    @Before
    fun setup() {
        deleteHistoricUseCase = DeleteHistoricUseCase(mockHistoricRepository)
    }

    @Test
    fun `GIVEN a historicId WHEN use deleteHistoricUseCase THEN return just a Unit value`() {
        runBlocking {
            // given
            val historic = createHistoric(historicId)
            given(mockHistoricRepository.delete(historic))
                .willReturn(Unit)

            // when
            val result = deleteHistoricUseCase.execute(historic)

            // then
            result shouldBeEqualTo Unit
        }
    }
}
