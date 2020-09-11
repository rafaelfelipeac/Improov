package com.rafaelfelipeac.improov.features.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rafaelfelipeac.improov.R
import com.rafaelfelipeac.improov.core.platform.base.BaseDialog
import kotlinx.android.synthetic.main.fragment_dialog_one_button.*

class DialogOneButton(private val message: String) : BaseDialog() {

    private var onClickListener: OnClickListener? = null

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

        dialogOneButtonOKButton.setOnClickListener { onClickListener?.onOK() }

        dialogOneButtonMessage.text = message
    }

    private fun setScreen() {
        hideTitle()
    }

    fun setOnClickListener(onOkClickListener: OnClickListener) {
        this.onClickListener = onOkClickListener
    }

    interface OnClickListener {
        fun onOK()
    }
}
