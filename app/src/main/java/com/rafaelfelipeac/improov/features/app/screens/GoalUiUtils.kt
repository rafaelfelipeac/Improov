package com.rafaelfelipeac.improov.features.app.screens

import com.rafaelfelipeac.improov.features.commons.data.enums.GoalType
import com.rafaelfelipeac.improov.features.commons.domain.model.Goal

internal fun goalTarget(goal: Goal?): Float {
    if (goal == null) {
        return 0F
    }

    return if (goal.divideAndConquer) {
        goal.goldValue
    } else {
        goal.singleValue
    }
}

internal fun goalTypeLabel(type: GoalType): String {
    return when (type) {
        GoalType.GOAL_LIST -> "List"
        GoalType.GOAL_COUNTER -> "Counter"
        GoalType.GOAL_FINAL -> "Final value"
        GoalType.GOAL_NONE -> "None"
    }
}
