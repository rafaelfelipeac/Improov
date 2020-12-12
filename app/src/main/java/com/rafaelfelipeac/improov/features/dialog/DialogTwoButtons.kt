package com.rafaelfelipeac.improov.features.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rafaelfelipeac.improov.core.extension.viewBinding
import com.rafaelfelipeac.improov.core.platform.base.BaseDialog
import com.rafaelfelipeac.improov.databinding.FragmentDialogTwoButtonsBinding

class DialogTwoButtons(
    private val message: String,
    private val negativeText: String = "",
    private val positiveText: String = ""
) : BaseDialog() {

    private var binding by viewBinding<FragmentDialogTwoButtonsBinding>()

    private var onClickListener: OnClickListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        setScreen()

        return FragmentDialogTwoButtonsBinding.inflate(inflater, container, false).run {
            binding = this
            binding.root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupLayout()
        setupBehaviours()
    }

    private fun setupLayout() {
        binding.dialogTwoButtonsMessage.text = message

        if (negativeText.isNotEmpty()) {
            binding.dialogTwoButtonsNegativeButton.text = negativeText
        }

        if (positiveText.isNotEmpty()) {
            binding.dialogTwoButtonsPositiveButton.text = positiveText
        }
    }

    private fun setupBehaviours() {
        binding.dialogTwoButtonsNegativeButton.setOnClickListener { onClickListener?.onNegative() }

        binding.dialogTwoButtonsPositiveButton.setOnClickListener { onClickListener?.onPositive() }
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
