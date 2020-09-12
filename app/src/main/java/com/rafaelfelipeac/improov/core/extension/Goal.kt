package com.rafaelfelipeac.improov.core.extension

import com.rafaelfelipeac.improov.features.goal.domain.model.Goal

const val PERCENTAGE_MAX = 100

// Normalmente eu uso esse package de extension só pras classes que não são minhas
// Nesse caso seria muito melhor se isso fosse um método da propria classe, ou então se a extension
// fosse declarada no arquivo da classe
fun Goal.getPercentage() = if (divideAndConquer) {
    (value / goldValue) * PERCENTAGE_MAX
} else {
    (value / singleValue) * PERCENTAGE_MAX
}
