package com.rafaelfelipeac.domore.ui.fragments.stats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rafaelfelipeac.domore.R
import com.rafaelfelipeac.domore.ui.activities.MainActivity
import com.rafaelfelipeac.domore.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_stats.*

class StatsFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (activity as MainActivity).supportActionBar?.title = "Estatísticas"

        return inflater.inflate(R.layout.fragment_stats, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupStats()
    }

    private fun setupStats() {
        val goals = goalDAO?.getAll()

        val doneWithMedals = goals?.filter { !it.trophies }
        val doneWithTrophies = goals?.filter { it.trophies }

        val quantityMedal = doneWithMedals?.size
        val quantityBronze = doneWithTrophies?.filter { it.value >= it.bronzeValue }?.size
        val quantitySilver = doneWithTrophies?.filter { it.value >= it.silverValue }?.size
        val quantityGold = doneWithTrophies?.filter { it.value >= it.goldValue }?.size

        stats_medal_value.text = String.format("%dx", quantityMedal)
        stats_trophy_bronze_value.text = String.format("%dx", quantityBronze)
        stats_trophy_silver_value.text = String.format("%dx", quantitySilver)
        stats_trophy_gold_value.text = String.format("%dx", quantityGold)
    }
}

