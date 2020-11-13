package com.rafaelfelipeac.improov.features.welcome.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rafaelfelipeac.improov.core.extension.viewBinding
import com.rafaelfelipeac.improov.core.platform.base.BaseFragment
import com.rafaelfelipeac.improov.databinding.FragmentWelcomeFirstBinding
import com.rafaelfelipeac.improov.databinding.FragmentWelcomeSecondBinding
import com.rafaelfelipeac.improov.databinding.FragmentWelcomeThirdBinding

class WelcomeItemFragment(
    private val fragment: WelcomeFragment,
    private val welcomePosition: WelcomePosition
) : BaseFragment() {

    constructor() : this(WelcomeFragment(), WelcomePosition.FIRST)

    private var bindingFirst by viewBinding<FragmentWelcomeFirstBinding>()
    private var bindingSecond by viewBinding<FragmentWelcomeSecondBinding>()
    private var bindingThird by viewBinding<FragmentWelcomeThirdBinding>()

    override fun onResume() {
        super.onResume()

        if (welcomePosition == WelcomePosition.THIRD) {
            fragment.showStartButton()
        } else {
            fragment.hideStartButton()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return when (welcomePosition) {
            WelcomePosition.FIRST -> FragmentWelcomeFirstBinding.inflate(
                inflater,
                container,
                false
            ).run {
                bindingFirst = this
                bindingFirst.root
            }
            WelcomePosition.SECOND -> FragmentWelcomeSecondBinding.inflate(
                inflater,
                container,
                false
            ).run {
                bindingSecond = this
                bindingSecond.root
            }
            WelcomePosition.THIRD -> FragmentWelcomeThirdBinding.inflate(
                inflater,
                container,
                false
            ).run {
                bindingThird = this
                bindingThird.root
            }
            else -> FragmentWelcomeFirstBinding.inflate(
                inflater,
                container,
                false
            ).run {
                bindingFirst = this
                bindingFirst.root
            }
        }
    }
}
