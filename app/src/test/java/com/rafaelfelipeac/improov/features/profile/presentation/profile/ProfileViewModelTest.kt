package com.rafaelfelipeac.improov.features.profile.presentation.profile

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.rafaelfelipeac.improov.base.CoroutineRule
import com.rafaelfelipeac.improov.base.equalTo
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
            mockSaveFirstTimeAddUseCase,
            mockSaveFirsTimeListUseCass,
            mockGetNameUseCase
        )
    }

    @Test
    fun `GIVEN saveWelcome is successful WHEN onSaveWelcome is called THEN a Unit is returned`() {
        // given
        val booleanValue = true

        given(runBlocking { mockSaveWelcomeUseCase(booleanValue) })
            .willReturn(Unit)

        // when
        profileViewModel.saveWelcome(booleanValue)

        // then
        profileViewModel.saved.value equalTo Unit
    }

    @Test
    fun `GIVEN saveFirstTimeAdd is successful WHEN onSaveFirstTimeAdd is called THEN a Unit is returned`() {
        // given
        val booleanValue = true

        given(runBlocking { mockSaveFirstTimeAddUseCase(booleanValue) })
            .willReturn(Unit)

        // when
        profileViewModel.saveFirstTimeAdd(booleanValue)

        // then
        profileViewModel.saved.value equalTo Unit
    }

    @Test
    fun `GIVEN saveFirstTimeList is successful WHEN onSaveFirstTimeList is called THEN a Unit is returned`() {
        // given
        val booleanValue = true

        given(runBlocking { mockSaveFirsTimeListUseCass(booleanValue) })
            .willReturn(Unit)

        // when
        profileViewModel.saveFirstTimeList(booleanValue)

        // then
        profileViewModel.saved.value equalTo Unit
    }

    @Test
    fun `GIVEN getName is successful WHEN loadData is called THEN a name is returned`() {
        // given
        val userName = "User Name"

        given(runBlocking { mockGetNameUseCase() })
            .willReturn(userName)

        // when
        profileViewModel.loadData()

        // then
        profileViewModel.name.value equalTo userName
    }
}
