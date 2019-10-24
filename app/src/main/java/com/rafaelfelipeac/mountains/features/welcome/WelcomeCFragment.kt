package com.rafaelfelipeac.mountains.features.welcome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rafaelfelipeac.mountains.R
import com.rafaelfelipeac.mountains.core.platform.base.BaseFragment

class WelcomeCFragment(val fragment: WelcomeFragment) : BaseFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        injector.inject(this)
        
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_welcome_c, container, false)
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if(isVisibleToUser)
            fragment.showStartButton()
    }
}
