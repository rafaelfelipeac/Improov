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
    fun `GIVEN a list of goals WHEN use getGoalListUseCase THEN return the same list of goals`() {
        runBlocking {
            // given
            val goals = listOf(createGoal(1), createGoal(2), createGoal(3))
            given(mockGoalRepository.getGoals())
                .willReturn(goals)

            // when
            val result = getGoalListUseCase.execute()

            // then
            result shouldBeEqualTo goals
        }
    }

    // test with order
}
