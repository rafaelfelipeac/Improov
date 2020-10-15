package com.rafaelfelipeac.improov.features.main

import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.rafaelfelipeac.improov.R
import com.rafaelfelipeac.improov.core.platform.base.BaseActivity
import com.rafaelfelipeac.improov.core.platform.base.BaseFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    lateinit var toolbar: Toolbar
    lateinit var navController: NavController
    lateinit var fakeBottomNav: View
    lateinit var navLayout: CoordinatorLayout
    lateinit var fab: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTheme(R.style.AppTheme)
        setContentView(R.layout.activity_main)

        setupElements()
        setupToolbar()

        NavigationUI.setupWithNavController(bottomNav, navController)
    }

    override fun onBackPressed() {
        val baseFragment = (navHostFragment.childFragmentManager.fragments[0] as BaseFragment)

        when {
            baseFragment.bottomSheetTipIsOpen() -> {
                baseFragment.hideBottomSheetTips()

                setupToolbar()
            }
            lastFragment() -> {
                finish()
            }
            else -> {
                super.onBackPressed()
            }
        }

        clearToolbarMenu()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.menuSave -> false
            R.id.menuAdd -> false
            android.R.id.home -> false
            else -> false
        }
    }

    override fun onSupportNavigateUp() = findNavController(R.id.navHostFragment).navigateUp()

    private fun lastFragment(): Boolean {
        val currentFragment = NavHostFragment.findNavController(navHostFragment).currentDestination?.id

        return currentFragment == R.id.navigationList
    }

    private fun setupElements() {
        toolbar = findViewById(R.id.toolbar)
        navController = findNavController(R.id.navHostFragment)
        navLayout = navigationLayout
        fakeBottomNav = fakeBottomNavigation
        fab = fabLayout
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)

        val color = ContextCompat.getColor(this, R.color.colorPrimary)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            toolbar.navigationIcon?.colorFilter = BlendModeColorFilter(color, BlendMode.SRC_ATOP)
        } else {
            @Suppress("DEPRECATION")
            toolbar.navigationIcon?.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
        }
    }

    private fun clearToolbarMenu() = toolbar.menu.clear()
}
