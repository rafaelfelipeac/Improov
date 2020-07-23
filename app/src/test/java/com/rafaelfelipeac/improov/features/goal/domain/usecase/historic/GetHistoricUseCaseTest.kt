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
class GetHistoricUseCaseTest {

    @Mock
    internal lateinit var mockHistoricRepository: HistoricRepository

    private lateinit var getHistoricUseCase: GetHistoricUseCase

    private val historicId = 1L

    @Before
    fun setup() {
        getHistoricUseCase = GetHistoricUseCase(mockHistoricRepository)
    }

    @Test
    fun `GIVEN a historicId WHEN use getHistoricUseCase THEN return a historic with the specific historicId`() {
        runBlocking {
            // given
            val historic = createHistoric(historicId)
            given(mockHistoricRepository.getHistoric(historicId))
                .willReturn(historic)

            // when
            val result = getHistoricUseCase(historicId)

            // then
            result shouldBeEqualTo historic
        }
    }
}
