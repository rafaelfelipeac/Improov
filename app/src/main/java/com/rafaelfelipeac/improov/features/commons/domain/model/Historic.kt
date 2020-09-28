package com.rafaelfelipeac.improov.features.commons.domain.model

import java.io.Serializable
import java.util.Date

data class Historic(
    val historicId: Long = 0,
    val goalId: Long,
    val value: Float,
    val date: Date?
) : Serializable
