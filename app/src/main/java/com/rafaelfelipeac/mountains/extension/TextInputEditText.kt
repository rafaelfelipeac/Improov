package com.rafaelfelipeac.mountains.extension

import com.google.android.material.textfield.TextInputEditText

fun TextInputEditText.toFloat(): Float {
    return text.toString().toFloat()
}

fun TextInputEditText.resetValue() {
    setText("")
}

fun TextInputEditText.isNotEmpty(): Boolean {
    return text?.isNotEmpty()!!
}

fun TextInputEditText.isEmpty(): Boolean {
    return text?.isEmpty()!!
}

