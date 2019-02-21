package com.rafaelfelipeac.domore.extension

import com.google.android.material.textfield.TextInputEditText

fun TextInputEditText.ifNotEmptyReturnValueElseReturnOtherValue(value: String) : String {
    if (this.text?.isNotEmpty()!!)
        return this.text.toString()
    return value
}