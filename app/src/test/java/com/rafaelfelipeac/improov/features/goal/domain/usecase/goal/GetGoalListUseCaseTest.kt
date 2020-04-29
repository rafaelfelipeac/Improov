package com.rafaelfelipeac.improov.features.goal.domain.usecase.goal

import com.rafaelfelipeac.improov.base.DataProvider.createGoal
import com.rafaelfelipeac.improov.base.DataProvider.shouldBeEqualTo
import com.rafaelfelipeac.improov.features.goal.domain.repository.GoalRepository
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GetGoalListUseCaseTest {

    @Mock
    internal lateinit var mockGoalRepository: GoalRepository

    private lateinit var getGoalListUseCase: GetGoalListUseCase

    @Before
    fun setup() {
        getGoalListUseCase = GetGoalListUseCase(mockGoalRepository)
    }

    @Test
    fun `GIVEN a list of goals with custom order WHEN use getGoalListUseCase THEN return the list of goals ordered by order parameter`() {
        runBlocking {
            // given
            val goals = listOf(
                createGoal(goalId = 1, order = 1),
                createGoal(goalId = 2, order = 2),
                createGoal(goalId = 3, order = 3),
                createGoal(goalId = 4, order = 20),
                createGoal(goalId = 5, order = 13),
                createGoal(goalId = 6, order = 5))

            val orderedGoals = goals.sortedBy { it.order }

            given(mockGoalRepository.getGoals())
                .willReturn(goals)

            // when
            val result = getGoalListUseCase.execute()

            // then
            result shouldBeEqualTo orderedGoals
        }
    }
}
