package com.rafaelfelipeac.mountains.extension

import android.content.Context
import android.widget.ImageView
import androidx.core.content.ContextCompat

fun ImageView.enableIcon(iconNormal: Int, context: Context) {
    background = ContextCompat.getDrawable(context, iconNormal)
}

fun ImageView.disableIcon(iconDark: Int, context: Context) {
    background = ContextCompat.getDrawable(context, iconDark)
}
