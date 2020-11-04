package com.rafaelfelipeac.improov.features.welcome.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.rafaelfelipeac.improov.base.CoroutineRule
import com.rafaelfelipeac.improov.base.equalTo
import com.rafaelfelipeac.improov.features.welcome.domain.usecase.SaveWelcomeUseCase
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
class WelcomeViewModelTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    var coroutinesTestRule = CoroutineRule()

    @get:Rule
    var rule = InstantTaskExecutorRule()

    private var mockSaveWelcomeUseCase = mock(SaveWelcomeUseCase::class.java)

    private lateinit var welcomeViewModel: WelcomeViewModel

    @Before
    fun setup() {
        welcomeViewModel = WelcomeViewModel(
            mockSaveWelcomeUseCase
        )
    }

    @Test
    fun `GIVEN saveWelcome is successful WHEN saveWelcome is called THEN a Unit is returned`() {
        // given
        val booleanValue = true

        given(runBlocking { mockSaveWelcomeUseCase(booleanValue) })
            .willReturn(Unit)

        // when
        welcomeViewModel.saveWelcome(booleanValue)

        // then
        welcomeViewModel.saved.value equalTo Unit
    }
}
