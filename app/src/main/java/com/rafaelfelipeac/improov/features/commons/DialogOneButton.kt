package com.rafaelfelipeac.improov.features.commons

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rafaelfelipeac.improov.R
import com.rafaelfelipeac.improov.core.platform.base.BaseDialog
import kotlinx.android.synthetic.main.fragment_dialog_one_button.*

class DialogOneButton : BaseDialog() {

    private var onClickListener: OnClickListener? = null

    private var message: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        setScreen()

        return inflater.inflate(R.layout.fragment_dialog_one_button, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog_one_button_ok_button.setOnClickListener { onClickListener?.onOK() }

        dialog_one_button_message.text = message
    }

    private fun setScreen() {
        hideTitle()
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
