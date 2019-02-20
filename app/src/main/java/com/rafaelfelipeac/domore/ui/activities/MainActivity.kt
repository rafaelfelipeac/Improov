package com.rafaelfelipeac.domore.ui.activities

import android.graphics.PorterDuff
import android.os.Bundle
import androidx.core.content.ContextCompat
import android.view.MenuItem
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

    var sheetBehavior: BottomSheetBehavior<*>? = null

    var botao_close: ImageView? = null

    var toolbar: Toolbar? = null

    var bottom_navigation: BottomNavigationView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar = findViewById(R.id.toolbar)!!

        setSupportActionBar(toolbar)

        navController = findNavController(R.id.nav_host_fragment)
        NavigationUI.setupWithNavController(bottom_nav, navController)

        bottom_navigation = bottom_nav
        botao_close = botao_fechar

        supportActionBar?.elevation = 0F

        sheetBehavior = BottomSheetBehavior.from(findViewById<LinearLayout>(R.id.bottom_sheet_id))

        toolbar!!.navigationIcon?.setColorFilter(ContextCompat.getColor(
            this, R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            R.id.menu_goal_save -> { return false }
            R.id.menu_goal_add -> {return false }
            android.R.id.home -> { return false }
        }

        return false
    }

    override fun onSupportNavigateUp() = findNavController(R.id.nav_host_fragment).navigateUp()

//    fun showBottomSheet() {
//        sheetBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED
//    }

//    fun hideBottomSheet() {
//        sheetBehavior?.state = BottomSheetBehavior.STATE_HIDDEN
//    }
//
//    fun setupBottomSheet() {
//        val height = 200
//        sheetBehavior?.peekHeight = height
//
//        showBottomSheet()
//    }

    fun openBottomSheet() {
        sheetBehavior?.state = BottomSheetBehavior.STATE_EXPANDED
    }

    fun closeBottomSheet() {
        sheetBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    fun openOrCloseBottomSheet(): Boolean {
        return sheetBehavior?.state != BottomSheetBehavior.STATE_EXPANDED
    }

    override fun onBackPressed() {
        super.onBackPressed()
        clearToolbarMenu()
    }

    private fun clearToolbarMenu() = toolbar?.menu!!.clear()
}
