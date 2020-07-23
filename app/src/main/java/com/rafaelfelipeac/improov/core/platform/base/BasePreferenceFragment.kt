package com.rafaelfelipeac.improov.core.platform.base

import androidx.preference.PreferenceFragmentCompat
import com.rafaelfelipeac.improov.core.extension.gone
import com.rafaelfelipeac.improov.features.main.MainActivity

abstract class BasePreferenceFragment : PreferenceFragmentCompat() {

    val main get() = (activity as MainActivity)

    val navController get() = main.navController
    private val navLayout get() = main.navLayout
    private val fakeBottomNav get() = main.fakeBottomNav

    fun hideNavigation() {
        navLayout.gone()
        fakeBottomNav.gone()
    }

    fun showBackArrow() {
        main.supportActionBar?.show()
        main.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    fun setTitle(title: String) {
        main.supportActionBar?.show()
        main.supportActionBar?.title = title
    }

    fun hasMenu() {
        main.supportActionBar?.show()
        setHasOptionsMenu(true)
    }
}
