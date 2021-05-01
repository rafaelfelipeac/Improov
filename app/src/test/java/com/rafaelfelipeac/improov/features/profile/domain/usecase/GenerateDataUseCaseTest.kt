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
class GenerateDataUseCaseTest {

    @Mock
    internal lateinit var mockDataRepository: DataRepository

    private lateinit var generateDataUseCase: GenerateDataUseCase

    @Before
    fun setup() {
        generateDataUseCase = GenerateDataUseCase(mockDataRepository)
    }

    @Test
    fun `GIVEN generateData is successful WHEN generateDataUseCase is called THEN a Unit is returned`() {
        runBlocking {
            // given
            given(mockDataRepository.generateData())
                .willReturn(Unit)

            // when
            val result = generateDataUseCase()

            // then
            result equalTo Unit
        }
    }
}
