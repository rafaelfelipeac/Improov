package com.rafaelfelipeac.readmore.ui.activities

import android.graphics.PorterDuff
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.Menu
import android.view.MenuItem
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.rafaelfelipeac.readmore.R
import com.rafaelfelipeac.readmore.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        navController = findNavController(R.id.nav_host_fragment)
        NavigationUI.setupWithNavController(bottom_nav, navController)

        supportActionBar?.elevation = 0F

        toolbar?.navigationIcon?.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary),
            PorterDuff.Mode.SRC_ATOP)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            R.id.menu_goal_save -> {
                //onBackPressed()
                return false
            }
            android.R.id.home -> {
                //onBackPressed()
                return false
            }
        }

        return false
    }

    override fun onSupportNavigateUp() = findNavController(R.id.nav_host_fragment).navigateUp()

    override fun onBackPressed() {
        super.onBackPressed()
        clearToolbarMenu()
    }

    private fun clearToolbarMenu() {
        toolbar.menu.clear()
    }
}
