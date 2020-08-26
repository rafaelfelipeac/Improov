package com.rafaelfelipeac.improov.core.extension

import android.text.InputType
import com.google.android.material.textfield.TextInputEditText
import com.rafaelfelipeac.improov.R
import com.rafaelfelipeac.improov.core.platform.base.BaseFragment

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

fun TextInputEditText.checkIfFieldIsEmptyOrZero(): Boolean {
    return when {
        isEmpty() -> true
        text.toString()[0] == '0' &&
                inputType == (InputType.TYPE_NUMBER_FLAG_DECIMAL + InputType.TYPE_CLASS_NUMBER) -> {
            text.toString().toDouble() <= 0
        }
        text.toString()[0] == ' ' -> {
            setText(text.toString().replaceFirst(" ", ""))

            checkIfFieldIsEmptyOrZero()
        }
        else -> false
    }
}

fun TextInputEditText.fieldIsEmptyOrZero(fragment: BaseFragment, showSnackbar: Boolean = true) {
    when {
        isEmpty() || text.toString() == "" -> {
            if (showSnackbar) {
                fragment.showSnackBar(fragment.getString(R.string.goal_form_single_value_empty))
            }

            requestFocus()
        }
        text.toString()[0] == '0' -> {
            if (showSnackbar) {
                fragment.showSnackBar(fragment.getString(R.string.goal_form_single_value_zero))
            }

            requestFocus()
        }
        else -> {
            if (text.toString()[0] == ' ') {
                setText(text.toString().replaceFirst(" ", ""))

                fieldIsEmptyOrZero(fragment, showSnackbar)
            }
        }
    }
}
