package com.rafaelfelipeac.improov.core.platform.base

import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.rafaelfelipeac.improov.R
import com.rafaelfelipeac.improov.core.persistence.sharedpreferences.Preferences
import com.rafaelfelipeac.improov.core.di.modules.viewModel.ViewModelFactory
import com.rafaelfelipeac.improov.core.extension.gone
import com.rafaelfelipeac.improov.core.extension.setMessageColor
import com.rafaelfelipeac.improov.core.extension.visible
import com.rafaelfelipeac.improov.features.main.MainActivity
import java.util.*
import javax.inject.Inject

abstract class BaseFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var preferences: Preferences

    val main get() = (activity as MainActivity)

    protected val injector by lazy { (activity as BaseActivity).injector }

    val navController get() = main.navController

    private val navLayout get() = main.navLayout

    private val fakeBottomNav get() = main.fakeBottomNav

    val fab get() = main.fab

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
            .make(view!!, message, Snackbar.LENGTH_SHORT)
            .setMessageColor(R.color.colorPrimaryDarkOne)
            .show()
    }

    fun showSnackBarLong(message: String) {
        Snackbar
            .make(view!!, message, Snackbar.LENGTH_LONG)
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
            .setActionTextColor(ContextCompat.getColor(context!!, R.color.colorPrimaryDarkOne))
            .show()
    }

    fun showDialogWithAction(title: String, function: () -> Unit) {
        AlertDialog.Builder(context!!)
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

    fun getCurrentTime() = Calendar.getInstance().time!!
}
