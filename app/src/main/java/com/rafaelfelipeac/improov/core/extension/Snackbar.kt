package com.rafaelfelipeac.improov.core.extension

import android.content.res.Resources
import android.view.View
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import com.rafaelfelipeac.improov.R

fun Snackbar.setMessageColor(color: Int): Snackbar {
    val textView = view.findViewById(R.id.snackbar_text) as TextView
    textView.setTextColor(color)

    return this
}

fun Snackbar.setIcon(resources: Resources, hasIcon: Boolean): Snackbar {
    if (hasIcon) {
        val snackbarView = view
        val snackbarTextView = snackbarView
            .findViewById<View>(com.google.android.material.R.id.snackbar_text) as TextView
        snackbarTextView.setCompoundDrawablesWithIntrinsicBounds(
            R.drawable.ic_snackbar_trophy, 0, 0, 0)
        snackbarTextView
            .compoundDrawablePadding = resources.getDimensionPixelOffset(R.dimen.snackbar_offset)
    }

    return this
}
