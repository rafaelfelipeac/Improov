package com.rafaelfelipeac.domore.ui.activities

import android.graphics.PorterDuff
import android.os.Bundle
import androidx.core.content.ContextCompat
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.rafaelfelipeac.domore.R
import com.rafaelfelipeac.domore.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.bottom_sheet_item_fragment.*

class MainActivity : BaseActivity() {

    lateinit var navController: NavController

    var toolbar: Toolbar? = null
    var sheetBehavior: BottomSheetBehavior<*>? = null
    var bottomNavigation: BottomNavigationView? = null
    var itemClose: ImageView? = null
    var itemSave: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initElements()

        setSupportActionBar(toolbar)

        NavigationUI.setupWithNavController(bottom_nav, navController)

//        supportActionBar?.elevation = 0F

        toolbar?.navigationIcon?.setColorFilter(ContextCompat.getColor(
            this, R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when {
            item?.itemId == R.id.menu_goal_save -> false
            item?.itemId == R.id.menu_goal_add -> false
            item?.itemId == android.R.id.home -> false
            else -> false
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()

        clearToolbarMenu()
    }

    override fun onSupportNavigateUp() = findNavController(R.id.nav_host_fragment).navigateUp()

    fun bottomNavigationVisible(visible: Boolean) {
        bottomNavigation?.visibility = if (visible) View.VISIBLE else View.GONE
    }

    private fun initElements() {
        toolbar = findViewById(R.id.toolbar)!!
        navController = findNavController(R.id.nav_host_fragment)
        bottomNavigation = bottom_nav
        itemClose = bottom_sheet_item_close
        itemSave = bottom_sheet_button_save
        sheetBehavior = BottomSheetBehavior.from(findViewById<LinearLayout>(R.id.bottom_sheet_id))
    }

    fun openBottomSheet() { sheetBehavior?.state = BottomSheetBehavior.STATE_EXPANDED }

    fun closeBottomSheet() { sheetBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED }

    private fun clearToolbarMenu() = toolbar?.menu!!.clear()
}
