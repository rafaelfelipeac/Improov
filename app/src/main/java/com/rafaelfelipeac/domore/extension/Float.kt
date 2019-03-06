package com.rafaelfelipeac.domore.extension

fun Float.getNumberInRightFormat(): String {
    if (this.toString().endsWith(".0")) {
        return this.toString().replace(".0", "")
    }

    return this.toString()
}

fun Float.getValueWithSymbol(): String {
    if (this > 0) {
        return String.format("%s%s", "+", this.getNumberInRightFormat())
    }

    return String.format(" %s", this.getNumberInRightFormat())
}