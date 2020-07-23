package com.rafaelfelipeac.improov.features.profile.domain.usecase

import com.rafaelfelipeac.improov.base.DataProviderTest.shouldBeEqualTo
import com.rafaelfelipeac.improov.features.profile.domain.repository.NameRepository
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SaveNameUseCaseTest {

    @Mock
    internal lateinit var mockNameRepository: NameRepository

    private lateinit var saveNameUseCase: SaveNameUseCase

    @Before
    fun setup() {
        saveNameUseCase = SaveNameUseCase(mockNameRepository)
    }

    @Test
    fun `GIVEN a name WHEN use saveNameUseCase THEN return just a Unit value`() {
        runBlocking {
            // given
            val name = "User Name"

            given(mockNameRepository.save(name))
                .willReturn(Unit)

            // when
            val result = saveNameUseCase(name)

            // then
            result shouldBeEqualTo Unit
        }
    }
}
