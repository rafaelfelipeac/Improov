package com.rafaelfelipeac.mountains.extension

import androidx.core.content.ContextCompat
import com.hookedonplay.decoviewlib.DecoView
import com.hookedonplay.decoviewlib.charts.SeriesItem
import com.rafaelfelipeac.mountains.R

fun DecoView.resetValue(minValue: Float, maxValue: Float, initialValue: Float): Int {
    configureAngles(300, 0)

    return addSeries(setupArcViewAndSeriesItem(minValue, maxValue, initialValue, false))
}

fun DecoView.setupArcViewAndSeriesItem(minValue: Float, maxValue: Float, initialValue: Float, visibility: Boolean): SeriesItem {
    addSeries(
        SeriesItem.Builder(ContextCompat.getColor(context!!, R.color.colorPrimaryAnother))
            .setRange(0F, 100F, 100F)
            .setInitialVisibility(true)
            .setLineWidth(32F + 4F)
            .build()
    )

    return SeriesItem.Builder(ContextCompat.getColor(context!!, R.color.colorPrimary))
        .setRange(minValue, maxValue, initialValue)
        .setInitialVisibility(visibility)
        .setLineWidth(32F)
        .build()
}