package com.rafaelfelipeac.domore.ui.base

import android.app.Activity
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.inputmethod.InputMethodManager
import com.rafaelfelipeac.domore.app.App
import com.rafaelfelipeac.domore.ui.activities.MainActivity

open class BaseFragment : Fragment() {

    protected val injector by lazy { (activity as BaseActivity).injector }

    var goalDAO = App.database?.goalDAO()

    fun showSnackBar(message: String) {
        Snackbar.make(view!!, message, Snackbar.LENGTH_SHORT).show()
    }

    fun showError(message: String) {
        Snackbar.make(view!!, message, Snackbar.LENGTH_SHORT).show()
    }

//    fun progressBarVisibility(visible: Boolean) {
//        val progressBar = activity?.findViewById<ProgressBar>(R.id.main_progressBar)!!
//
//        if (visible) {
//            progressBar.visibility = View.VISIBLE
//        } else {
//            progressBar.visibility = View.GONE
//        }
//    }

    val navController get() = (activity as MainActivity).navController

    fun showSoftKeyboard(activity: Activity) {
        val inputMethodManager = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager?
        inputMethodManager!!.toggleSoftInputFromWindow(view?.windowToken, InputMethodManager.SHOW_FORCED, 0)
    }

    fun hideSoftKeyboard(activity: Activity) {
        val inputMethodManager = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager?
        inputMethodManager!!.hideSoftInputFromWindow(view?.windowToken, 0)
    }
}
