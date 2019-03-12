package com.rafaelfelipeac.domore.ui.activities

import android.graphics.PorterDuff
import android.os.Bundle
import androidx.core.content.ContextCompat
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.textfield.TextInputEditText
import com.rafaelfelipeac.domore.R
import com.rafaelfelipeac.domore.models.Goal
import com.rafaelfelipeac.domore.models.Item
import com.rafaelfelipeac.domore.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.bottom_sheet_goal_done.*
import kotlinx.android.synthetic.main.bottom_sheet_item_fragment.*
import java.text.SimpleDateFormat

class MainActivity : BaseActivity() {

    lateinit var navController: NavController

    var toolbar: Toolbar? = null
    var bottomSheetAddItem: BottomSheetBehavior<*>? = null
    var bottomSheetDoneGoal: BottomSheetBehavior<*>? = null
    var bottomNavigation: BottomNavigationView? = null
    var itemClose: ImageView? = null
    var itemSave: Button? = null
    var itemTitle: TextView? = null
    var itemValue: TextInputEditText? = null
    var bottomSheetDoneGoalYes: Button? = null
    var bottomSheetDoneGoalNo: Button? = null
    var item: Item? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initElements()
        setToolbar()

        NavigationUI.setupWithNavController(bottom_nav, navController)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when {
            item?.itemId == R.id.menu_goal_save -> false
            item?.itemId == R.id.menu_goal_add  -> false
            item?.itemId == android.R.id.home   -> false
            else                                -> false
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()

        clearToolbarMenu()
    }

    override fun onSupportNavigateUp() = findNavController(R.id.nav_host_fragment).navigateUp()

    private fun initElements() {
        toolbar = findViewById(R.id.toolbar)!!
        navController = findNavController(R.id.nav_host_fragment)
        bottomNavigation = bottom_nav
        bottomSheetAddItem = BottomSheetBehavior.from(findViewById<LinearLayout>(R.id.bottom_sheet_add_item))
        bottomSheetDoneGoal =  BottomSheetBehavior.from(findViewById<LinearLayout>(R.id.bottom_sheet_done_goal))
        itemClose = bottom_sheet_item_close
        itemSave = bottom_sheet_button_save
        itemTitle = bottom_sheet_title
        itemValue = bottom_sheet_item_name
        bottomSheetDoneGoalYes = bottom_sheet_button_yes
        bottomSheetDoneGoalNo = bottom_sheet_button_no
    }

    fun bottomNavigationVisible(visible: Boolean) {
        bottomNavigation?.visibility = if (visible) View.VISIBLE else View.GONE
    }

    fun openBottomSheetAddItem(item: Item?) {
        bottomSheetAddItem?.state = BottomSheetBehavior.STATE_EXPANDED

        this.item = item
        if (item == null) {
            itemTitle?.text = "Novo item"
        } else {
            itemTitle?.text = "Editar item"
            itemValue?.setText(item.title)
        }

        if (item != null) {
            if (item.done) {
                val sdf = SimpleDateFormat("dd/MM/yy - HH:mm:ss")
                val currentDate = sdf.format(item.doneDate)

                bottom_sheet_date.visibility = View.VISIBLE
                bottom_sheet_date.text = String.format("Concluído em %s", currentDate)
            } else {
                bottom_sheet_date.visibility = View.GONE
            }
        }
    }

    fun closeBottomSheetAddItem() { bottomSheetAddItem?.state = BottomSheetBehavior.STATE_COLLAPSED }

    fun openBottomSheetDoneGoal(goal: Goal, function: (goal: Goal, done: Boolean) -> Unit) {
        bottomSheetDoneGoal?.state = BottomSheetBehavior.STATE_EXPANDED

        bottomSheetDoneGoalYes?.setOnClickListener {
            function(goal, true)
            closeBottomSheetDoneGoal()
        }

        bottomSheetDoneGoalNo?.setOnClickListener {
            closeBottomSheetDoneGoal()
        }
    }

    fun closeBottomSheetDoneGoal() { bottomSheetDoneGoal?.state = BottomSheetBehavior.STATE_COLLAPSED }

    private fun setToolbar() {
        setSupportActionBar(toolbar)

        toolbar?.navigationIcon?.setColorFilter(
            ContextCompat.getColor(
                this, R.color.colorPrimary
            ), PorterDuff.Mode.SRC_ATOP
        )
    }

    private fun clearToolbarMenu() = toolbar?.menu!!.clear()
}
