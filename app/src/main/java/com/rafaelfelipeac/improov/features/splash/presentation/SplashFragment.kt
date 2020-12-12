package com.rafaelfelipeac.improov.features.splash.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.rafaelfelipeac.improov.core.extension.viewBinding
import com.rafaelfelipeac.improov.core.platform.base.BaseFragment
import com.rafaelfelipeac.improov.databinding.FragmentSplashBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SplashFragment : BaseFragment() {

    private val viewModel by lazy { viewModelProvider.splashViewModel() }

    private var binding by viewBinding<FragmentSplashBinding>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return FragmentSplashBinding.inflate(inflater, container, false).run {
            binding = this
            binding.root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.loadData()

        observeViewModel()
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.welcome.collect {
                if (it) {
                    navController.navigate(SplashFragmentDirections.splashToList())
                } else {
                    navController.navigate(SplashFragmentDirections.splashToWelcome())
                }
            }
        }
    }
}
