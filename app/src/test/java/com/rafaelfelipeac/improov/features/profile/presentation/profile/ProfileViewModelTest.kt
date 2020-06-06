package com.rafaelfelipeac.improov.features.profile.presentation.profile

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.rafaelfelipeac.improov.base.CoroutineRule
import com.rafaelfelipeac.improov.base.DataProvider.shouldBeEqualTo
import com.rafaelfelipeac.improov.features.profile.domain.usecase.GetNameUseCase
import com.rafaelfelipeac.improov.features.profile.domain.usecase.SaveFirstTimeAddUseCase
import com.rafaelfelipeac.improov.features.profile.domain.usecase.SaveFirstTimeListUseCase
import com.rafaelfelipeac.improov.features.profile.domain.usecase.SaveWelcomeUseCase
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
class ProfileViewModelTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    var coroutinesTestRule = CoroutineRule()

    @get:Rule
    var rule = InstantTaskExecutorRule()

    private var mockSaveWelcomeUseCase = mock(SaveWelcomeUseCase::class.java)
    private var mockGetNameUseCase = mock(GetNameUseCase::class.java)
    private var mockSaveFirstTimeAddUseCase = mock(SaveFirstTimeAddUseCase::class.java)
    private var mockSaveFirsTimeListUseCass = mock(SaveFirstTimeListUseCase::class.java)

    private lateinit var profileViewModel: ProfileViewModel

    @Before
    fun setup() {
        profileViewModel = ProfileViewModel(
            mockSaveWelcomeUseCase,
            mockGetNameUseCase,
            mockSaveFirstTimeAddUseCase,
            mockSaveFirsTimeListUseCass
        )
    }

    @Test
    fun `GIVEN saveWelcome is successful WHEN onSaveWelcome is called THEN true is returned`() {
        // given
        val booleanValue = true

        given(runBlocking { mockSaveWelcomeUseCase.execute(booleanValue) })
            .willReturn(Unit)

        // when
        profileViewModel.onSaveWelcome(booleanValue)

        // then
        profileViewModel.stateLiveData.value shouldBeEqualTo ProfileViewModel.ViewState(
            name = "",
            welcomeSaved = true,
            firstTimeAddSaved = false,
            firstTimeListSaved = false
        )
    }

    @Test
    fun `GIVEN getName is successful WHEN loadData is called THEN a name is returned`() {
        // given
        val userName = "User Name"

        given(runBlocking { mockGetNameUseCase.execute() })
            .willReturn(userName)

        // when
        profileViewModel.loadData()

        // then
        profileViewModel.stateLiveData.value shouldBeEqualTo ProfileViewModel.ViewState(
            name = userName,
            welcomeSaved = false,
            firstTimeAddSaved = false,
            firstTimeListSaved = false
        )
    }

    @Test
    fun `GIVEN saveFirstTimeAdd is successful WHEN onSaveFirstTimeAdd is called THEN true is returned`() {
        // given
        val booleanValue = true

        given(runBlocking { mockSaveFirstTimeAddUseCase.execute(booleanValue) })
            .willReturn(Unit)

        // when
        profileViewModel.onSaveFirstTimeAdd(booleanValue)

        // then
        profileViewModel.stateLiveData.value shouldBeEqualTo ProfileViewModel.ViewState(
            name = "",
            welcomeSaved = false,
            firstTimeAddSaved = true,
            firstTimeListSaved = false
        )
    }

    @Test
    fun `GIVEN saveFirstTimeList is successful WHEN onSaveFirstTimeList is called THEN true is returned`() {
        // given
        val booleanValue = true

        given(runBlocking { mockSaveFirsTimeListUseCass.execute(booleanValue) })
            .willReturn(Unit)

        // when
        profileViewModel.onSaveFirstTimeList(booleanValue)

        // then
        profileViewModel.stateLiveData.value shouldBeEqualTo ProfileViewModel.ViewState(
            name = "",
            welcomeSaved = false,
            firstTimeAddSaved = false,
            firstTimeListSaved = true
        )
    }
}
