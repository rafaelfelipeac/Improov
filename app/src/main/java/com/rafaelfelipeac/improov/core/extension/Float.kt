package com.rafaelfelipeac.improov.core.extension

// Right format pra quem??
fun Float.getNumberInRightFormat(): String {
    return this.toString().let { string ->
        if (string.endsWith(".0")) {
            string.replace(".0", "")
        }

        ("%.2f".format(this)).replace(",", ".")
    }
}

fun Float.getValueWithSymbol(): String = if (this > 0) {
    "+ ${getNumberInRightFormat()}"
} else {
    " ${getNumberInRightFormat()}"
}
