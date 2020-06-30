package com.rafaelfelipeac.improov.features.welcome.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.rafaelfelipeac.improov.base.CoroutineRule
import com.rafaelfelipeac.improov.base.DataProviderTest.shouldBeEqualTo
import com.rafaelfelipeac.improov.features.welcome.domain.usecase.GetWelcomeUseCase
import com.rafaelfelipeac.improov.features.welcome.domain.usecase.SaveWelcomeUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito
import org.mockito.BDDMockito.given
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class WelcomeViewModelTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    var coroutinesTestRule = CoroutineRule()

    @get:Rule
    var rule = InstantTaskExecutorRule()

    private var mockSaveWelcomeUseCase = BDDMockito.mock(SaveWelcomeUseCase::class.java)
    private var mockGetWelcomeUseCase = BDDMockito.mock(GetWelcomeUseCase::class.java)

    private lateinit var welcomeViewModel: WelcomeViewModel

    @Before
    fun setup() {
        welcomeViewModel = WelcomeViewModel(
            mockSaveWelcomeUseCase,
            mockGetWelcomeUseCase
        )
    }

    @Test
    fun `GIVEN saveWelcome is successful WHEN onSaveWelcome is called THEN Unit is returned`() {
        // given
        val booleanValue = true

        given(runBlocking { mockSaveWelcomeUseCase.execute(booleanValue) })
            .willReturn(Unit)

        // when
        welcomeViewModel.saveWelcome(booleanValue)

        // then
        welcomeViewModel.saved.value shouldBeEqualTo Unit
    }

    @Test
    fun `GIVEN getWelcome is successful WHEN loadData is called THEN false is returned`() {
        // given
        val booleanValue = false

        given(runBlocking { mockGetWelcomeUseCase.execute() })
            .willReturn(booleanValue)

        // when
        welcomeViewModel.loadData()

        // then
        welcomeViewModel.welcome.value shouldBeEqualTo booleanValue
    }
}
