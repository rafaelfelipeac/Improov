package com.rafaelfelipeac.mountains.ui.fragments.otherGoal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.rafaelfelipeac.mountains.R
import com.rafaelfelipeac.mountains.ui.activities.MainActivity
import com.rafaelfelipeac.mountains.ui.base.BaseFragment

class OtherGoalFragment : BaseFragment() {

    var goalNew: Boolean? = null

    private lateinit var viewModel: OtherGoalViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //injector.inject(this)

        setHasOptionsMenu(true)

        (activity as MainActivity).openToolbar()

        (activity as MainActivity).bottomNavigationVisible(View.GONE)

        goalNew = arguments?.let { OtherGoalFragmentArgs.fromBundle(it).goalNew }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        (activity as MainActivity).supportActionBar?.title = getString(R.string.fragment_title_goal)

        viewModel = ViewModelProviders.of(this).get(OtherGoalViewModel::class.java)

        return inflater.inflate(R.layout.fragment_other_goal, container, false)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                if (goalNew!!) {
                    navController.navigateUp()
                }
            }
        }

        return false
    }

    override fun onStart() {
        super.onStart()

        hideNavigation()
    }
}
