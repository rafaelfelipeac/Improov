package com.rafaelfelipeac.improov.features.welcome.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rafaelfelipeac.improov.core.extension.viewBinding
import com.rafaelfelipeac.improov.core.platform.base.BaseFragment
import com.rafaelfelipeac.improov.databinding.FragmentWelcomeItemABinding
import com.rafaelfelipeac.improov.databinding.FragmentWelcomeItemBBinding
import com.rafaelfelipeac.improov.databinding.FragmentWelcomeItemCBinding

class WelcomeItemFragment(
    private val fragment: WelcomeFragment,
    private val welcomePosition: WelcomePosition
) : BaseFragment() {

    constructor() : this(WelcomeFragment(), WelcomePosition.FIRST)

    private var bindingA by viewBinding<FragmentWelcomeItemABinding>()
    private var bindingB by viewBinding<FragmentWelcomeItemBBinding>()
    private var bindingC by viewBinding<FragmentWelcomeItemCBinding>()

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
            WelcomePosition.FIRST -> FragmentWelcomeItemABinding.inflate(
                inflater,
                container,
                false
            ).run {
                bindingA = this
                bindingA.root
            }
            WelcomePosition.SECOND -> FragmentWelcomeItemBBinding.inflate(
                inflater,
                container,
                false
            ).run {
                bindingB = this
                bindingB.root
            }
            WelcomePosition.THIRD -> FragmentWelcomeItemCBinding.inflate(
                inflater,
                container,
                false
            ).run {
                bindingC = this
                bindingC.root
            }
            else -> FragmentWelcomeItemABinding.inflate(
                inflater,
                container,
                false
            ).run {
                bindingA = this
                bindingA.root
            }
        }
    }
}
