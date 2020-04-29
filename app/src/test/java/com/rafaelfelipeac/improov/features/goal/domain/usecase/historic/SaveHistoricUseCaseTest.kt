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
class SaveHistoricUseCaseTest {

    @Mock
    internal lateinit var mockHistoricRepository: HistoricRepository

    private lateinit var saveHistoricUseCase: SaveHistoricUseCase

    private var historicId = 1L

    @Before
    fun setup() {
        saveHistoricUseCase = SaveHistoricUseCase(mockHistoricRepository)
    }

    @Test
    fun `GIVEN a historicId WHEN use saveHistoricUseCase THEN return the same historicId as a confirmation`() {
        runBlocking {
            // given
            val historic = createHistoric(historicId)
            given(mockHistoricRepository.save(historic))
                .willReturn(historicId)

            // when
            val result = saveHistoricUseCase.execute(historic)

            // then
            result shouldBeEqualTo historicId
        }
    }
}
