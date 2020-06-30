package com.rafaelfelipeac.improov.core.extension

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.fragment.app.Fragment

const val VIBRATE_SECONDS = 20L

fun Fragment.vibrate() {
    val vibrator = context?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        vibrator.vibrate(VibrationEffect.createOneShot(VIBRATE_SECONDS, VibrationEffect.DEFAULT_AMPLITUDE))
    } else {
        @Suppress("DEPRECATION")
        vibrator.vibrate(VIBRATE_SECONDS)
    }
}
