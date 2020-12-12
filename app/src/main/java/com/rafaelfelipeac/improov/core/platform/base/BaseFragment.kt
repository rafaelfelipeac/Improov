package com.rafaelfelipeac.improov.core.platform.base

import android.annotation.SuppressLint
import android.app.Activity
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.rafaelfelipeac.improov.R
import com.rafaelfelipeac.improov.core.di.provider.ViewModelProvider
import com.rafaelfelipeac.improov.core.extension.gone
import com.rafaelfelipeac.improov.core.extension.visible
import com.rafaelfelipeac.improov.core.extension.setMessageColor
import com.rafaelfelipeac.improov.core.extension.setIcon
import com.rafaelfelipeac.improov.core.extension.isEmpty
import com.rafaelfelipeac.improov.core.extension.resetValue
import com.rafaelfelipeac.improov.core.extension.formatToDateTime
import com.rafaelfelipeac.improov.core.extension.show
import com.rafaelfelipeac.improov.core.extension.hide
import com.rafaelfelipeac.improov.features.commons.domain.model.Goal
import com.rafaelfelipeac.improov.features.commons.domain.model.Item
import com.rafaelfelipeac.improov.features.main.MainActivity
import java.util.Date
import java.util.Calendar

const val DURATION_SNACKBAR = 10000

@Suppress("TooManyFunctions")
abstract class BaseFragment : Fragment() {

    protected val viewModelProvider: ViewModelProvider
        get() = (activity as BaseActivity).viewModelProvider

    val main: MainActivity get() = (activity as MainActivity)

    val fab: FloatingActionButton get() = main.fab
    val navController: NavController get() = main.navController
    private val appBarLayout: AppBarLayout get() = main.appBarLayout
    private val toolbar: Toolbar get() = main.toolbar
    private val navigation: CoordinatorLayout get() = main.navigation
    private val navigationFake: View get() = main.navigationFake

    private lateinit var bottomSheetGoal: BottomSheetDialog
    private lateinit var bottomSheetGoalYes: Button
    private lateinit var bottomSheetGoalNo: Button

    private lateinit var bottomSheetItem: BottomSheetDialog
    private lateinit var bottomSheetItemSave: Button
    private lateinit var bottomSheetItemName: TextInputEditText
    private lateinit var bottomSheetItemEmptyName: TextView
    private lateinit var bottomSheetItemTitle: TextView
    private lateinit var bottomSheetItemDate: TextView

    private var bottomSheetTip: BottomSheetBehavior<*>? = null
    private lateinit var bottomSheetTipClose: ConstraintLayout

    override fun onResume() {
        super.onResume()

        hideSoftKeyboard()
    }

    fun showToolbar() {
        appBarLayout.visible()
        toolbar.show(main.supportActionBar)
    }

    fun hideToolbar() {
        appBarLayout.gone()
        toolbar.hide(main.supportActionBar)
    }

    fun showNavigation() = navigation.show(navigationFake)

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

    fun showSnackBar(message: String) {
        Snackbar
            .make(requireView(), message, Snackbar.LENGTH_LONG)
            .setMessageColor(R.color.colorPrimaryDarkOne)
            .setIcon(message == getString(R.string.goal_message_goal_done))
            .show()
    }

    @SuppressLint("WrongConstant")
    fun showSnackBarWithAction(
        view: View,
        message: String,
        actionMessage: String,
        obj: Any,
        function: (obj: Any) -> Unit
    ) {
        Snackbar
            .make(view, message, Snackbar.LENGTH_INDEFINITE)
            .setMessageColor(R.color.colorPrimaryDarkOne)
            .setAction(actionMessage) { function(obj) }
            .setActionTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorPrimaryDarkOne
                )
            )
            .setDuration(DURATION_SNACKBAR)
            .show()
    }

    fun showDialogWithAction(title: String, function: () -> Unit) {
        AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setPositiveButton(getString(R.string.dialog_action_positive)) { _, _ -> function() }
            .setNegativeButton(getString(R.string.dialog_action_negative)) { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
    }

    fun showSoftKeyboard() {
        (activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as? InputMethodManager)?.run {
            toggleSoftInputFromWindow(
                view?.windowToken,
                InputMethodManager.SHOW_FORCED,
                0
            )
        }
    }

    fun hideSoftKeyboard() {
        (activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as? InputMethodManager?)?.run {
            hideSoftInputFromWindow(this@BaseFragment.view?.windowToken, 0)
        }
    }

    fun getCurrentTime(): Date = Calendar.getInstance().time

    fun setupBottomSheetGoal() {
        bottomSheetGoal = BottomSheetDialog(main)
        val sheetView = layoutInflater.inflate(R.layout.bottom_sheet_goal, null)
        bottomSheetGoal.setContentView(sheetView)

        bottomSheetGoalYes = sheetView.findViewById(R.id.goalDoneYes)
        bottomSheetGoalNo = sheetView.findViewById(R.id.goalDoneNo)
    }

    fun showBottomSheetGoal(
        goal: Goal,
        functionYes: (goal: Goal) -> Unit,
        functionToReload: () -> Unit
    ) {
        bottomSheetGoal.show()

        bottomSheetGoalYes.setOnClickListener {
            functionYes(goal)
            hideBottomSheetGoal()
        }

        bottomSheetGoalNo.setOnClickListener {
            functionToReload()
            hideBottomSheetGoal()
        }

        bottomSheetGoal.setOnDismissListener {
            functionToReload()
        }
    }

    private fun hideBottomSheetGoal() {
        bottomSheetGoal.hide()
    }

    fun setupBottomSheetItem(newItem: (name: String) -> Unit, updateItem: (name: String) -> Unit) {
        bottomSheetItem = BottomSheetDialog(this.requireActivity())
        val sheetView = layoutInflater.inflate(R.layout.bottom_sheet_item, null)
        bottomSheetItem.setContentView(sheetView)

        bottomSheetItemSave = sheetView.findViewById(R.id.bottomSheetItemSave)
        bottomSheetItemName = sheetView.findViewById(R.id.bottomSheetItemName)
        bottomSheetItemTitle = sheetView.findViewById(R.id.bottomSheetItemTitle)
        bottomSheetItemDate = sheetView.findViewById(R.id.bottomSheetItemDate)
        bottomSheetItemEmptyName = sheetView.findViewById(R.id.bottomSheetItemEmptyName)

        bottomSheetItemSave.setOnClickListener {
            if (bottomSheetItemName.isEmpty()) {
                bottomSheetItemEmptyName.text = getString(R.string.item_empty_name)
            } else {
                if (bottomSheetItemTitle.text == getString(R.string.item_title_add)) {
                    newItem(bottomSheetItemName.text.toString())
                } else {
                    updateItem(bottomSheetItemName.text.toString())
                }

                bottomSheetItemName.resetValue()
                hideBottomSheetItem()
            }
        }
    }

    fun showBottomSheetItem(item: Item?) {
        bottomSheetItem.show()

        bottomSheetItem.window?.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE or
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE
        )

        val bottomSheet =
            bottomSheetItem.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)

        bottomSheet?.let {
            BottomSheetBehavior.from(it).state = BottomSheetBehavior.STATE_EXPANDED
        }

        if (item != null) {
            bottomSheetItemTitle.text = getString(R.string.item_title_edit)
            bottomSheetItemName.setText(item.name)
            bottomSheetItemName.setSelection(bottomSheetItemName.text?.length ?: 0)

            if (item.done) {
                bottomSheetItemDate.text = String.format(
                    getString(R.string.item_date_format),
                    context?.let { item.doneDate?.formatToDateTime(it) }
                )
                bottomSheetItemDate.visible()
            } else {
                bottomSheetItemDate.gone()
            }
        } else {
            bottomSheetItemTitle.text = getString(R.string.item_title_add)
            bottomSheetItemName.resetValue()
            bottomSheetItemDate.resetValue()
            bottomSheetItemDate.gone()
        }
    }

    private fun hideBottomSheetItem() {
        bottomSheetItem.hide()
    }

    fun setupBottomSheetTip() {
        bottomSheetTipClose.setOnClickListener {
            hideSoftKeyboard()
            hideBottomSheetTips()
        }
    }

    fun setupBottomSheetTipsDivideAndConquer() {
        bottomSheetTip = BottomSheetBehavior.from(main.findViewById<LinearLayout>(R.id.tipsDivideAndConquer))
        bottomSheetTipClose = main.findViewById(R.id.tipsDivideAndConquerButtonClose)
    }

    fun setupBottomSheetTipsGoalType() {
        bottomSheetTip = BottomSheetBehavior.from(main.findViewById<LinearLayout>(R.id.tipsGoalType))
        bottomSheetTipClose = main.findViewById(R.id.tipsGoalTypeButtonClose)
    }

    fun setupBottomSheetTipsSwipe() {
        bottomSheetTip = BottomSheetBehavior.from(main.findViewById<LinearLayout>(R.id.tipsSwipe))
        bottomSheetTipClose = main.findViewById(R.id.tipsSwipeButtonClose)
    }

    fun setupBottomSheetTipsBackup() {
        bottomSheetTip = BottomSheetBehavior.from(main.findViewById<LinearLayout>(R.id.tipsBackup))
        bottomSheetTipClose = main.findViewById(R.id.tipsBackupButtonClose)
    }

    fun showBottomSheetTips() {
        bottomSheetTip?.state = BottomSheetBehavior.STATE_EXPANDED
    }

    fun hideBottomSheetTips() {
        bottomSheetTip?.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    fun bottomSheetTipIsOpen(): Boolean {
        return bottomSheetTip?.state == BottomSheetBehavior.STATE_EXPANDED
    }
}
