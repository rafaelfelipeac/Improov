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

// Esse nome é um pouco confuso visto que segue o padrão de getter pra boolean, mas isso não é um getter
fun View.isVisible(isVisible: Boolean) {
    if (isVisible) {
        visible()
    } else {
        invisible()
    }
}
