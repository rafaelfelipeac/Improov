package com.rafaelfelipeac.domore.ui.base

import android.app.Activity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import androidx.fragment.app.Fragment
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.rafaelfelipeac.domore.R
import com.rafaelfelipeac.domore.app.App
import com.rafaelfelipeac.domore.ui.activities.MainActivity

open class BaseFragment : Fragment() {

    protected val injector by lazy { (activity as BaseActivity).injector }

    var goalDAO = App.database?.goalDAO()
    var itemDAO = App.database?.itemDAO()

    val navController get() = (activity as MainActivity).navController

    val navigation: BottomNavigationView get() = (activity as MainActivity).findViewById(R.id.bottom_nav)

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

    fun showSoftKeyboard(activity: Activity) {
        val inputMethodManager = activity.
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager?

        inputMethodManager!!.toggleSoftInputFromWindow(view?.windowToken, InputMethodManager.SHOW_FORCED, 0)
    }

    fun hideSoftKeyboard(activity: Activity) {
        val inputMethodManager = activity.
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager?

        inputMethodManager!!.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    fun showNavigation() { navigation.visibility = View.VISIBLE }

    fun hideNavigation() { navigation.visibility = View.GONE }
 }
