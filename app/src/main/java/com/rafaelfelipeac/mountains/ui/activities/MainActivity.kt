package com.rafaelfelipeac.mountains.ui.activities

import android.graphics.PorterDuff
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.crashlytics.android.Crashlytics
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.rafaelfelipeac.mountains.R
import com.rafaelfelipeac.mountains.models.User
import com.rafaelfelipeac.mountains.ui.base.BaseActivity
import io.fabric.sdk.android.Fabric
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.bottom_sheet_tips_one.*
import kotlinx.android.synthetic.main.bottom_sheet_tips_two.*

class MainActivity : BaseActivity() {

    lateinit var toolbar: Toolbar
    lateinit var navController: NavController
    lateinit var fakeBottomNav: View
    lateinit var navLayout: CoordinatorLayout
    lateinit var fab: FloatingActionButton

    lateinit var bottomSheetTipClose: ConstraintLayout
    lateinit var bottomSheetTip: BottomSheetBehavior<*>

    lateinit var bottomSheetFAB: BottomSheetDialog
    lateinit var bottomSheetFABGoal: Button
    lateinit var bottomSheetFABRoutine: Button
    lateinit var bottomSheetFABClose: ImageView

    var mGoogleSignInClient: GoogleSignInClient? = null

    lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Fabric.with(this, Crashlytics())

        setTheme(R.style.AppTheme)
        setContentView(R.layout.activity_main)

        setupElements()
        setupToolbar()
        setupGoogleClient()

        setupBottomSheetFAB()

        NavigationUI.setupWithNavController(bottom_nav, navController)
    }

    private fun setupBottomSheetFAB() {
        bottomSheetFAB = BottomSheetDialog(this)
        val sheetView = layoutInflater.inflate(R.layout.bottom_sheet_fab, null)
        bottomSheetFAB.setContentView(sheetView)

        bottomSheetFABGoal = sheetView.findViewById(R.id.bottom_sheet_fab_goal)
        bottomSheetFABRoutine = sheetView.findViewById(R.id.bottom_sheet_fab_routine)
        bottomSheetFABClose = sheetView.findViewById(R.id.bottom_sheet_fab_close)
    }

    override fun onBackPressed() {
        if (lastFragment()) {
            finish()
        } else {
            super.onBackPressed()
        }

        clearToolbarMenu()
    }

    private fun lastFragment(): Boolean {
        val currentFragment = NavHostFragment.findNavController(nav_host_fragment).currentDestination!!.id

        return currentFragment == R.id.navigation_goals
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when {
            item?.itemId == R.id.menu_goal_save -> false
            item?.itemId == R.id.menu_goal_add -> false
            item?.itemId == android.R.id.home -> false
            else -> false
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

    fun openBottomSheetFAB() {
        bottomSheetFAB.show()

        bottomSheetFABGoal.setOnClickListener {
            navController.navigate(R.id.action_navigation_goals_to_navigation_goal_form)
        }

        bottomSheetFABRoutine.setOnClickListener {
            navController.navigate(R.id.action_navigation_goals_to_navigation_routine_form)
        }

        bottomSheetFABClose.setOnClickListener {
            closeBottomSheetFAB()
        }
    }

    fun closeBottomSheetFAB() {
        bottomSheetFAB.hide()
    }

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
