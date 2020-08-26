package com.rafaelfelipeac.improov.features.settings.presentation.settingslanguage

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.rafaelfelipeac.improov.base.CoroutineRule
import com.rafaelfelipeac.improov.core.extension.equalTo
import com.rafaelfelipeac.improov.features.settings.domain.usecase.GetLanguageUseCase
import com.rafaelfelipeac.improov.features.settings.domain.usecase.SaveLanguageUseCase
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
class SettingsLanguageViewModelTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    var coroutinesTestRule = CoroutineRule()

    @get:Rule
    var rule = InstantTaskExecutorRule()

    private var mockSaveLanguageUseCase = mock(SaveLanguageUseCase::class.java)
    private var mockGetLanguageUseCase = mock(GetLanguageUseCase::class.java)

    private lateinit var settingsLanguageViewModel: SettingsLanguageViewModel

    @Before
    fun setup() {
        settingsLanguageViewModel = SettingsLanguageViewModel(
            mockSaveLanguageUseCase,
            mockGetLanguageUseCase
        )
    }

    @Test
    fun `GIVEN saveLanguage is successful WHEN onSaveLanguage is called THEN a Unit is returned`() {
        // given
        val language = "pt_br"

        given(runBlocking { mockSaveLanguageUseCase(language) })
            .willReturn(Unit)

        // when
        settingsLanguageViewModel.saveLanguage(language)

        // then
        settingsLanguageViewModel.saved.value equalTo Unit
    }

    @Test
    fun `GIVEN getLanguage is successful WHEN loadData is called THEN a language is returned`() {
        // given
        val language = "pt_br"

        given(runBlocking { mockGetLanguageUseCase() })
            .willReturn(language)

        // when
        settingsLanguageViewModel.loadData()

        // then
        settingsLanguageViewModel.language.value equalTo language
    }
}
