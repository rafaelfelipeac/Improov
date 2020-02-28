package com.rafaelfelipeac.improov.core.extension

fun Float.getNumberInRightFormat(): String {
    if (this.toString().endsWith(".0")) {
        return this.toString().replace(".0", "")
    }

    return ("%.2f".format(this.toString().toFloat())).replace(",", ".")
}

fun Float.getValueWithSymbol(): String {
    if (this > 0) {
        return String.format("%s%s", "+", this.getNumberInRightFormat())
    }

    return String.format(" %s", this.getNumberInRightFormat())
}