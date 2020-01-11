package com.rafaelfelipeac.improov.features.commons

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.rafaelfelipeac.improov.R
import kotlinx.android.synthetic.main.fragment_dialog_one_button.*

class DialogOneButton : DialogFragment() {

    private var onClickListener: OnClickListener? = null

    private var message = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view =  inflater.inflate(R.layout.fragment_dialog_one_button, container, false)

        if (dialog != null && dialog?.window != null) {
            dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        one_button_ok_button.setOnClickListener { onClickListener?.onOK() }

        one_button_message.text = message
    }

    fun setOnClickListener(onOkClickListener: OnClickListener) {
        this.onClickListener = onOkClickListener
    }

    fun setMessage(message: String) {
        this.message = message
    }

    interface OnClickListener {
        fun onOK()
    }
}
