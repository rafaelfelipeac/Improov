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
import androidx.navigation.ui.NavigationUI
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.rafaelfelipeac.improov.R
import com.rafaelfelipeac.improov.core.extension.viewBinding
import com.rafaelfelipeac.improov.core.platform.base.BaseActivity
import com.rafaelfelipeac.improov.core.platform.base.BaseFragment
import com.rafaelfelipeac.improov.databinding.ActivityMainBinding
import com.rafaelfelipeac.improov.features.goal.presentation.goaldetail.GoalDetailFragmentDirections

class MainActivity : BaseActivity() {

    lateinit var toolbar: Toolbar
    lateinit var appBarLayout: AppBarLayout
    lateinit var navController: NavController
    lateinit var navigation: CoordinatorLayout
    lateinit var navigationFake: View
    lateinit var fab: FloatingActionButton

    private val binding by viewBinding(ActivityMainBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTheme(R.style.AppTheme)
        setContentView(binding.root)

        setupElements()
        setupToolbar()

        NavigationUI.setupWithNavController(binding.bottomNav, navController)
    }

    override fun onBackPressed() {
        val baseFragment: BaseFragment? = try {
            supportFragmentManager.fragments[0] as BaseFragment
        } catch (e: ClassCastException) {
            e.printStackTrace()
            null
        }

        when {
            baseFragment != null && baseFragment.bottomSheetTipIsOpen() -> {
                baseFragment.hideBottomSheetTips()

                setupToolbar()
            }
            lastFragment() -> {
                finish()
            }
            isDetailFragment() -> {
                navController.navigate(GoalDetailFragmentDirections.goalToList())
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
        val currentFragment = navController.currentDestination?.id

        return currentFragment == R.id.navList || currentFragment == R.id.navWelcome
    }

    private fun isDetailFragment(): Boolean {
        val currentFragment = navController.currentDestination?.id

        return currentFragment == R.id.navGoal
    }

    private fun setupElements() {
        navController = findNavController(R.id.navHostFragment)
        navigation = binding.navigation
        navigationFake = binding.navigationFake
        fab = binding.fab
        toolbar = binding.toolbar
        appBarLayout = binding.appBarLayout
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
