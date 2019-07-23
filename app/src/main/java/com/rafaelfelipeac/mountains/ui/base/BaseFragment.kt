package com.rafaelfelipeac.mountains.ui.base

import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.rafaelfelipeac.mountains.R
import com.rafaelfelipeac.mountains.extension.gone
import com.rafaelfelipeac.mountains.extension.setMessageColor
import com.rafaelfelipeac.mountains.extension.visible
import com.rafaelfelipeac.mountains.models.Goal
import com.rafaelfelipeac.mountains.ui.activities.MainActivity
import java.util.*

abstract class BaseFragment : Fragment() {

    protected val injector by lazy { (activity as BaseActivity).injector }

    val navController get() = (activity as MainActivity).navController

    private val navLayout get() = (activity as MainActivity).navLayout

    private val fakeBottomNav get() = (activity as MainActivity).fakeBottomNav

    val fab get() = (activity as MainActivity).fab

    val mGoogleSignInClient get() = (activity as MainActivity).mGoogleSignInClient

    val user get () = (activity as MainActivity).user
    var userFirebase = FirebaseAuth.getInstance().currentUser

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

    fun showSnackBarWithAction(view: View, message: String, obj: Any, function: (obj: Any) -> Unit) {
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
            .setPositiveButton("OK") { _, _ -> function() }
            .setNegativeButton("Cancelar") { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
    }

    fun showSoftKeyboard(activity: Activity) {
        val inputMethodManager = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager?
        inputMethodManager!!.toggleSoftInputFromWindow(view?.windowToken, InputMethodManager.SHOW_FORCED, 0)
    }

    fun hideSoftKeyboard(view: View, activity: Activity?) {
        val inputMethodManager = activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager?
        inputMethodManager?.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun getCurrentTime() = Calendar.getInstance().time!!
}
