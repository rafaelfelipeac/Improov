package com.rafaelfelipeac.mountains.features.commons

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.rafaelfelipeac.mountains.R
import kotlinx.android.synthetic.main.fragment_two_buttons_dialog.*

class TwoButtonsDialog : DialogFragment() {

    private var onClickListener: OnClickListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view =  inflater.inflate(R.layout.fragment_two_buttons_dialog, container, false)

        if (dialog != null && dialog?.window != null) {
            dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        two_buttons_cancel_button.setOnClickListener { onClickListener?.onCancel() }

        two_buttons_ok_button.setOnClickListener { onClickListener?.onOK() }
    }

    fun setOnClickListener(onOkClickListener: OnClickListener) {
        this.onClickListener = onOkClickListener
    }

    interface OnClickListener {
        fun onCancel()
        fun onOK()
    }
}
