package com.rafaelfelipeac.improov.core.extension

import android.view.View

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.setVisibility(isVisible: Boolean) {
    if (isVisible) {
        visible()
    } else {
        invisible()
    }
}
