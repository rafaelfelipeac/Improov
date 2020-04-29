package com.rafaelfelipeac.improov.features.goal.data.model

import com.rafaelfelipeac.improov.base.DataProvider.createGoal
import com.rafaelfelipeac.improov.base.DataProvider.createGoalDataModel
import com.rafaelfelipeac.improov.base.DataProvider.shouldBeEqualTo
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GoalDataModelTest {

    private val mapper = GoalDataModelMapper()

    @Test
    fun `GIVEN goalDataModel WHEN map is called THEN a goal is returned`() {
        // given
        val goalDataModel = createGoalDataModel()

        // when
        val goal = goalDataModel.let { mapper.map(goalDataModel) }

        // then
        goal shouldBeEqualTo createGoal()
    }

    @Test
    fun `GIVEN goalDataModel with custom goalId and name WHEN map is called THEN a goal with the same goalId and name is returned`() {
        // given
        val goalDataModel = createGoalDataModel(goalId = 123, name = "goal123")

        // when
        val goal = goalDataModel.let { mapper.map(goalDataModel) }

        // then
        goal shouldBeEqualTo createGoal(goalId = 123, name = "goal123")
    }

    @Test
    fun `GIVEN goal WHEN mapReverse is called THEN goalDataModel is returned`() {
        // given
        val goal = createGoal()

        // when
        val goalDataModel = goal.let { mapper.mapReverse(goal) }

        // then
        goalDataModel shouldBeEqualTo createGoalDataModel()
    }

    @Test
    fun `GIVEN goal with custom goalId and name WHEN mapReverse is called THEN goalDataModel with the same goalId and name is returned`() {
        // given
        val goal = createGoal(goalId = 123, name = "goal123")

        // when
        val goalDataModel = goal.let { mapper.mapReverse(goal) }

        // then
        goalDataModel shouldBeEqualTo createGoalDataModel(goalId = 123, name = "goal123")
    }
}
