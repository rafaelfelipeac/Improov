package com.rafaelfelipeac.improov.core.extension

fun Float.getNumberInExhibitionFormat(): String {
    return this.toString().let { string ->
        if (string.endsWith(".0")) {
            string.replace(".0", "")
        }

        ("%.2f".format(this)).replace(",", ".")
    }
}

fun Float.getValueWithSymbol(): String = if (this > 0) {
    "+ ${getNumberInExhibitionFormat()}"
} else {
    " ${getNumberInExhibitionFormat()}"
}
