package com.rafaelfelipeac.mountains.core.extension

import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
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

fun TextInputEditText.emailIsInvalid(): Boolean {
    return !android.util.Patterns.EMAIL_ADDRESS.matcher(this.text.toString()).matches()
}

fun TextInputEditText.showOrHidePassword() {
    if (transformationMethod == HideReturnsTransformationMethod.getInstance()) {
        transformationMethod = PasswordTransformationMethod.getInstance()
    } else {
        transformationMethod = HideReturnsTransformationMethod.getInstance()
    }

    setSelection(text?.length!!)
}
