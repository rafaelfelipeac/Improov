package com.rafaelfelipeac.mountains.extension

import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.fragment_forgot_password.*

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

fun TextInputEditText.emailIsInvalid(): Boolean {
    return !android.util.Patterns.EMAIL_ADDRESS.matcher(this.text.toString()).matches()
}
