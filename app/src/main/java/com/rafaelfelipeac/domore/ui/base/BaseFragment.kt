package com.rafaelfelipeac.domore.ui.base

import android.app.Activity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import androidx.fragment.app.Fragment
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import com.rafaelfelipeac.domore.R
import com.rafaelfelipeac.domore.app.App
import com.rafaelfelipeac.domore.extension.setMessageColor
import com.rafaelfelipeac.domore.ui.activities.MainActivity
import java.util.*

abstract class BaseFragment : Fragment() {

    protected val injector by lazy { (activity as BaseActivity).injector }

    var goalDAO = App.database?.goalDAO()
    var itemDAO = App.database?.itemDAO()
    var historicDAO = App.database?.historicDAO()

    val navController get() = (activity as MainActivity).navController

    val navigation: BottomNavigationView get() = (activity as MainActivity).findViewById(R.id.bottom_nav)

    fun showNavigation() {
        navigation.visibility = View.VISIBLE
    }

    fun hideNavigation() {
        navigation.visibility = View.GONE
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
            .setAction("DESFAZER") { function(obj) }
            .setActionTextColor(ContextCompat.getColor(context!!, R.color.colorPrimaryDarkOne))
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
