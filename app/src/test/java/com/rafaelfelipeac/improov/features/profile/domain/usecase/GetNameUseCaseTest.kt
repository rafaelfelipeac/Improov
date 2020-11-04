package com.rafaelfelipeac.improov.features.profile.domain.usecase

import com.rafaelfelipeac.improov.base.equalTo
import com.rafaelfelipeac.improov.features.profile.domain.repository.NameRepository
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GetNameUseCaseTest {

    @Mock
    internal lateinit var mockNameRepository: NameRepository

    private lateinit var getNameUseCase: GetNameUseCase

    @Before
    fun setup() {
        getNameUseCase = GetNameUseCase(mockNameRepository)
    }

    @Test
    fun `GIVEN a name WHEN getNameUseCase is called THEN return the same name`() {
        runBlocking {
            // given
            val name = "User Name"

            given(mockNameRepository.getName())
                .willReturn(name)

            // when
            val result = getNameUseCase()

            // then
            result equalTo name
        }
    }
}
