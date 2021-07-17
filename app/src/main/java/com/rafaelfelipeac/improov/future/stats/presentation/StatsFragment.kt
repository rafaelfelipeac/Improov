package com.rafaelfelipeac.improov.future.stats.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.rafaelfelipeac.improov.R
import com.rafaelfelipeac.improov.core.extension.viewBinding
import com.rafaelfelipeac.improov.core.platform.base.BaseFragment
import com.rafaelfelipeac.improov.databinding.FragmentStatsBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class StatsFragment : BaseFragment() {

    private var binding by viewBinding<FragmentStatsBinding>()

    private val viewModel by lazy { viewModelProvider.statsViewModel() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        setScreen()

        return FragmentStatsBinding.inflate(inflater, container, false).run {
            binding = this
            binding.root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupBehaviours()
        observeViewModel()
    }

    private fun setScreen() {
        hideToolbar()
        showNavigation()
        hideBottomSheetTips()
    }

    private fun setupBehaviours() {
        fab.setOnClickListener {
            hideBottomSheetTips()

            navController.navigate(StatsFragmentDirections.statsToAdd())
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.singleValue.collect {
                binding.statsSingleValue.text =
                    String.format(getString(R.string.stats_value), it)
            }
        }

        lifecycleScope.launch {
            viewModel.bronzeValue.collect {
                binding.statsBronzeValue.text =
                    String.format(getString(R.string.stats_value), it)
            }
        }

        lifecycleScope.launch {
            viewModel.silverValue.collect {
                binding.statsSilverValue.text =
                    String.format(getString(R.string.stats_value), it)
            }
        }

        lifecycleScope.launch {
            viewModel.goldValue.collect {
                binding.statsGoldValue.text =
                    String.format(getString(R.string.stats_value), it)
            }
        }

        lifecycleScope.launch {
            viewModel.habitsValue.collect {
                binding.statsHabitsValue.text =
                    String.format(getString(R.string.stats_value), it)
            }
        }
    }
}
