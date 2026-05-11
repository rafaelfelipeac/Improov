package com.rafaelfelipeac.improov.features.settings.presentation.settings

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.rafaelfelipeac.improov.base.CoroutineRule
import com.rafaelfelipeac.improov.base.equalTo
import com.rafaelfelipeac.improov.core.ui.theme.AppThemeMode
import com.rafaelfelipeac.improov.features.settings.domain.usecase.GetThemeModeUseCase
import com.rafaelfelipeac.improov.features.settings.domain.usecase.SaveThemeModeUseCase
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SettingsViewModelTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    var coroutinesTestRule = CoroutineRule()

    @get:Rule
    var rule = InstantTaskExecutorRule()

    private var mockGetThemeModeUseCase = mock(GetThemeModeUseCase::class.java)
    private var mockSaveThemeModeUseCase = mock(SaveThemeModeUseCase::class.java)

    private lateinit var settingsViewModel: SettingsViewModel

    @Before
    fun setup() {
        settingsViewModel = SettingsViewModel(
            mockGetThemeModeUseCase,
            mockSaveThemeModeUseCase,
        )
    }

    @Test
    fun `GIVEN the theme mode WHEN themeMode is read THEN the same theme mode is returned`() {
        runBlocking {
            // given
            given(mockGetThemeModeUseCase())
                .willReturn(flowOf(AppThemeMode.DARK))

            // when
            val result = settingsViewModel.themeMode.first()

            // then
            result equalTo AppThemeMode.DARK
        }
    }

    @Test
    fun `GIVEN a theme mode WHEN saveThemeMode is called THEN the use case receives it`() {
        runBlocking {
            // given
            val themeMode = AppThemeMode.LIGHT
            given(mockSaveThemeModeUseCase(themeMode))
                .willReturn(Unit)

            // when
            settingsViewModel.saveThemeMode(themeMode)

            // then
            verify(mockSaveThemeModeUseCase).invoke(themeMode)
        }
    }
}
