package com.rafaelfelipeac.improov.features.goal.presentation.goallist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.rafaelfelipeac.improov.base.CoroutineRule
import com.rafaelfelipeac.improov.base.DataProvider.createGoal
import com.rafaelfelipeac.improov.base.DataProvider.shouldBeEqualTo
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

    private lateinit var goalListViewModel: GoalListViewModel

    @Before
    fun setup() {
        goalListViewModel = GoalListViewModel(mockSaveGoalUseCase, mockGetGoalListUseCase)
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
            true,
            listOf()
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
            false,
            listOf(
                goal,
                goal,
                goal
            )
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
            true,
            list
        )
    }
}
