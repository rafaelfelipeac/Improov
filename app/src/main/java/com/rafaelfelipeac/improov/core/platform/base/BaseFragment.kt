package com.rafaelfelipeac.improov.core.platform.base

import android.app.Activity
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.rafaelfelipeac.improov.R
import com.rafaelfelipeac.improov.core.di.modules.viewModel.ViewModelFactory
import com.rafaelfelipeac.improov.core.extension.gone
import com.rafaelfelipeac.improov.core.extension.visible
import com.rafaelfelipeac.improov.core.extension.setMessageColor
import com.rafaelfelipeac.improov.core.extension.isEmpty
import com.rafaelfelipeac.improov.core.extension.resetValue
import com.rafaelfelipeac.improov.core.extension.convertDateToString
import com.rafaelfelipeac.improov.features.goal.domain.model.Goal
import com.rafaelfelipeac.improov.features.goal.domain.model.Item
import com.rafaelfelipeac.improov.features.main.MainActivity
import java.util.Date
import java.util.Calendar
import javax.inject.Inject

@Suppress("TooManyFunctions")
abstract class BaseFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    protected val injector by lazy { (activity as BaseActivity).injector }

    val main get() = (activity as MainActivity)

    val fab get() = main.fab

    val navController get() = main.navController

    private val navLayout get() = main.navLayout

    private val fakeBottomNav get() = main.fakeBottomNav

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

    fun showNavigation() {
        navLayout.visible()
        fakeBottomNav.visible()
    }

    fun hideNavigation() {
        navLayout.gone()
        fakeBottomNav.gone()
    }

    fun showSnackBar(message: String) {
        Snackbar
            .make(requireView(), message, Snackbar.LENGTH_SHORT)
            .setMessageColor(R.color.colorPrimaryDarkOne)
            .show()
    }

    fun showSnackBarLong(message: String) {
        Snackbar
            .make(requireView(), message, Snackbar.LENGTH_LONG)
            .setMessageColor(R.color.colorPrimaryDarkOne)
            .show()
    }

    fun showSnackBarWithAction(
        view: View,
        message: String,
        obj: Any,
        function: (obj: Any) -> Unit
    ) {
        Snackbar
            .make(view, message, Snackbar.LENGTH_LONG)
            .setMessageColor(R.color.colorPrimaryDarkOne)
            .setAction(getString(R.string.snackbar_action_undo)) { function(obj) }
            .setActionTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimaryDarkOne))
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
        val inputMethodManager =
            activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager?
        inputMethodManager!!.toggleSoftInputFromWindow(
            view?.windowToken,
            InputMethodManager.SHOW_FORCED,
            0
        )
    }

    fun hideSoftKeyboard() {
        val inputMethodManager =
            this.activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager?
        inputMethodManager?.hideSoftInputFromWindow(this.view?.windowToken, 0)
    }

    fun getCurrentTime(): Date = Calendar.getInstance().time

    fun setupBottomSheetGoal() {
        bottomSheetGoal = BottomSheetDialog(main)
        val sheetView = layoutInflater.inflate(R.layout.bottom_sheet_goal, null)
        bottomSheetGoal.setContentView(sheetView)

        bottomSheetGoalYes = sheetView.findViewById(R.id.goal_done_yes)
        bottomSheetGoalNo = sheetView.findViewById(R.id.goal_done_no)
    }

    fun showBottomSheetGoal(
        goal: Goal,
        functionYes: (goal: Goal) -> Unit
    ) {
        bottomSheetGoal.show()

        bottomSheetGoalYes.setOnClickListener {
            functionYes(goal)
            hideBottomSheetGoal()
        }

        bottomSheetGoalNo.setOnClickListener {
            hideBottomSheetGoal()
        }
    }

    private fun hideBottomSheetGoal() {
        bottomSheetGoal.hide()
    }

    fun setupBottomSheetItem(newItem: (name: String) -> Unit, updateItem: (name: String) -> Unit) {
        bottomSheetItem = BottomSheetDialog(this.requireActivity())
        val sheetView = layoutInflater.inflate(R.layout.bottom_sheet_item, null)
        bottomSheetItem.setContentView(sheetView)

        bottomSheetItemSave = sheetView.findViewById(R.id.bottom_sheet_item_save)
        bottomSheetItemName = sheetView.findViewById(R.id.bottom_sheet_item_name)
        bottomSheetItemTitle = sheetView.findViewById(R.id.bottom_sheet_item_title)
        bottomSheetItemDate = sheetView.findViewById(R.id.bottom_sheet_item_date)
        bottomSheetItemEmptyName = sheetView.findViewById(R.id.bottom_sheet_item_empty_name)

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
        BottomSheetBehavior.from(bottomSheet!!).state = BottomSheetBehavior.STATE_EXPANDED

        if (item != null) {
            bottomSheetItemTitle.text = getString(R.string.item_title_edit)
            bottomSheetItemName.setText(item.name)

            if (item.done) {
                bottomSheetItemDate.text = String.format(
                    getString(R.string.item_date_format),
                    context?.let { item.doneDate?.convertDateToString(it) }
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
        bottomSheetTip = BottomSheetBehavior.from(main.findViewById<LinearLayout>(R.id.tips_divide_and_conquer))
        bottomSheetTipClose = main.findViewById(R.id.tips_divide_and_conquer_button_close)
    }

    fun setupBottomSheetTipsGoalType() {
        bottomSheetTip = BottomSheetBehavior.from(main.findViewById<LinearLayout>(R.id.tips_goal_type))
        bottomSheetTipClose = main.findViewById(R.id.tips_goal_type_button_close)
    }

    fun setupBottomSheetTipsSwipe() {
        bottomSheetTip = BottomSheetBehavior.from(main.findViewById<LinearLayout>(R.id.tips_swipe))
        bottomSheetTipClose = main.findViewById(R.id.tips_swipe_button_close)
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
