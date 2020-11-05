package com.rafaelfelipeac.improov.features.splash.domain

import com.rafaelfelipeac.improov.base.equalTo
import com.rafaelfelipeac.improov.features.splash.domain.repository.WelcomeRepository
import com.rafaelfelipeac.improov.features.splash.domain.usecase.GetWelcomeUseCase
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GetWelcomeUseCaseTest {

    @Mock
    internal lateinit var mockWelcomeRepository: WelcomeRepository

    private lateinit var getWelcomeUseCase: GetWelcomeUseCase

    @Before
    fun setup() {
        getWelcomeUseCase =
            GetWelcomeUseCase(
                mockWelcomeRepository
            )
    }

    @Test
    fun `GIVEN a boolean value WHEN getWelcomeUseCase is called THEN return the boolean value`() {
        runBlocking {
            // given
            val booleanValue = true

            given(mockWelcomeRepository.getWelcome())
                .willReturn(booleanValue)

            // when
            val result = getWelcomeUseCase()

            // then
            result equalTo booleanValue
        }
    }
}
