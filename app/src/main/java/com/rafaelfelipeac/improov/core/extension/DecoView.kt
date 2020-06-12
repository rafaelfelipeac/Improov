package com.rafaelfelipeac.improov.core.extension

import androidx.core.content.ContextCompat
import com.hookedonplay.decoviewlib.DecoView
import com.hookedonplay.decoviewlib.charts.SeriesItem
import com.hookedonplay.decoviewlib.events.DecoEvent
import com.rafaelfelipeac.improov.R

const val DURATION_ANIMATION = 400L
const val LINE_WIDTH = 32F
const val LINE_WIDTH_BORDER = 4F
const val TOTAL_ANGLE = 300
const val ROTATE_ANGLE = 0
const val MIN_VALUE_DEFAULT = 0F
const val MAX_VALUE_DEFAULT = 100F
const val INITIAL_VALUE_DEFAULT = 100F

fun DecoView.resetValue(minValue: Float, maxValue: Float, initialValue: Float): Int {
    configureAngles(TOTAL_ANGLE, ROTATE_ANGLE)

    return addSeries(setupArcViewAndSeriesItem(minValue, maxValue, initialValue))
}

fun DecoView.setupArcViewAndSeriesItem(
    minValue: Float,
    maxValue: Float,
    initialValue: Float
): SeriesItem {
    addSeries(
        SeriesItem.Builder(ContextCompat.getColor(context!!, R.color.colorPrimaryAnother))
            .setRange(MIN_VALUE_DEFAULT, MAX_VALUE_DEFAULT, INITIAL_VALUE_DEFAULT)
            .setInitialVisibility(true)
            .setLineWidth(LINE_WIDTH + LINE_WIDTH_BORDER)
            .build()
    )

    return SeriesItem.Builder(ContextCompat.getColor(context!!, R.color.colorPrimary))
        .setRange(minValue, maxValue, initialValue)
        .setInitialVisibility(false)
        .setLineWidth(LINE_WIDTH)
        .build()
}

fun DecoView.setup(value: Float, index: Int) {
    addEvent(
        DecoEvent.Builder(value)
            .setIndex(index)
            .setDuration(DURATION_ANIMATION)
            .build()
    )
}
