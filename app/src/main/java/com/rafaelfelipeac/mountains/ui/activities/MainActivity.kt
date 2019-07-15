package com.rafaelfelipeac.mountains.ui.activities

import android.graphics.PorterDuff
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.crashlytics.android.Crashlytics
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.textfield.TextInputEditText
import com.rafaelfelipeac.mountains.R
import com.rafaelfelipeac.mountains.extension.convertDateToString
import com.rafaelfelipeac.mountains.extension.gone
import com.rafaelfelipeac.mountains.extension.resetValue
import com.rafaelfelipeac.mountains.extension.visible
import com.rafaelfelipeac.mountains.models.Goal
import com.rafaelfelipeac.mountains.models.Item
import com.rafaelfelipeac.mountains.models.User
import com.rafaelfelipeac.mountains.ui.base.BaseActivity
import io.fabric.sdk.android.Fabric
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.bottom_sheet_goal_done.*
import kotlinx.android.synthetic.main.bottom_sheet_item_fragment.*
import androidx.navigation.fragment.NavHostFragment
import kotlinx.android.synthetic.main.bottom_sheet_item_fragment.bottom_sheet_item_button_save
import kotlinx.android.synthetic.main.bottom_sheet_item_fragment.bottom_sheet_item_date
import kotlinx.android.synthetic.main.bottom_sheet_item_fragment.bottom_sheet_item_name
import kotlinx.android.synthetic.main.bottom_sheet_item_fragment.bottom_sheet_item_title
import kotlinx.android.synthetic.main.bottom_sheet_tips_one.*
import kotlinx.android.synthetic.main.bottom_sheet_tips_two.*
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : BaseActivity() {

    lateinit var toolbar: Toolbar
    lateinit var navController: NavController
    lateinit var fakeBottomNav: View
    lateinit var navLayout: CoordinatorLayout
    lateinit var fab: FloatingActionButton

    lateinit var bottomSheetItemSave: Button
    lateinit var bottomSheetItemClose: ImageView
    lateinit var bottomSheetItemName: TextInputEditText
    lateinit var bottomSheetItem: BottomSheetBehavior<*>

    lateinit var bottomSheetDoneGoalNo: Button
    lateinit var bottomSheetDoneGoalYes: Button
    lateinit var bottomSheetDoneGoal: BottomSheetBehavior<*>

    lateinit var bottomSheetTipClose: ConstraintLayout
    lateinit var bottomSheetTip: BottomSheetBehavior<*>

    var mGoogleSignInClient: GoogleSignInClient? = null

    var item: Item? = null

    lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Fabric.with(this, Crashlytics())

        setTheme(R.style.AppTheme)
        setContentView(R.layout.activity_main)

        setupElements()
        setupToolbar()
        setupGoogleClient()

        NavigationUI.setupWithNavController(bottom_nav, navController)
    }

    override fun onBackPressed() {
        if (lastFragment()) { finish() }
        else { super.onBackPressed() }

        clearToolbarMenu()
    }

    private fun lastFragment(): Boolean {
        val currentFragment = NavHostFragment.findNavController(nav_host_fragment).currentDestination!!.id

        return currentFragment == R.id.navigation_goals
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when {
            item?.itemId == R.id.menu_goal_save -> false
            item?.itemId == R.id.menu_goal_add  -> false
            item?.itemId == android.R.id.home   -> false
            else                                -> false
        }
    }

    override fun onSupportNavigateUp() = findNavController(R.id.nav_host_fragment).navigateUp()

    private fun setupGoogleClient() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    private fun setupElements() {
        toolbar = findViewById(R.id.toolbar)!!
        navController = findNavController(R.id.nav_host_fragment)
        navLayout = nav_layout
        fakeBottomNav = fake_bottom_nav
        fab = fab_layout

        bottom_nav.alpha = 0.9F

        setupBottomSheetItem()
        setupBottomSheetDoneGoal()
    }

    private fun setupBottomSheetItem() {
        bottomSheetItem = BottomSheetBehavior.from(findViewById<LinearLayout>(R.id.bottom_sheet_add_item))
        bottomSheetItemClose = bottom_sheet_item_button_close
        bottomSheetItemSave = bottom_sheet_item_button_save
        bottomSheetItemName = bottom_sheet_item_name
    }

    private fun setupBottomSheetDoneGoal() {
        bottomSheetDoneGoal = BottomSheetBehavior.from(findViewById<LinearLayout>(R.id.bottom_sheet_done_goal))
        bottomSheetDoneGoalYes = bottom_sheet_button_yes
        bottomSheetDoneGoalNo = bottom_sheet_button_no
    }

    fun setupBottomSheetTipsOne() {
        bottomSheetTip = BottomSheetBehavior.from(findViewById<LinearLayout>(R.id.bottom_sheet_tips_one))
        bottomSheetTipClose = bottom_sheet_tips_one_button_close
    }

    fun setupBottomSheetTipsTwo() {
        bottomSheetTip = BottomSheetBehavior.from(findViewById<LinearLayout>(R.id.bottom_sheet_tips_two))
        bottomSheetTipClose = bottom_sheet_tips_two_button_close
    }

    fun bottomNavigationVisible(visibility: Int) {
        if (bottom_nav != null) {
            navLayout = nav_layout
            navLayout.visibility = visibility

            fakeBottomNav = fake_bottom_nav
            fakeBottomNav.visibility = visibility
        }
    }

    fun openBottomSheetItem(item: Item?) {
        bottomSheetItem.state = BottomSheetBehavior.STATE_EXPANDED

        this.item = item

        if (item != null) {
            bottom_sheet_item_title?.text = getString(R.string.bottom_sheet_item_title_edit)
            bottom_sheet_item_name?.setText(item.name)

            if (item.done) {
                bottom_sheet_item_date.text = String.format(getString(R.string.bottom_sheet_item_date_format), item.doneDate?.convertDateToString())
                bottom_sheet_item_date.visible()
            } else {
                bottom_sheet_item_date.gone()
            }
        } else {
            bottom_sheet_item_title?.text = getString(R.string.bottom_sheet_item_title_add)
            bottom_sheet_item_name?.resetValue()
            bottom_sheet_item_date?.resetValue()
            bottom_sheet_item_date.gone()
        }
    }

    fun closeBottomSheetItem() { bottomSheetItem.state = BottomSheetBehavior.STATE_COLLAPSED }

    fun openBottomSheetDoneGoal(goal: Goal, function: (goal: Goal) -> Unit) {
        bottomSheetDoneGoal.state = BottomSheetBehavior.STATE_EXPANDED

        bottomSheetDoneGoalYes.setOnClickListener {
            function(goal)
            closeBottomSheetDoneGoal()
        }

        bottomSheetDoneGoalNo.setOnClickListener {
            closeBottomSheetDoneGoal()
        }
    }

    fun closeBottomSheetDoneGoal() { bottomSheetDoneGoal.state = BottomSheetBehavior.STATE_COLLAPSED }

    fun openBottomSheetTips() {
        bottomSheetTip.state = BottomSheetBehavior.STATE_EXPANDED
    }

    fun closeBottomSheetTips() { bottomSheetTip.state = BottomSheetBehavior.STATE_COLLAPSED }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)

        toolbar.navigationIcon?.setColorFilter(
            ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP
        )
    }

    private fun clearToolbarMenu() = toolbar.menu!!.clear()

    fun closeToolbar() = supportActionBar?.hide()

    fun openToolbar() = supportActionBar?.show()
}
