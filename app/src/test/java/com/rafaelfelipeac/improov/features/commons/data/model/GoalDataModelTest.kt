package com.rafaelfelipeac.improov.features.commons.data.model

import com.rafaelfelipeac.improov.base.DataProviderTest.createGoal
import com.rafaelfelipeac.improov.base.DataProviderTest.createGoalDataModel
import com.rafaelfelipeac.improov.core.extension.equalTo
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
        goal equalTo createGoal()
    }

    @Test
    fun `GIVEN goalDataModel WHEN map is called THEN a goal with the same values is returned`() {
        // given
        val goalDataModel = createGoalDataModel(goalId = 123, name = "goal123")

        // when
        val goal = goalDataModel.let { mapper.map(goalDataModel) }

        // then
        goal equalTo createGoal(goalId = 123, name = "goal123")
    }

    @Test
    fun `GIVEN goal WHEN mapReverse is called THEN goalDataModel is returned`() {
        // given
        val goal = createGoal()

        // when
        val goalDataModel = goal.let { mapper.mapReverse(goal) }

        // then
        goalDataModel equalTo createGoalDataModel()
    }

    @Test
    fun `GIVEN goal WHEN mapReverse is called THEN goalDataModel with the same value is returned`() {
        // given
        val goal = createGoal(goalId = 123, name = "goal123")

        // when
        val goalDataModel = goal.let { mapper.mapReverse(goal) }

        // then
        goalDataModel equalTo createGoalDataModel(goalId = 123, name = "goal123")
    }
}
