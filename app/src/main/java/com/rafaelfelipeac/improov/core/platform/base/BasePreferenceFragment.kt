package com.rafaelfelipeac.improov.core.platform.base

import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.navigation.NavController
import androidx.preference.PreferenceFragmentCompat
import com.rafaelfelipeac.improov.core.extension.gone
import com.rafaelfelipeac.improov.features.main.MainActivity

abstract class BasePreferenceFragment : PreferenceFragmentCompat() {

    val main: MainActivity get() = (activity as MainActivity)

    val navController: NavController get() = main.navController
    private val navLayout: CoordinatorLayout get() = main.navLayout
    private val fakeBottomNav: View get() = main.fakeBottomNav

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
