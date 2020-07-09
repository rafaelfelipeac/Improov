package com.rafaelfelipeac.improov.features.splash.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rafaelfelipeac.improov.R
import com.rafaelfelipeac.improov.core.extension.observe
import com.rafaelfelipeac.improov.core.platform.base.BaseFragment

class SplashFragment : BaseFragment() {

    private val viewModel by lazy { viewModelFactory.get<SplashViewModel>(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        injector.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        setScreen()

        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.loadData()

        observeViewModel()
    }

    private fun setScreen() {
        hideToolbar()
        hideNavigation()
    }

    private fun observeViewModel() {
        viewModel.welcome.observe(this) {
            if (it) {
                navController.navigate(SplashFragmentDirections.splashToList())
            } else {
                navController.navigate(SplashFragmentDirections.splashToWelcome())
            }
        }
    }
}
