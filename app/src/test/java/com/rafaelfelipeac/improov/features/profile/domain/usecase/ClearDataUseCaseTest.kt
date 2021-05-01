package com.rafaelfelipeac.improov.features.profile.domain.usecase

import com.rafaelfelipeac.improov.base.equalTo
import com.rafaelfelipeac.improov.features.profile.domain.repository.DataRepository
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ClearDataUseCaseTest {

    @Mock
    internal lateinit var mockDataRepository: DataRepository

    private lateinit var clearDataUseCase: ClearDataUseCase

    @Before
    fun setup() {
        clearDataUseCase = ClearDataUseCase(mockDataRepository)
    }

    @Test
    fun `GIVEN clearData is successful WHEN clearDataUseCase is called THEN a Unit is returned`() {
        runBlocking {
            // given
            given(mockDataRepository.clearData())
                .willReturn(Unit)

            // when
            val result = clearDataUseCase()

            // then
            result equalTo Unit
        }
    }
}
