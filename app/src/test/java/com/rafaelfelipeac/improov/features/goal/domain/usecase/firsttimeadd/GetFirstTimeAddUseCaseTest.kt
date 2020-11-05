package com.rafaelfelipeac.improov.features.goal.domain.usecase.firsttimeadd

import com.rafaelfelipeac.improov.base.equalTo
import com.rafaelfelipeac.improov.features.goal.domain.repository.FirstTimeAddRepository
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GetFirstTimeAddUseCaseTest {

    @Mock
    internal lateinit var mockFirstTimeAddRepository: FirstTimeAddRepository

    private lateinit var getFirstTimeAddUseCase: GetFirstTimeAddUseCase

    @Before
    fun setup() {
        getFirstTimeAddUseCase = GetFirstTimeAddUseCase(mockFirstTimeAddRepository)
    }

    @Test
    fun `GIVEN a boolean value WHEN getFirstTimeAddUseCase is called THEN return the boolean value`() {
        runBlocking {
            // given
            val booleanValue = true
            given(mockFirstTimeAddRepository.getFirstTimeAdd())
                .willReturn(booleanValue)

            // when
            val result = getFirstTimeAddUseCase()

            // then
            result equalTo booleanValue
        }
    }
}
