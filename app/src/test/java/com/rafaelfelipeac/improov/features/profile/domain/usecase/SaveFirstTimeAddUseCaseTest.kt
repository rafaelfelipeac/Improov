package com.rafaelfelipeac.improov.features.profile.domain.usecase

import com.rafaelfelipeac.improov.base.equalTo
import com.rafaelfelipeac.improov.features.profile.domain.repository.FirstTimeAddRepository
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SaveFirstTimeAddUseCaseTest {

    @Mock
    internal lateinit var mockFirstTimeAddRepository: FirstTimeAddRepository

    private lateinit var saveFirstTimeAddUseCase: SaveFirstTimeAddUseCase

    @Before
    fun setup() {
        saveFirstTimeAddUseCase = SaveFirstTimeAddUseCase(mockFirstTimeAddRepository)
    }

    @Test
    fun `GIVEN a boolean value WHEN saveFirstTimeAddUseCase is called THEN return just a Unit value`() {
        runBlocking {
            // given
            val booleanValue = false

            given(mockFirstTimeAddRepository.save(booleanValue))
                .willReturn(Unit)

            // when
            val result = saveFirstTimeAddUseCase(booleanValue)

            // then
            result equalTo Unit
        }
    }
}
