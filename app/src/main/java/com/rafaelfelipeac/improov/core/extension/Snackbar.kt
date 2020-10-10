package com.rafaelfelipeac.improov.core.extension

import android.content.res.Resources
import android.view.View
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import com.rafaelfelipeac.improov.R

fun Snackbar.setMessageColor(color: Int): Snackbar = apply {
    val textView = view.findViewById(R.id.snackbar_text) as TextView
    textView.setTextColor(color)
}

fun Snackbar.setIcon(resources: Resources, hasIcon: Boolean): Snackbar = apply {
    if (hasIcon) {
        val snackbarTextView = view.findViewById<View>(
            com.google.android.material.R.id.snackbar_text
        ) as TextView
        snackbarTextView.setCompoundDrawablesWithIntrinsicBounds(
            R.drawable.ic_snackbar_trophy, 0, 0, 0
        )
        snackbarTextView.compoundDrawablePadding = resources.getDimensionPixelOffset(
            R.dimen.snackbar_offset
        )
    }
}
