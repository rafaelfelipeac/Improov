package com.rafaelfelipeac.readmore.ui.base

import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.View
import android.widget.ProgressBar
import com.rafaelfelipeac.readmore.R
import com.rafaelfelipeac.readmore.ui.activities.MainActivity

open class BaseFragment : Fragment() {

    protected val injector by lazy { (activity as BaseActivity).injector }

    var viewModel: BaseViewModel? = null

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
}
