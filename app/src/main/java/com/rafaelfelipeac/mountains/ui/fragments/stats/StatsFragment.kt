package com.rafaelfelipeac.mountains.ui.fragments.stats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.rafaelfelipeac.mountains.R
import com.rafaelfelipeac.mountains.extension.invisible
import com.rafaelfelipeac.mountains.extension.visible
import com.rafaelfelipeac.mountains.models.Goal
import com.rafaelfelipeac.mountains.ui.activities.MainActivity
import com.rafaelfelipeac.mountains.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_stats.*

class StatsFragment : BaseFragment() {

    var goals: List<Goal>? = null

    private lateinit var viewModel: StatsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        injector.inject(this)

        (activity as MainActivity).closeToolbar()

        setHasOptionsMenu(true)

        (activity as MainActivity).bottomNavigationVisible(View.VISIBLE)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (activity as MainActivity).supportActionBar?.title = getString(R.string.fragment_title_stats)

        viewModel = ViewModelProviders.of(this).get(StatsViewModel::class.java)

        return inflater.inflate(R.layout.fragment_stats, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()

        showNavigation()

        (activity as MainActivity).closeBottomSheetDoneGoal()
    }

    private fun observeViewModel() {
        viewModel.getGoals()?.observe(this, Observer { goals ->
            this.goals = goals.filter { it.userId == it.userId }

            setupStats()
        })
    }

    private fun setupStats() {
        if (goals?.isNotEmpty()!!) {
            setStats()

            cl_stats_on.visible()
            cl_stats_off.invisible()
        } else {
            cl_stats_on.invisible()
            cl_stats_off.visible()
        }
    }

    private fun setStats() {
        val goals = goals!!

        val doneWithSingles = goals.filter { !it.mountains }
        val doneWithMountains = goals.filter { it.mountains }

        val quantitySingles = doneWithSingles.filter { it.value >= it.singleValue }.size
        val quantityBronze = doneWithMountains.filter { it.value >= it.bronzeValue }.size
        val quantitySilver = doneWithMountains.filter { it.value >= it.silverValue }.size
        val quantityGold = doneWithMountains.filter { it.value >= it.goldValue }.size

        stats_message.text = String.format(getString(R.string.stats_goals_completed_message), goals.filter { it.done }.size)
        stats_single_value.text = String.format(getString(R.string.stats_single_value), quantitySingles)
        stats_mountain_bronze_value.text = String.format(getString(R.string.stats_bronze_value), quantityBronze)
        stats_mountain_silver_value.text = String.format(getString(R.string.stats_silver_value), quantitySilver)
        stats_mountain_gold_value.text = String.format(getString(R.string.stats_gold_value), quantityGold)
    }
}

