package com.rafaelfelipeac.improov.core.extension

import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
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

fun TextInputEditText.checkIfFieldIsEmptyOrZero(): Boolean {
    return isEmpty() || text.toString() == "0" || text.toString() == " "
}

fun TextInputEditText.fieldIsEmptyOrZero(fragment: BaseFragment, showSnackbar: Boolean = true) {
    when {
        isEmpty() -> {
            if (showSnackbar) {
                fragment.showSnackBarLong(fragment.getString(R.string.goal_form_single_value_empty))
            }

            requestFocus()
        }
        text.toString() == "0" || text.toString() == " "-> {
            if (showSnackbar) {
                fragment.showSnackBarLong(fragment.getString(R.string.goal_form_single_value_zero))
            }

            requestFocus()
        }
    }
}