package com.rafaelfelipeac.improov.core.platform.base

import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.navigation.NavController
import androidx.preference.PreferenceFragmentCompat
import com.rafaelfelipeac.improov.core.extension.gone
import com.rafaelfelipeac.improov.core.extension.show
import com.rafaelfelipeac.improov.features.main.MainActivity

abstract class BasePreferenceFragment : PreferenceFragmentCompat() {

    private val main: MainActivity get() = (activity as MainActivity)

    val navController: NavController get() = main.navController
    private val toolbar: Toolbar get() = main.toolbar
    private val navLayout: CoordinatorLayout get() = main.navLayout
    private val fakeBottomNav: View get() = main.fakeBottomNav

    fun hideNavigation() {
        navLayout.gone()
        fakeBottomNav.gone()
    }

    fun showBackArrow() {
        toolbar.show(main.supportActionBar)
        main.supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    fun setTitle(title: String) {
        toolbar.show(main.supportActionBar)
        main.supportActionBar?.title = title
    }

    fun hasMenu() {
        toolbar.show(main.supportActionBar)
        setHasOptionsMenu(true)
    }
}
