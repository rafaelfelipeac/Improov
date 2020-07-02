package com.rafaelfelipeac.improov.features.goal.presentation.goalform

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.rafaelfelipeac.improov.base.CoroutineRule
import com.rafaelfelipeac.improov.base.DataProviderTest.createGoal
import com.rafaelfelipeac.improov.base.DataProviderTest.createHistoric
import com.rafaelfelipeac.improov.base.DataProviderTest.createItem
import com.rafaelfelipeac.improov.base.DataProviderTest.shouldBeEqualTo
import com.rafaelfelipeac.improov.features.goal.domain.model.Goal
import com.rafaelfelipeac.improov.features.goal.domain.usecase.firsttimeadd.GetFirstTimeAddUseCase
import com.rafaelfelipeac.improov.features.goal.domain.usecase.firsttimeadd.SaveFirstTimeAddUseCase
import com.rafaelfelipeac.improov.features.goal.domain.usecase.firsttimelist.SaveFirstTimeListUseCase
import com.rafaelfelipeac.improov.features.goal.domain.usecase.goal.GetGoalListUseCase
import com.rafaelfelipeac.improov.features.goal.domain.usecase.goal.GetGoalUseCase
import com.rafaelfelipeac.improov.features.goal.domain.usecase.goal.SaveGoalUseCase
import com.rafaelfelipeac.improov.features.goal.domain.usecase.historic.GetHistoricListUseCase
import com.rafaelfelipeac.improov.features.goal.domain.usecase.item.GetItemListUseCase
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
    private var mockGetItemListUseCase = mock(GetItemListUseCase::class.java)
    private var mockGetHistoricListUseCase = mock(GetHistoricListUseCase::class.java)
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
            mockGetItemListUseCase,
            mockGetHistoricListUseCase,
            mockSaveFirstTimeListUseCase,
            mockSaveFirstTimeAddUseCase,
            mockGetFirstTimeAddUseCase
        )
    }

    @Test
    fun `GIVEN saveGoal is successful WHEN onSaveGoal is called THEN true is returned`() {
        // given
        val goalId = 1L
        val goal = createGoal(goalId)

        given(runBlocking { mockSaveGoalUseCase(goal) })
            .willReturn(goalId)

        // when
        goalFormViewModel.onSaveGoal(goal)

        // then
        goalFormViewModel.stateLiveData.value shouldBeEqualTo GoalFormViewModel.ViewState(
            goalSaved = true,
            goal = Goal(),
            goals = listOf(),
            items = listOf(),
            historics = listOf(),
            firstTimeAddSaved = false,
            firstTimeListSaved = false,
            firstTimeAddLoaded = false
        )
    }

    @Test
    fun `GIVEN getGoal is successful WHEN getGoal is called THEN return the specific goal`() {
        // given
        val goalId = 1L
        val goal = createGoal(goalId)

        given(runBlocking { mockGetGoalUseCase(goalId) })
            .willReturn(goal)

        // when
        goalFormViewModel.setGoalId(goalId)
        goalFormViewModel.loadData()

        // then
        goalFormViewModel.stateLiveData.value shouldBeEqualTo GoalFormViewModel.ViewState(
            goalSaved = false,
            goal = goal,
            goals = listOf(),
            items = listOf(),
            historics = listOf(),
            firstTimeAddSaved = false,
            firstTimeListSaved = false,
            firstTimeAddLoaded = false
        )
    }

    @Test
    fun `GIVEN getGoalList is successful WHEN getGoals is called THEN return a list of goals`() {
        // given
        val goals = listOf(createGoal(1), createGoal(2), createGoal(3))

        given(runBlocking { mockGetGoalListUseCase() })
            .willReturn(goals)

        // when
        goalFormViewModel.loadData()

        // then
        goalFormViewModel.stateLiveData.value shouldBeEqualTo GoalFormViewModel.ViewState(
            goalSaved = false,
            goal = Goal(),
            goals = goals,
            items = listOf(),
            historics = listOf(),
            firstTimeAddSaved = false,
            firstTimeListSaved = false,
            firstTimeAddLoaded = false
        )
    }

    @Test
    fun `GIVEN getItemList is successful WHEN getItems is called THEN return a list of items`() {
        // given
        val goalId = 1L
        val items = listOf(
            createItem(itemId = 1, goalId = goalId),
            createItem(itemId = 2, goalId = goalId),
            createItem(itemId = 3, goalId = goalId)
        )

        given(runBlocking { mockGetItemListUseCase(goalId) })
            .willReturn(items)

        // when
        goalFormViewModel.setGoalId(goalId)
        goalFormViewModel.loadData()

        // then
        goalFormViewModel.stateLiveData.value shouldBeEqualTo GoalFormViewModel.ViewState(
            goalSaved = false,
            goal = Goal(),
            goals = listOf(),
            items = items,
            historics = listOf(),
            firstTimeAddSaved = false,
            firstTimeListSaved = false,
            firstTimeAddLoaded = false
        )
    }

    @Test
    fun `GIVEN getHistoricList is successful WHEN getHistoric is called THEN return a list of historics`() {
        // given
        val goalId = 1L
        val historics = listOf(
            createHistoric(historicId = 1, goalId = goalId),
            createHistoric(historicId = 2, goalId = goalId),
            createHistoric(historicId = 3, goalId = goalId)
        )

        given(runBlocking { mockGetHistoricListUseCase(goalId) })
            .willReturn(historics)

        // when
        goalFormViewModel.setGoalId(goalId)
        goalFormViewModel.loadData()

        // then
        goalFormViewModel.stateLiveData.value shouldBeEqualTo GoalFormViewModel.ViewState(
            goalSaved = false,
            goal = Goal(),
            goals = listOf(),
            items = listOf(),
            historics = historics,
            firstTimeAddSaved = false,
            firstTimeListSaved = false,
            firstTimeAddLoaded = false
        )
    }

    @Test
    fun `GIVEN saveFirstTimeList is successful WHEN onSaveFirstTimeList is called THEN true is returned`() {
        // given
        val booleanValue = true

        given(runBlocking { mockSaveFirstTimeListUseCase(booleanValue) })
            .willReturn(Unit)

        // when
        goalFormViewModel.onSaveFirstTimeList(booleanValue)

        // then
        goalFormViewModel.stateLiveData.value shouldBeEqualTo GoalFormViewModel.ViewState(
            goalSaved = false,
            goal = Goal(),
            goals = listOf(),
            items = listOf(),
            historics = listOf(),
            firstTimeAddSaved = false,
            firstTimeListSaved = true,
            firstTimeAddLoaded = false
        )
    }

    @Test
    fun `GIVEN saveFirstTimeAdd is successful WHEN onSaveFirstTimeAdd is called THEN true is returned`() {
        // given
        val booleanValue = true

        given(runBlocking { mockSaveFirstTimeAddUseCase(booleanValue) })
            .willReturn(Unit)

        // when
        goalFormViewModel.onSaveFirstTimeAdd(booleanValue)

        // then
        goalFormViewModel.stateLiveData.value shouldBeEqualTo GoalFormViewModel.ViewState(
            goalSaved = false,
            goal = Goal(),
            goals = listOf(),
            items = listOf(),
            historics = listOf(),
            firstTimeAddSaved = true,
            firstTimeListSaved = false,
            firstTimeAddLoaded = false
        )
    }

    @Test
    fun `GIVEN getFirstTimeAdd is successful WHEN getFirstTimeAdd is called THEN a boolean value is returned`() {
        // given
        val booleanValue = true

        given(runBlocking { mockGetFirstTimeAddUseCase() })
            .willReturn(booleanValue)

        // when
        goalFormViewModel.loadData()

        // then
        goalFormViewModel.stateLiveData.value shouldBeEqualTo GoalFormViewModel.ViewState(
            goalSaved = false,
            goal = Goal(),
            goals = listOf(),
            items = listOf(),
            historics = listOf(),
            firstTimeAddSaved = false,
            firstTimeListSaved = false,
            firstTimeAddLoaded = booleanValue
        )
    }
}
