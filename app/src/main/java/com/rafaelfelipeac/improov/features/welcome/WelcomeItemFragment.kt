package com.rafaelfelipeac.improov.features.welcome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rafaelfelipeac.improov.R
import com.rafaelfelipeac.improov.core.platform.base.BaseFragment

class WelcomeItemFragment(
    private val fragment: WelcomeFragment,
    private val pos: Int
) : BaseFragment() {

    constructor(): this(WelcomeFragment(), 0)

    override fun onCreate(savedInstanceState: Bundle?) {
        injector.inject(this)

        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return when (pos) {
            0 -> inflater.inflate(R.layout.fragment_welcome_item_a, container, false)
            1 -> inflater.inflate(R.layout.fragment_welcome_item_b, container, false)
            2 -> inflater.inflate(R.layout.fragment_welcome_item_c, container, false)
            else -> inflater.inflate(R.layout.fragment_welcome_item_a, container, false)
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if(isVisibleToUser && pos == 2) {
            fragment.showStartButton()
        } else {
            fragment.hideStartButton()
        }
    }
}
