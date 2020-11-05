package com.rafaelfelipeac.improov.features.goal.domain.usecase.firsttimelist

import com.rafaelfelipeac.improov.base.equalTo
import com.rafaelfelipeac.improov.features.goal.domain.repository.FirstTimeListRepository
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GetFirstTimeListUseCaseTest {

    @Mock
    internal lateinit var mockFirstTimeListRepository: FirstTimeListRepository

    private lateinit var getFirstTimeListUseCase: GetFirstTimeListUseCase

    @Before
    fun setup() {
        getFirstTimeListUseCase = GetFirstTimeListUseCase(mockFirstTimeListRepository)
    }

    @Test
    fun `GIVEN a boolean value WHEN getFirstTimeListUseCase is called THEN return the boolean value`() {
        runBlocking {
            // given
            val booleanValue = true

            given(mockFirstTimeListRepository.getFirstTimeList())
                .willReturn(booleanValue)

            // when
            val result = getFirstTimeListUseCase()

            // then
            result equalTo booleanValue
        }
    }
}
