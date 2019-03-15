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
    var bottomNavigation: BottomNavigationView? = null
    var item: Item? = null

    lateinit var bottomSheetItem: BottomSheetBehavior<*>
    lateinit var bottomSheetItemClose: ImageView
    lateinit var bottomSheetItemSave: Button
    lateinit var bottomSheetItemName: TextInputEditText

    lateinit var bottomSheetDoneGoal: BottomSheetBehavior<*>
    lateinit var bottomSheetDoneGoalYes: Button
    lateinit var bottomSheetDoneGoalNo: Button



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

        bottomSheetItem = BottomSheetBehavior.from(findViewById<LinearLayout>(R.id.bottom_sheet_add_item))
        bottomSheetItemClose = bottom_sheet_item_button_close
        bottomSheetItemSave = bottom_sheet_item_button_save
        bottomSheetItemName = bottom_sheet_item_name

        bottomSheetDoneGoal =  BottomSheetBehavior.from(findViewById<LinearLayout>(R.id.bottom_sheet_done_goal))
        bottomSheetDoneGoalYes = bottom_sheet_button_yes
        bottomSheetDoneGoalNo = bottom_sheet_button_no
    }

    fun bottomNavigationVisible(visible: Boolean) {
        bottomNavigation?.visibility = if (visible) View.VISIBLE else View.GONE
    }

    fun openBottomSheetAddItem(item: Item?) {
        bottomSheetItem.state = BottomSheetBehavior.STATE_EXPANDED

        this.item = item

        if (item != null) {
            bottom_sheet_item_title?.text = getString(R.string.bottom_sheet_item_title_edit)
            bottom_sheet_item_name?.setText(item.name)

            if (item.done) {
                bottom_sheet_item_date.visibility = View.VISIBLE

                val sdf = SimpleDateFormat("dd/MM/yy - HH:mm:ss")
                val currentDate = sdf.format(item.doneDate)

                bottom_sheet_item_date.text = String.format("Concluído em %s", currentDate)
            } else {
                bottom_sheet_item_date.visibility = View.GONE
            }
        } else {
            bottom_sheet_item_title?.text = getString(R.string.bottom_sheet_item_title_add)
            bottom_sheet_item_name?.setText("")
            bottom_sheet_item_date.text = ""
            bottom_sheet_item_date.visibility = View.GONE
        }
    }

    fun closeBottomSheetAddItem() { bottomSheetItem.state = BottomSheetBehavior.STATE_COLLAPSED }

    fun openBottomSheetDoneGoal(goal: Goal, function: (goal: Goal, done: Boolean) -> Unit) {
        bottomSheetDoneGoal.state = BottomSheetBehavior.STATE_EXPANDED

        bottomSheetDoneGoalYes.setOnClickListener {
            function(goal, true)
            closeBottomSheetDoneGoal()
        }

        bottomSheetDoneGoalNo.setOnClickListener {
            closeBottomSheetDoneGoal()
        }
    }

    fun closeBottomSheetDoneGoal() { bottomSheetDoneGoal.state = BottomSheetBehavior.STATE_COLLAPSED }

    private fun setToolbar() {
        setSupportActionBar(toolbar)

        toolbar?.navigationIcon?.setColorFilter(
            ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP
        )
    }

    private fun clearToolbarMenu() = toolbar?.menu!!.clear()
}
