package com.rafaelfelipeac.mountains.extension

import androidx.core.content.ContextCompat
import com.hookedonplay.decoviewlib.DecoView
import com.hookedonplay.decoviewlib.charts.SeriesItem
import com.hookedonplay.decoviewlib.events.DecoEvent
import com.rafaelfelipeac.mountains.R

private var durationAnimation = 400L
private var lineWidth = 32F
private var lineWidthBorder = 4F
private var totalAngle = 300
private var rotateAngle = 0
private var minValueDefault = 0F
private var maxValueDefault = 100F
private var initialValueDefault = 100F

fun DecoView.resetValue(minValue: Float, maxValue: Float, initialValue: Float): Int {
    configureAngles(totalAngle, rotateAngle)

    return addSeries(setupArcViewAndSeriesItem(minValue, maxValue, initialValue))
}

fun DecoView.setupArcViewAndSeriesItem(minValue: Float, maxValue: Float, initialValue: Float): SeriesItem {
    addSeries(
        SeriesItem.Builder(ContextCompat.getColor(context!!, R.color.colorPrimaryAnother))
            .setRange(minValueDefault, maxValueDefault, initialValueDefault)
            .setInitialVisibility(true)
            .setLineWidth(lineWidth + lineWidthBorder)
            .build()
    )

    return SeriesItem.Builder(ContextCompat.getColor(context!!, R.color.colorPrimary))
        .setRange(minValue, maxValue, initialValue)
        .setInitialVisibility(false)
        .setLineWidth(lineWidth)
        .build()
}

fun DecoView.setup(value: Float, index: Int) {
    addEvent(
        DecoEvent.Builder(value)
            .setIndex(index)
            .setDuration(durationAnimation)
            .build()
    )
}
