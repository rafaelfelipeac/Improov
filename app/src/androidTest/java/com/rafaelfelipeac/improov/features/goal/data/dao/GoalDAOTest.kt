package com.rafaelfelipeac.improov.features.goal.data.dao

import androidx.test.runner.AndroidJUnit4
import com.rafaelfelipeac.improov.base.BaseInstTest
import com.rafaelfelipeac.improov.base.DataProviderAndroidTest.createGoalDataModel
import com.rafaelfelipeac.improov.base.DataProviderAndroidTest.shouldBeEqualTo
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GoalDAOTest : BaseInstTest() {

    private val goalDAO get() = roomDatabase.goalDAO()

    @Test
    fun givenANewGoalWhenGetIsCalledThenTheSameGoalIsReturned() {
        // given
        val goalId = 1L
        val goal = createGoalDataModel(goalId)

        goalDAO.save(goal)

        // when
        val result = goalDAO.get(goalId)

        // then
        result shouldBeEqualTo goal
    }

    @Test
    fun givenTwoNewGoalsWhenGetAllIsCalledThenAListWithTheTwoGoalsAreReturned() {
        // given
        val goalA = createGoalDataModel(goalId = 1, name = "goalA")
        val goalB = createGoalDataModel(goalId = 2, name = "goalB")
        val list = listOf(goalA, goalB)

        goalDAO.save(goalA)
        goalDAO.save(goalB)

        // when
        val result = goalDAO.getAll()

        // then
        result shouldBeEqualTo list
    }

    @Test
    fun givenTwoNewGoalsAndDeleteTheFirstOneWhenGetAllIsCalledThenAListWithJustOneGoalIsReturned() {
        // given
        val goalA = createGoalDataModel(goalId = 1, name = "goalA")
        val goalB = createGoalDataModel(goalId = 2, name = "goalB")
        val list = mutableListOf(goalA, goalB)

        goalDAO.save(goalA)
        goalDAO.save(goalB)

        list.remove(goalA)
        goalDAO.delete(goalA)

        // when
        val result = goalDAO.getAll()

        // then
        result shouldBeEqualTo list
    }

    @Test
    fun givenADatabaseWithDataAndSaveAAGoalWhenGetIsCalledThenItMustReturnedTheGoalUpdated() {
        // given
        val goalId = 10L
        val goalA = createGoalDataModel(goalId = goalId, name = "goalA")
        goalDAO.save(goalA)

        goalA.name = "goalAUpdated"
        goalDAO.save(goalA)

        // when
        val result = goalDAO.get(goalId)

        // then
        result shouldBeEqualTo goalA
    }
}
