package com.rafaelfelipeac.domore.extension

import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import com.rafaelfelipeac.domore.R

fun Snackbar.setMessageColor(color: Int): Snackbar {
    val textView = view.findViewById(R.id.snackbar_text) as TextView
    textView.setTextColor(color)

    return this
}