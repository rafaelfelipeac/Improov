package com.rafaelfelipeac.improov.core.extension

import android.text.InputType
import com.google.android.material.textfield.TextInputEditText
import com.rafaelfelipeac.improov.R
import com.rafaelfelipeac.improov.core.platform.base.BaseFragment

fun TextInputEditText.toFloat(): Float = text.toString().toFloatOrNull() ?: 0F

fun TextInputEditText.resetValue() {
    setText("")
}

fun TextInputEditText.isNotEmpty(): Boolean = text?.isNotEmpty() ?: false

fun TextInputEditText.isEmpty(): Boolean = text?.isEmpty() ?: false

fun TextInputEditText.isEmptyOrZero(): Boolean {
    return text.toString().trim().let {
        it.isEmpty() || (it.firstOrNull() == '0' && (it.toDoubleOrNull() ?: 0.0) <= 0 &&
                inputType == (InputType.TYPE_NUMBER_FLAG_DECIMAL + InputType.TYPE_CLASS_NUMBER))
    }
}

fun TextInputEditText.focusOnEmptyOrZero(fragment: BaseFragment, showSnackbar: Boolean = true) {
    text.toString().trim().let { string ->
        if (string.isEmpty()) {
            if (showSnackbar) {
                fragment.showSnackBar(fragment.getString(R.string.goal_form_single_value_empty))
            }

            requestFocus()
            return
        }
        if (string.firstOrNull() == '0') {
            if (showSnackbar) {
                fragment.showSnackBar(fragment.getString(R.string.goal_form_single_value_zero))
            }

            requestFocus()
            return
        }
    }
}
