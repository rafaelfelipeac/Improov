@file:Suppress(
    "LongMethod",
    "LongParameterList",
    "CyclomaticComplexMethod",
    "MagicNumber",
    "NoUnusedImports",
    "MaximumLineLength",
    "Wrapping",
    "ArgumentListWrapping",
    "ReturnCount",
)

package com.rafaelfelipeac.improov.features.app.screens

import java.util.Date
import com.rafaelfelipeac.improov.features.commons.data.enums.GoalType
import com.rafaelfelipeac.improov.features.commons.domain.model.Goal
import com.rafaelfelipeac.improov.features.goal.domain.rule.GoalRules

internal fun buildGoalToSave(
    currentGoal: Goal?,
    goalId: Long,
    name: String,
    selectedType: GoalType,
    divideAndConquer: Boolean,
    singleValue: String,
    bronzeValue: String,
    silverValue: String,
    goldValue: String,
    incrementValue: String,
    decrementValue: String,
    goalsSize: Int,
): Goal? {
    if (name.isBlank()) {
        return null
    }
    if (selectedType == GoalType.GOAL_NONE) {
        return null
    }
    if (divideAndConquer && !isValidDivideAndConquer(bronzeValue, silverValue, goldValue)) {
        return null
    }
    if (selectedType == GoalType.GOAL_COUNTER && !isValidCounter(incrementValue, decrementValue)) {
        return null
    }

    val goal = currentGoal?.copy() ?: Goal()
    goal.name = name
    goal.type = selectedType
    goal.divideAndConquer = divideAndConquer

    if (goalId == 0L && goal.goalId == 0L) {
        goal.value = 0F
        goal.done = false
        goal.order = GoalRules.createNextOrder(goalsSize)
        goal.createdDate = Date()
    } else {
        goal.updatedDate = Date()
    }

    if (selectedType == GoalType.GOAL_COUNTER) {
        goal.incrementValue = incrementValue.toFloatOrNull() ?: 0F
        goal.decrementValue = decrementValue.toFloatOrNull() ?: 0F
    }

    if (divideAndConquer) {
        goal.bronzeValue = bronzeValue.toFloatOrNull() ?: 0F
        goal.silverValue = silverValue.toFloatOrNull() ?: 0F
        goal.goldValue = goldValue.toFloatOrNull() ?: 0F
    } else {
        goal.singleValue = singleValue.toFloatOrNull() ?: 0F
    }

    return goal
}

internal fun isValidDivideAndConquer(bronzeValue: String, silverValue: String, goldValue: String): Boolean {
    val bronze = bronzeValue.toFloatOrNull() ?: return false
    val silver = silverValue.toFloatOrNull() ?: return false
    val gold = goldValue.toFloatOrNull() ?: return false
    return GoalRules.hasValidDivideAndConquerValues(bronze, silver, gold)
}

internal fun isValidCounter(incrementValue: String, decrementValue: String): Boolean {
    val increment = incrementValue.toFloatOrNull() ?: return false
    val decrement = decrementValue.toFloatOrNull() ?: return false
    return GoalRules.hasValidCounterValues(increment, decrement)
}
