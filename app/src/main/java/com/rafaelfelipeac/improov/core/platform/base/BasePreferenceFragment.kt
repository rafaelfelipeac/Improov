package com.rafaelfelipeac.improov.core.platform.base

import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.navigation.NavController
import androidx.preference.PreferenceFragmentCompat
import com.rafaelfelipeac.improov.core.extension.hide
import com.rafaelfelipeac.improov.core.extension.show
import com.rafaelfelipeac.improov.features.main.MainActivity

abstract class BasePreferenceFragment : PreferenceFragmentCompat() {

    private val main: MainActivity get() = (activity as MainActivity)

    val navController: NavController get() = main.navController
    private val toolbar: Toolbar get() = main.toolbar
    private val navigation: CoordinatorLayout get() = main.navigation
    private val navigationFake: View get() = main.navigationFake

    fun showToolbar() = toolbar.show(main.supportActionBar)

    fun hideNavigation() = navigation.hide(navigationFake)

    fun showBackArrow() {
        showToolbar()
        main.supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    fun setTitle(title: String) {
        showToolbar()
        main.supportActionBar?.title = title
    }

    fun hasMenu() {
        showToolbar()
        setHasOptionsMenu(true)
    }
}
