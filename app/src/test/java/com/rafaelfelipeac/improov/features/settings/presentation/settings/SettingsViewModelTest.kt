package com.rafaelfelipeac.improov.features.settings.presentation.settings

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.rafaelfelipeac.improov.base.CoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SettingsViewModelTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    var coroutinesTestRule = CoroutineRule()

    @get:Rule
    var rule = InstantTaskExecutorRule()

    private lateinit var settingsViewModel: SettingsViewModel

    @Before
    fun setup() {
        settingsViewModel = SettingsViewModel()
    }

    @Test
    fun `GIVEN WHEN THEN`() {
        // given

        // when

        // then
    }
}
