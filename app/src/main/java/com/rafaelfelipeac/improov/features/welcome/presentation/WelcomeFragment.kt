package com.rafaelfelipeac.improov.features.welcome.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.viewpager.widget.ViewPager
import com.rafaelfelipeac.improov.core.extension.invisible
import com.rafaelfelipeac.improov.core.extension.viewBinding
import com.rafaelfelipeac.improov.core.extension.visible
import com.rafaelfelipeac.improov.core.platform.base.BaseFragment
import com.rafaelfelipeac.improov.databinding.FragmentWelcomeBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

const val INDEX_LAST_WELCOME = 2

class WelcomeFragment : BaseFragment() {

    private val viewModel by lazy { viewModelProvider.welcomeViewModel() }

    private var binding by viewBinding<FragmentWelcomeBinding>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        setScreen()

        return FragmentWelcomeBinding.inflate(inflater, container, false).run {
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
        hideNavigation()
    }

    private fun setupBehaviours() {
        binding.welcomeStartButton.setOnClickListener {
            viewModel.saveWelcome(true)
        }

        binding.welcomeViewPager.adapter = WelcomeAdapter(parentFragmentManager)

        binding.welcomeViewPager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                if (position == INDEX_LAST_WELCOME) {
                    showStartButton()
                } else {
                    hideStartButton()
                }
            }
        })

        binding.welcomeDots.setupWithViewPager(binding.welcomeViewPager, true)
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.saved.collect {
                navController.navigate(WelcomeFragmentDirections.welcomeToList())
            }
        }
    }

    fun showStartButton() {
        binding.welcomeStartButton.visible()
    }

    fun hideStartButton() {
        binding.welcomeStartButton.invisible()
    }
}
