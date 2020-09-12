package com.rafaelfelipeac.improov.core.platform.base

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import androidx.fragment.app.DialogFragment

open class BaseDialog : DialogFragment() {

    fun hideTitle() {
        dialog?.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            requestFeature(Window.FEATURE_NO_TITLE)
        }
    }
}
