package com.rafaelfelipeac.improov.features.goal.domain.rule

import com.rafaelfelipeac.improov.features.commons.domain.model.Goal
import java.util.Date

object GoalRules {

    private const val PERCENT_MULTIPLIER = 100F

    fun calculatePercentage(goal: Goal): Float {
        val maxValue = if (goal.divideAndConquer) {
            goal.goldValue
        } else {
            goal.singleValue
        }

        return if (maxValue <= 0F) {
            0F
        } else {
            (goal.value / maxValue) * PERCENT_MULTIPLIER
        }
    }

    fun isComplete(goal: Goal): Boolean {
        val maxValue = if (goal.divideAndConquer) {
            goal.goldValue
        } else {
            goal.singleValue
        }

        return maxValue > 0F && goal.value >= maxValue
    }

    fun shouldConfirmCompletion(goal: Goal): Boolean {
        return !goal.done && !isComplete(goal)
    }

    fun hasValidDivideAndConquerValues(
        bronzeValue: Float,
        silverValue: Float,
        goldValue: Float
    ): Boolean {
        return goldValue > silverValue && silverValue > bronzeValue
    }

    fun hasValidCounterValues(
        incrementValue: Float,
        decrementValue: Float
    ): Boolean {
        return incrementValue > 0F && decrementValue > 0F
    }

    fun swapOrder(firstGoal: Goal, secondGoal: Goal) {
        val currentOrder = firstGoal.order
        firstGoal.order = secondGoal.order
        secondGoal.order = currentOrder
    }

    fun createNextOrder(loadedGoalsCount: Int): Int {
        return if (loadedGoalsCount == 0) {
            0
        } else {
            loadedGoalsCount + 1
        }
    }

    fun toggleDone(goal: Goal, date: Date): Goal {
        goal.done = !goal.done

        if (!goal.done) {
            goal.undoneDate = date
        }

        return goal
    }
}
