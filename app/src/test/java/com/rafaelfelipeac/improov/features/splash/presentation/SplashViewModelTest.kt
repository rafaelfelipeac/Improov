package com.rafaelfelipeac.improov.features.splash.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.rafaelfelipeac.improov.base.CoroutineRule
import com.rafaelfelipeac.improov.base.DataProviderTest.shouldBeEqualTo
import com.rafaelfelipeac.improov.features.splash.domain.usecase.GetWelcomeUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SplashViewModelTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    var coroutinesTestRule = CoroutineRule()

    @get:Rule
    var rule = InstantTaskExecutorRule()

    private var mockGetWelcomeUseCase = mock(GetWelcomeUseCase::class.java)

    private lateinit var splashViewModel: SplashViewModel

    @Before
    fun setup() {
        splashViewModel = SplashViewModel(
            mockGetWelcomeUseCase
        )
    }

    @Test
    fun `GIVEN getWelcome is successful WHEN loadData is called THEN false is returned`() {
        // given
        val booleanValue = false

        given(runBlocking { mockGetWelcomeUseCase() })
            .willReturn(booleanValue)

        // when
        splashViewModel.loadData()

        // then
        splashViewModel.welcome.value shouldBeEqualTo booleanValue
    }
}
