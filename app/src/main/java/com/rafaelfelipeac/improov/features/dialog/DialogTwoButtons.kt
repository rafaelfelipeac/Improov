package com.rafaelfelipeac.improov.features.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rafaelfelipeac.improov.R
import com.rafaelfelipeac.improov.core.platform.base.BaseDialog
import kotlinx.android.synthetic.main.fragment_dialog_two_buttons.*

class DialogTwoButtons(
    private val message: String,
    private val negativeText: String = "",
    private val positiveText: String = ""
) : BaseDialog() {

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

        setupLayout()
        setupBehaviours()
    }

    private fun setupLayout() {
        dialogTwoButtonsMessage.text = message

        if (negativeText.isNotEmpty()) {
            dialogTwoButtonsNegativeButton.text = negativeText
        }

        if (positiveText.isNotEmpty()) {
            dialogTwoButtonsPositiveButton.text = positiveText
        }
    }

    private fun setupBehaviours() {
        dialogTwoButtonsNegativeButton.setOnClickListener { onClickListener?.onNegative() }

        dialogTwoButtonsPositiveButton.setOnClickListener { onClickListener?.onPositive() }
    }

    private fun setScreen() {
        hideTitle()
    }

    fun setOnClickListener(onOkClickListener: OnClickListener) {
        this.onClickListener = onOkClickListener
    }

    interface OnClickListener {
        fun onNegative()
        fun onPositive()
    }
}