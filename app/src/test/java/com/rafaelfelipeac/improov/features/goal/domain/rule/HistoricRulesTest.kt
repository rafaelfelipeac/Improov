package com.rafaelfelipeac.improov.features.goal.domain.rule

import com.rafaelfelipeac.improov.base.equalTo
import org.junit.Test
import java.util.Date

class HistoricRulesTest {

    @Test
    fun `createIncrement keeps the provided value`() {
        val date = Date(123L)

        val historic = HistoricRules.createIncrement(
            goalId = 10L,
            value = 2.5F,
            date = date
        )

        historic.goalId equalTo 10L
        historic.value equalTo 2.5F
        historic.date equalTo date
    }

    @Test
    fun `createDecrement stores a negative value`() {
        val historic = HistoricRules.createDecrement(
            goalId = 10L,
            value = 2.5F,
            date = Date(456L)
        )

        historic.value equalTo -2.5F
    }

    @Test
    fun `createManual keeps the typed value`() {
        val historic = HistoricRules.createManual(
            goalId = 10L,
            value = -7.25F,
            date = Date(789L)
        )

        historic.value equalTo -7.25F
    }
}
