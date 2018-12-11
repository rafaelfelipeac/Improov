package com.rafaelfelipeac.readmore.ui.fragments

import android.app.Activity
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import com.rafaelfelipeac.readmore.R
import com.rafaelfelipeac.readmore.ui.activities.MainActivity
import kotlinx.android.synthetic.main.fragment_search.*


class SearchFragment : Fragment() {

    private var open: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as MainActivity).supportActionBar?.title = "Search"

        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        search_edittext.onFocusChangeListener = OnFocusChangeListener { _, hasFocus ->
            open = !open

            if (!hasFocus) {
                hideSoftKeyboard(activity!!)
            }

            verifyOpenOrClose()
        }

        search_imageview.setOnClickListener {
            open = !open

            if (open) {
                search_edittext.requestFocus()
                open = !open
            }

            verifyOpenOrClose()
        }

        frame_layout_search.setOnClickListener {
            if (open) {
                open = !open

                verifyOpenOrClose()
            }
        }
    }

    private fun openSearch() {
        setVerticalBias(search_edittext, 0f)
        setVerticalBias(search_imageview, 0f)

        showSoftKeyboard(activity!!)
    }

    private fun closeSearch() {
        setVerticalBias(search_edittext, 0.5f)
        setVerticalBias(search_imageview, 0.5f)

        hideSoftKeyboard(activity!!)
    }

    private fun setVerticalBias(view: View, value: Float) {
        val params = view.layoutParams as ConstraintLayout.LayoutParams
        params.verticalBias = value
        view.layoutParams = params
    }

    private fun verifyOpenOrClose() {
        if (open)
            openSearch()
        else
            closeSearch()
    }

    private fun showSoftKeyboard(activity: Activity) {
        val inputMethodManager = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager?
        inputMethodManager!!.toggleSoftInputFromWindow(view?.windowToken, InputMethodManager.SHOW_FORCED, 0)
    }

    private fun hideSoftKeyboard(activity: Activity) {
        val inputMethodManager = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager?
        inputMethodManager!!.hideSoftInputFromWindow(view?.windowToken, 0)
    }

}
