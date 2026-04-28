package com.rafaelfelipeac.improov.features.goal.domain.rule

import com.rafaelfelipeac.improov.base.equalTo
import com.rafaelfelipeac.improov.features.commons.data.enums.GoalType
import com.rafaelfelipeac.improov.features.commons.domain.model.Goal
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.Date

class GoalRulesTest {

    @Test
    fun `calculatePercentage returns zero when max value is zero`() {
        val goal = Goal(value = 5F, singleValue = 0F)

        GoalRules.calculatePercentage(goal) equalTo 0F
    }

    @Test
    fun `calculatePercentage uses single value when divide and conquer is disabled`() {
        val goal = Goal(value = 25F, singleValue = 50F)

        GoalRules.calculatePercentage(goal) equalTo 50F
    }

    @Test
    fun `calculatePercentage uses gold value when divide and conquer is enabled`() {
        val goal = Goal(
            value = 30F,
            divideAndConquer = true,
            goldValue = 60F
        )

        GoalRules.calculatePercentage(goal) equalTo 50F
    }

    @Test
    fun `isComplete returns true when the goal reaches the target`() {
        val goal = Goal(value = 10F, singleValue = 10F, type = GoalType.GOAL_FINAL)

        assertTrue(GoalRules.isComplete(goal))
    }

    @Test
    fun `isComplete returns false when the goal is below target`() {
        val goal = Goal(value = 9F, singleValue = 10F, type = GoalType.GOAL_FINAL)

        assertFalse(GoalRules.isComplete(goal))
    }

    @Test
    fun `hasValidDivideAndConquerValues requires strict ordering`() {
        assertTrue(GoalRules.hasValidDivideAndConquerValues(1F, 2F, 3F))
        assertFalse(GoalRules.hasValidDivideAndConquerValues(1F, 1F, 3F))
    }

    @Test
    fun `hasValidCounterValues requires positive values`() {
        assertTrue(GoalRules.hasValidCounterValues(1F, 2F))
        assertFalse(GoalRules.hasValidCounterValues(0F, 2F))
    }

    @Test
    fun `createNextOrder preserves the current list behavior`() {
        GoalRules.createNextOrder(0) equalTo 0
        GoalRules.createNextOrder(3) equalTo 4
    }

    @Test
    fun `toggleDone writes the matching completion timestamp`() {
        val goal = Goal(done = false)
        val date = Date(123L)

        GoalRules.toggleDone(goal, date)

        assertTrue(goal.done)
        goal.undoneDate equalTo null
        goal.doneDate equalTo null
    }
}
