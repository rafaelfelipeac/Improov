package com.rafaelfelipeac.improov.features.goal.presentation.goallist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.rafaelfelipeac.improov.base.CoroutineRule
import com.rafaelfelipeac.improov.base.DataProviderTest.createGoal
import com.rafaelfelipeac.improov.base.equalTo
import com.rafaelfelipeac.improov.features.goal.domain.usecase.firsttimelist.GetFirstTimeListUseCase
import com.rafaelfelipeac.improov.features.goal.domain.usecase.firsttimelist.SaveFirstTimeListUseCase
import com.rafaelfelipeac.improov.features.goal.domain.usecase.goal.GetGoalListUseCase
import com.rafaelfelipeac.improov.features.goal.domain.usecase.goal.SaveGoalUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
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
    fun `GIVEN saveGoal is successful WHEN saveGoal is called THEN the goalId is returned`() {
        // given
        val goalId = 1L
        val goal = createGoal(goalId)

        given(runBlocking { mockSaveGoalUseCase(goal) })
            .willReturn(goalId)

        // when
        goalListViewModel.saveGoal(goal)

        // then
        runBlocking {
            goalListViewModel.savedGoal.first() equalTo goalId
        }
    }

    @Test
    fun `GIVEN getGoaList is successful WHEN loadData is called THEN a list of goals must be returned`() {
        // given
        val goal = createGoal()
        val goals = listOf(goal, goal, goal)

        given(runBlocking { mockGetGoalListUseCase() })
            .willReturn(goals)

        // when
        goalListViewModel.loadData()

        // then
        runBlocking {
            goalListViewModel.goals.first() equalTo goals
        }
    }

    @Test
    fun `GIVEN saveFirstTimeList is successful WHEN saveFirstTimeList is called THEN a Unit is returned`() {
        // given
        val booleanValue = false

        given(runBlocking { mockSaveFirstTimeListUseCase(booleanValue) })
            .willReturn(Unit)

        // when
        goalListViewModel.saveFirstTimeList(booleanValue)

        // then
        runBlocking {
            goalListViewModel.savedFirstTimeList.first() equalTo Unit
        }
    }

    @Test
    fun `GIVEN getFirstTimeList is successful WHEN loadData is called THEN a boolean value must be returned`() {
        // given
        val booleanValue = false

        given(runBlocking { mockGetFirstTimeListUseCase() })
            .willReturn(booleanValue)

        // when
        goalListViewModel.loadData()

        // then
        runBlocking {
            goalListViewModel.firstTimeList.first() equalTo booleanValue
        }
    }
}
