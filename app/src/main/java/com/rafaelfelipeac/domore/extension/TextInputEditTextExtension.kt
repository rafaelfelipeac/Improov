package com.rafaelfelipeac.domore.extension

import com.google.android.material.textfield.TextInputEditText

fun TextInputEditText.ifNotEmptyReturnValue() : String {
    if (this.text?.isNotEmpty()!!)
        return this.text.toString()
    return ""
}