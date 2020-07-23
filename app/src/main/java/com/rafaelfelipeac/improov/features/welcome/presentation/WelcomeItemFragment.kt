package com.rafaelfelipeac.improov.features.welcome.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rafaelfelipeac.improov.R
import com.rafaelfelipeac.improov.core.platform.base.BaseFragment

class WelcomeItemFragment(
    private val fragment: WelcomeFragment,
    private val welcomePosition: WelcomePosition
) : BaseFragment() {

    constructor() : this(WelcomeFragment(), WelcomePosition.FIRST)

    override fun onCreate(savedInstanceState: Bundle?) {
        injector.inject(this)

        super.onCreate(savedInstanceState)
    }

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
            WelcomePosition.FIRST -> inflater.inflate(R.layout.fragment_welcome_item_a, container, false)
            WelcomePosition.SECOND -> inflater.inflate(R.layout.fragment_welcome_item_b, container, false)
            WelcomePosition.THIRD -> inflater.inflate(R.layout.fragment_welcome_item_c, container, false)
            else -> inflater.inflate(R.layout.fragment_welcome_item_a, container, false)
        }
    }
}
