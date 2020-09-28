package com.rafaelfelipeac.improov.features.commons

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rafaelfelipeac.improov.R
import com.rafaelfelipeac.improov.core.platform.base.BaseDialog
import kotlinx.android.synthetic.main.fragment_dialog_two_buttons.*

class DialogTwoButtons : BaseDialog() {

    private var onClickListener: OnClickListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        setScreen()

        return inflater.inflate(R.layout.fragment_dialog_two_buttons, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialogTwoButtonsCancelButton.setOnClickListener { onClickListener?.onCancel() }

        dialogTwoButtonsOKButton.setOnClickListener { onClickListener?.onOK() }
    }

    private fun setScreen() {
        hideTitle()
    }

    private fun setScreen() {
        hideTitle()
    }

    fun setOnClickListener(onOkClickListener: OnClickListener) {
        this.onClickListener = onOkClickListener
    }

    interface OnClickListener {
        fun onCancel()
        fun onOK()
    }
}
