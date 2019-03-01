package com.rafaelfelipeac.domore.extension

fun Float.getNumberInRightFormat(): String {
    if (this.toString().endsWith(".0")) {
        return this.toString().replace(".0", "")
    }

    return this.toString()
}