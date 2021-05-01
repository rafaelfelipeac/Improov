package com.rafaelfelipeac.improov.features.profile.presentation.profile

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.rafaelfelipeac.improov.base.CoroutineRule
import com.rafaelfelipeac.improov.base.equalTo
import com.rafaelfelipeac.improov.features.profile.domain.usecase.GetNameUseCase
import com.rafaelfelipeac.improov.features.profile.domain.usecase.SaveFirstTimeAddUseCase
import com.rafaelfelipeac.improov.features.profile.domain.usecase.SaveFirstTimeListUseCase
import com.rafaelfelipeac.improov.features.profile.domain.usecase.SaveWelcomeUseCase
import com.rafaelfelipeac.improov.features.profile.domain.usecase.GenerateDataUseCase
import com.rafaelfelipeac.improov.features.profile.domain.usecase.ClearDataUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
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
    private var mockSaveFirsTimeListUseCase = mock(SaveFirstTimeListUseCase::class.java)
    private var mockGenerateDataUseCase = mock(GenerateDataUseCase::class.java)
    private var mockClearDataUseCase = mock(ClearDataUseCase::class.java)

    private lateinit var profileViewModel: ProfileViewModel

    @Before
    fun setup() {
        profileViewModel = ProfileViewModel(
            mockSaveWelcomeUseCase,
            mockSaveFirstTimeAddUseCase,
            mockSaveFirsTimeListUseCase,
            mockGetNameUseCase,
            mockGenerateDataUseCase,
            mockClearDataUseCase
        )
    }

    @Test
    fun `GIVEN saveWelcome is successful WHEN saveWelcome is called THEN a Unit is returned`() {
        // given
        val booleanValue = true

        given(runBlocking { mockSaveWelcomeUseCase(booleanValue) })
            .willReturn(Unit)

        // when
        profileViewModel.saveWelcome(booleanValue)

        // then
        runBlocking {
            profileViewModel.saved.first() equalTo Unit
        }
    }

    @Test
    fun `GIVEN saveFirstTimeAdd is successful WHEN saveFirstTimeAdd is called THEN a Unit is returned`() {
        // given
        val booleanValue = true

        given(runBlocking { mockSaveFirstTimeAddUseCase(booleanValue) })
            .willReturn(Unit)

        // when
        profileViewModel.saveFirstTimeAdd(booleanValue)

        // then
        runBlocking {
            profileViewModel.saved.first() equalTo Unit
        }
    }

    @Test
    fun `GIVEN saveFirstTimeList is successful WHEN saveFirstTimeList is called THEN a Unit is returned`() {
        // given
        val booleanValue = true

        given(runBlocking { mockSaveFirsTimeListUseCase(booleanValue) })
            .willReturn(Unit)

        // when
        profileViewModel.saveFirstTimeList(booleanValue)

        // then
        runBlocking {
            profileViewModel.saved.first() equalTo Unit
        }
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
        runBlocking {
            profileViewModel.name.first() equalTo userName
        }
    }

    @Test
    fun `GIVEN generateData is successful WHEN generateData is called THEN a Unit is returned`() {
        // given
        given(runBlocking { mockGenerateDataUseCase() })
            .willReturn(Unit)

        // when
        profileViewModel.generateData()

        // then
        runBlocking {
            profileViewModel.generated.first() equalTo Unit
        }
    }

    @Test
    fun `GIVEN clearData is successful WHEN clearData is called THEN a Unit is returned`() {
        // given
        given(runBlocking { mockClearDataUseCase() })
            .willReturn(Unit)

        // when
        profileViewModel.clearData()

        // then
        runBlocking {
            profileViewModel.clean.first() equalTo Unit
        }
    }
}
