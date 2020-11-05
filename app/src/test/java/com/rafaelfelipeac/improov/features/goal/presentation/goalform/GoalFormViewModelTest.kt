package com.rafaelfelipeac.improov.features.goal.presentation.goalform

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.rafaelfelipeac.improov.base.CoroutineRule
import com.rafaelfelipeac.improov.base.DataProviderTest.createGoal
import com.rafaelfelipeac.improov.base.equalTo
import com.rafaelfelipeac.improov.features.goal.domain.usecase.firsttimeadd.GetFirstTimeAddUseCase
import com.rafaelfelipeac.improov.features.goal.domain.usecase.firsttimeadd.SaveFirstTimeAddUseCase
import com.rafaelfelipeac.improov.features.goal.domain.usecase.firsttimelist.SaveFirstTimeListUseCase
import com.rafaelfelipeac.improov.features.goal.domain.usecase.goal.GetGoalListUseCase
import com.rafaelfelipeac.improov.features.goal.domain.usecase.goal.GetGoalUseCase
import com.rafaelfelipeac.improov.features.goal.domain.usecase.goal.SaveGoalUseCase
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
class GoalFormViewModelTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    var coroutinesTestRule = CoroutineRule()

    @get:Rule
    var rule = InstantTaskExecutorRule()

    private var mockSaveGoalUseCase = mock(SaveGoalUseCase::class.java)
    private var mockGetGoalUseCase = mock(GetGoalUseCase::class.java)
    private var mockGetGoalListUseCase = mock(GetGoalListUseCase::class.java)
    private var mockSaveFirstTimeListUseCase = mock(SaveFirstTimeListUseCase::class.java)
    private var mockSaveFirstTimeAddUseCase = mock(SaveFirstTimeAddUseCase::class.java)
    private var mockGetFirstTimeAddUseCase = mock(GetFirstTimeAddUseCase::class.java)

    private lateinit var goalFormViewModel: GoalFormViewModel

    @Before
    fun setup() {
        goalFormViewModel = GoalFormViewModel(
            mockSaveGoalUseCase,
            mockGetGoalUseCase,
            mockGetGoalListUseCase,
            mockSaveFirstTimeListUseCase,
            mockSaveFirstTimeAddUseCase,
            mockGetFirstTimeAddUseCase
        )
    }

    @Test
    fun `GIVEN saveGoal is successful WHEN saveGoal is called THEN goalId is returned`() {
        // given
        val goalId = 1L
        val goal = createGoal(goalId)

        given(runBlocking { mockSaveGoalUseCase(goal) })
            .willReturn(goalId)

        // when
        goalFormViewModel.saveGoal(goal)

        // then
        goalFormViewModel.savedGoal.value equalTo goalId
    }

    @Test
    fun `GIVEN getGoal is successful WHEN loadData is called THEN return the specific goal`() {
        // given
        val goalId = 1L
        val goal = createGoal(goalId)

        given(runBlocking { mockGetGoalUseCase(goalId) })
            .willReturn(goal)

        // when
        goalFormViewModel.setGoalId(goalId)
        goalFormViewModel.loadData()

        // then
        goalFormViewModel.goal.value equalTo goal
    }

    @Test
    fun `GIVEN getGoalList is successful WHEN loadData is called THEN return a list of goals`() {
        // given
        val goals = listOf(createGoal(1), createGoal(2), createGoal(3))

        given(runBlocking { mockGetGoalListUseCase() })
            .willReturn(goals)

        // when
        goalFormViewModel.loadData()

        // then
        goalFormViewModel.goals.value equalTo goals
    }

    @Test
    fun `GIVEN saveFirstTimeList is successful WHEN saveFirstTimeList is called THEN a Unit is returned`() {
        // given
        val booleanValue = true

        given(runBlocking { mockSaveFirstTimeListUseCase(booleanValue) })
            .willReturn(Unit)

        // when
        goalFormViewModel.saveFirstTimeList(booleanValue)

        // then
        goalFormViewModel.savedFirstTimeList.value equalTo Unit
    }

    @Test
    fun `GIVEN saveFirstTimeAdd is successful WHEN saveFirstTimeAdd is called THEN a Unit is returned`() {
        // given
        val booleanValue = true

        given(runBlocking { mockSaveFirstTimeAddUseCase(booleanValue) })
            .willReturn(Unit)

        // when
        goalFormViewModel.saveFirstTimeAdd(booleanValue)

        // then
        goalFormViewModel.savedFirstTimeAdd.value equalTo Unit
    }

    @Test
    fun `GIVEN getFirstTimeAdd is successful WHEN loadData is called THEN a boolean is returned`() {
        // given
        val booleanValue = true

        given(runBlocking { mockGetFirstTimeAddUseCase() })
            .willReturn(booleanValue)

        // when
        goalFormViewModel.loadData()

        // then
        goalFormViewModel.firstTimeAdd.value equalTo booleanValue
    }
}
