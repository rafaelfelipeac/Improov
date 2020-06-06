package com.rafaelfelipeac.improov.features.profile.presentation.profileedit

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.rafaelfelipeac.improov.base.CoroutineRule
import com.rafaelfelipeac.improov.base.DataProvider.shouldBeEqualTo
import com.rafaelfelipeac.improov.features.profile.domain.usecase.GetNameUseCase
import com.rafaelfelipeac.improov.features.profile.domain.usecase.SaveNameUseCase
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
class ProfileEditViewModelTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    var coroutinesTestRule = CoroutineRule()

    @get:Rule
    var rule = InstantTaskExecutorRule()

    private var mockSaveNameUseCase = mock(SaveNameUseCase::class.java)
    private var mockGetNameUseCase = mock(GetNameUseCase::class.java)

    private lateinit var profileEditViewModel: ProfileEditViewModel

    @Before
    fun setup() {
        profileEditViewModel = ProfileEditViewModel(
            mockSaveNameUseCase,
            mockGetNameUseCase
        )
    }

    @Test
    fun `GIVEN saveName is successful WHEN onSaveName is called THEN true is returned`() {
        // given
        val name = "User Name"

        given(runBlocking { mockSaveNameUseCase.execute(name) })
            .willReturn(Unit)

        // when
        profileEditViewModel.onSaveName(name)

        // then
        profileEditViewModel.stateLiveData.value shouldBeEqualTo ProfileEditViewModel.ViewState(
            name = "",
            nameSaved = true
        )
    }

    @Test
    fun `GIVEN getName is successful WHEN loadData is called THEN a name is returned`() {
        // given
        val name = "User Name"

        given(runBlocking { mockGetNameUseCase.execute() })
            .willReturn(name)

        // when
        profileEditViewModel.loadData()

        // then
        profileEditViewModel.stateLiveData.value shouldBeEqualTo ProfileEditViewModel.ViewState(
            name = name,
            nameSaved = false
        )
    }
}
