package com.rafaelfelipeac.improov.core.extension

import androidx.core.content.ContextCompat
import com.hookedonplay.decoviewlib.DecoView
import com.hookedonplay.decoviewlib.charts.SeriesItem
import com.hookedonplay.decoviewlib.events.DecoEvent
import com.rafaelfelipeac.improov.R

const val DURATION_ANIMATION = 400L
const val LINE_WIDTH = 11f
const val TOTAL_ANGLE = 300
const val ROTATE_ANGLE = 0
const val MIN_VALUE_DEFAULT = 0F
const val MAX_VALUE_DEFAULT = 100F
const val INITIAL_VALUE_DEFAULT = 100F

fun DecoView.resetValue(
    minValue: Float?,
    maxValue: Float?,
    initialValue: Float?,
    screenMultiplier: Float?
): Int {
    configureAngles(TOTAL_ANGLE, ROTATE_ANGLE)

    return addSeries(setupArcViewAndSeriesItem(minValue, maxValue, initialValue, screenMultiplier))
}

fun DecoView.setupArcViewAndSeriesItem(
    minValue: Float?,
    maxValue: Float?,
    initialValue: Float?,
    screenMultiplier: Float?
): SeriesItem {
    addSeries(
        SeriesItem.Builder(ContextCompat.getColor(context, R.color.colorPrimaryAnother))
            .setRange(MIN_VALUE_DEFAULT, MAX_VALUE_DEFAULT, INITIAL_VALUE_DEFAULT)
            .setInitialVisibility(true)
            .setLineWidth(screenMultiplier?.times(LINE_WIDTH) ?: 0F)
            .build()
    )

    return SeriesItem.Builder(ContextCompat.getColor(context, R.color.colorPrimary))
        .setRange(minValue ?: 0F, maxValue ?: 0F, initialValue ?: 0F)
        .setInitialVisibility(false)
        .setLineWidth(screenMultiplier?.times(LINE_WIDTH) ?: 0F)
        .build()
}

fun DecoView.setup(value: Float?, index: Int) {
    addEvent(
        DecoEvent.Builder(value ?: 0F)
            .setIndex(index)
            .setDuration(DURATION_ANIMATION)
            .build()
    )
}
