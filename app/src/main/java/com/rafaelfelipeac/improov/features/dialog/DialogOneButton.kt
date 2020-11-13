package com.rafaelfelipeac.improov.features.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rafaelfelipeac.improov.core.extension.viewBinding
import com.rafaelfelipeac.improov.core.platform.base.BaseDialog
import com.rafaelfelipeac.improov.databinding.FragmentDialogOneButtonBinding

class DialogOneButton(private val message: String) : BaseDialog() {

    private var binding by viewBinding<FragmentDialogOneButtonBinding>()

    private var onClickListener: OnClickListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        setScreen()

        return FragmentDialogOneButtonBinding.inflate(inflater, container, false).run {
            binding = this
            binding.root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupLayout()
        setBehaviours()
    }

    private fun setupLayout() {
        binding.dialogOneButtonMessage.text = message
    }

    private fun setBehaviours() {
        binding.dialogOneButtonOKButton.setOnClickListener { onClickListener?.onOK() }
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
