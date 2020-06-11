package com.rafaelfelipeac.improov.features.goal.presentation.goallist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.rafaelfelipeac.improov.base.CoroutineRule
import com.rafaelfelipeac.improov.base.DataProviderTest.createGoal
import com.rafaelfelipeac.improov.base.DataProviderTest.shouldBeEqualTo
import com.rafaelfelipeac.improov.features.goal.domain.usecase.firsttimelist.GetFirstTimeListUseCase
import com.rafaelfelipeac.improov.features.goal.domain.usecase.firsttimelist.SaveFirstTimeListUseCase
import com.rafaelfelipeac.improov.features.goal.domain.usecase.goal.GetGoalListUseCase
import com.rafaelfelipeac.improov.features.goal.domain.usecase.goal.SaveGoalUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GoalListViewModelTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    var coroutinesTestRule = CoroutineRule()

    @get:Rule
    var rule = InstantTaskExecutorRule()

    private var mockSaveGoalUseCase = mock(SaveGoalUseCase::class.java)
    private var mockGetGoalListUseCase = mock(GetGoalListUseCase::class.java)
    private var mockSaveFirstTimeListUseCase = mock(SaveFirstTimeListUseCase::class.java)
    private var mockGetFirstTimeListUseCase = mock(GetFirstTimeListUseCase::class.java)

    private lateinit var goalListViewModel: GoalListViewModel

    @Before
    fun setup() {
        goalListViewModel = GoalListViewModel(
            mockSaveGoalUseCase,
            mockGetGoalListUseCase,
            mockSaveFirstTimeListUseCase,
            mockGetFirstTimeListUseCase
        )
    }

    @Test
    fun `GIVEN saveGoal is successful WHEN onSaveGoal is called THEN true is returned`() {
        // given
        val goalId = 1L
        val goal = createGoal(goalId)

        given(runBlocking { mockSaveGoalUseCase.execute(goal) })
            .willReturn(goalId)

        // when
        goalListViewModel.onSaveGoal(goal)

        // then
        goalListViewModel.stateLiveData.value shouldBeEqualTo GoalListViewModel.ViewState(
            goalSaved = true,
            goals = listOf(),
            firstTimeListSaved = false,
            firstTimeList = false
        )
    }

    @Test
    fun `GIVEN getGoaList is successful WHEN loadData is called THEN a list of goals must be returned`() {
        // given
        val goal = createGoal()
        given(runBlocking { mockGetGoalListUseCase.execute() })
            .willReturn(listOf(goal, goal, goal))

        // when
        goalListViewModel.loadData()

        // then
        goalListViewModel.stateLiveData.value shouldBeEqualTo GoalListViewModel.ViewState(
            goalSaved = false,
            goals = listOf(
                goal,
                goal,
                goal
            ),
            firstTimeListSaved = false,
            firstTimeList = false
        )
    }

    @Test
    fun `GIVEN saveGoal return success and then calling getGoalList WHEN onSaveGoal is called THEN true and a list of goals are returned`() {
        // given
        val goalId = 1L
        val goal = createGoal(goalId)
        val list = listOf(createGoal(1), createGoal(2), createGoal(3))

        given(runBlocking { mockSaveGoalUseCase.execute(goal) })
            .willReturn(goalId)
        given(runBlocking { mockGetGoalListUseCase.execute() })
            .willReturn(list)

        // when
        goalListViewModel.onSaveGoal(goal)

        // then 
        goalListViewModel.stateLiveData.value shouldBeEqualTo GoalListViewModel.ViewState(
            goalSaved = true,
            goals = list,
            firstTimeListSaved = false,
            firstTimeList = false
        )
    }

    @Test
    fun `GIVEN saveFirstTimeList is successful WHEN onSaveFirstTimeList is called THEN true is returned`() {
        // given
        val booleanValue = false

        given(runBlocking { mockSaveFirstTimeListUseCase.execute(booleanValue) })
            .willReturn(Unit)

        // when
        goalListViewModel.onSaveFirstTimeList(booleanValue)

        // then
        goalListViewModel.stateLiveData.value shouldBeEqualTo GoalListViewModel.ViewState(
            goalSaved = false,
            goals = listOf(),
            firstTimeListSaved = true,
            firstTimeList = false
        )
    }

    @Test
    fun `GIVEN getFirstTimeList is successful WHEN loadData is called THEN a boolean value must be returned`() {
        // given
        val booleanValue = false

        given(runBlocking { mockGetFirstTimeListUseCase.execute() })
            .willReturn(booleanValue)

        // when
        goalListViewModel.loadData()

        // then
        goalListViewModel.stateLiveData.value shouldBeEqualTo GoalListViewModel.ViewState(
            goalSaved = false,
            goals = listOf(),
            firstTimeListSaved = false,
            firstTimeList = booleanValue
        )
    }
}
