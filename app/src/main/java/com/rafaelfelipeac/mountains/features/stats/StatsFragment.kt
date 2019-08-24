package com.rafaelfelipeac.mountains.features.stats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.rafaelfelipeac.mountains.R
import com.rafaelfelipeac.mountains.core.extension.invisible
import com.rafaelfelipeac.mountains.core.extension.visible
import com.rafaelfelipeac.mountains.features.commons.GoalHabit
import com.rafaelfelipeac.mountains.core.platform.base.BaseFragment
import com.rafaelfelipeac.mountains.features.commons.Goal
import com.rafaelfelipeac.mountains.features.commons.Habit
import com.rafaelfelipeac.mountains.features.main.MainActivity
import kotlinx.android.synthetic.main.fragment_stats.*

class StatsFragment : BaseFragment() {

    var goals: List<Goal>? = listOf()
    var habits: List<Habit>? = listOf()
    var goalsFinal: MutableList<GoalHabit>? = mutableListOf()

    private val statsViewModel by lazy { viewModelFactory.get<StatsViewModel>(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        injector.inject(this)

        (activity as MainActivity).closeToolbar()

        setHasOptionsMenu(true)
    }

    override fun onResume() {
        super.onResume()

        (activity as MainActivity).closeToolbar()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        (activity as MainActivity).supportActionBar?.title = getString(R.string.stats_title)

        showNavigation()

        return inflater.inflate(R.layout.fragment_stats, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()

        fab.setOnClickListener {
            navController.navigate(StatsFragmentDirections.actionNavigationStatsToNavigationAdd())
        }
    }

    private fun observeViewModel() {
        statsViewModel.getGoals()?.observe(this, Observer { goals ->
            this.goals = goals.filter { it.userId == it.userId }

            setupStats()
        })

        statsViewModel.getHabits()?.observe(this, Observer { habits ->
            this.habits = habits.filter { it.userId == it.userId }

            setupStats()
        })
    }

    private fun setupStats() {
        if (goals?.isNotEmpty()!! || habits?.isNotEmpty()!!) {
            setStats()

            stats_default.visible()
            stats_placeholder.invisible()
        } else {
            stats_default.invisible()
            stats_placeholder.visible()
        }
    }

    private fun setStats() {
        goalsFinal = mutableListOf()

        goals?.let { goalsFinal!!.addAll(it) }
        habits?.let { goalsFinal!!.addAll(it) }

        val doneWithSingles = goals!!.filter { !it.divideAndConquer }
        val doneWithMountains = goals!!.filter { it.divideAndConquer }

        val quantitySingles = doneWithSingles.filter { it.value >= it.singleValue }.size
        val quantityBronze = doneWithMountains.filter { it.value >= it.bronzeValue }.size
        val quantitySilver = doneWithMountains.filter { it.value >= it.silverValue }.size
        val quantityGold = doneWithMountains.filter { it.value >= it.goldValue }.size

        stats_message.text = String.format(getString(R.string.stats_message), goals!!.filter { it.done }.size)
        stats_single_value.text = String.format(getString(R.string.stats_single_value), quantitySingles)
        stats_bronze_value.text = String.format(getString(R.string.stats_bronze_value), quantityBronze)
        stats_silver_value.text = String.format(getString(R.string.stats_silver_value), quantitySilver)
        stats_gold_value.text = String.format(getString(R.string.stats_gold_value), quantityGold)
    }
}

