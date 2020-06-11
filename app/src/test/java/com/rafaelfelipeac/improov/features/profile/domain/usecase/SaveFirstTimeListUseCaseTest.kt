package com.rafaelfelipeac.improov.features.profile.domain.usecase

import com.rafaelfelipeac.improov.base.DataProviderTest.shouldBeEqualTo
import com.rafaelfelipeac.improov.features.profile.domain.repository.FirstTimeListRepository
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SaveFirstTimeListUseCaseTest {

    @Mock
    internal lateinit var mockFirstTimeListRepository: FirstTimeListRepository

    private lateinit var saveFirstTimeListUseCase: SaveFirstTimeListUseCase

    @Before
    fun setup() {
        saveFirstTimeListUseCase = SaveFirstTimeListUseCase(mockFirstTimeListRepository)
    }

    @Test
    fun `GIVEN a boolean value WHEN use saveFirstTimeListUseCase THEN return just a Unit value`() {
        runBlocking {
            // given
            val booleanValue = false

            given(mockFirstTimeListRepository.save(booleanValue))
                .willReturn(Unit)

            // when
            val result = saveFirstTimeListUseCase.execute(booleanValue)

            // then
            result shouldBeEqualTo Unit
        }
    }
}
