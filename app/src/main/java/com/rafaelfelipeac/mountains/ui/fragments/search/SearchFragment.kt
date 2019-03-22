package com.rafaelfelipeac.mountains.ui.fragments.search

import android.os.Bundle
import androidx.constraintlayout.widget.ConstraintLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rafaelfelipeac.mountains.R
import com.rafaelfelipeac.mountains.ui.activities.MainActivity
import com.rafaelfelipeac.mountains.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_search.*

class SearchFragment : BaseFragment() {

    private var open: Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (activity as MainActivity).supportActionBar?.title = "Pesquisar"

        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity).closeBottomSheetDoneGoal()

        search_edittext.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            open = !open

            if (!hasFocus) {
                hideSoftKeyboard(view, activity!!)
            }

            verifyOpenOrClose()
        }

        search_imageview.setOnClickListener {
            if (search_edittext.text?.isEmpty()!! && !open) {
                open = !open

                if (open) {
                    search_edittext.requestFocus()
                    open = !open
                }

                verifyOpenOrClose()
            } else {
                hideSoftKeyboard(view, activity!!)
                showSnackBar("Pesquisa.")
            }
        }

        frame_layout_search.setOnClickListener {
            if (open) {
                open = !open

                verifyOpenOrClose()
            }
        }
    }

    private fun verifyOpenOrClose() = if (open) openSearch() else closeSearch()

    private fun openSearch() {
        setVerticalBias(search_edittext, 0f)
        setVerticalBias(search_imageview, 0f)

        showSoftKeyboard(activity!!)
    }

    private fun closeSearch() {
        setVerticalBias(search_edittext, 0.5f)
        setVerticalBias(search_imageview, 0.5f)

        hideSoftKeyboard(view!!, activity!!)
    }

    private fun setVerticalBias(view: View, value: Float) {
        val params = view.layoutParams as ConstraintLayout.LayoutParams
        params.verticalBias = value
        view.layoutParams = params
    }
}
