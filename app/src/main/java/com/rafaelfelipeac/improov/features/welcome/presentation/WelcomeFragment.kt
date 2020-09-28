package com.rafaelfelipeac.improov.features.welcome.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rafaelfelipeac.improov.R
import com.rafaelfelipeac.improov.core.extension.invisible
import com.rafaelfelipeac.improov.core.extension.observe
import com.rafaelfelipeac.improov.core.extension.visible
import com.rafaelfelipeac.improov.core.platform.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_welcome.*

class WelcomeFragment : BaseFragment() {

    private val viewModel by lazy { viewModelFactory.get<WelcomeViewModel>(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        injector.inject(this)

        viewModel.loadData()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        setScreen()

        return inflater.inflate(R.layout.fragment_welcome, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupLayout()
        observeViewModel()
    }

    private fun setScreen() {
        hideToolbar()
        hideNavigation()
    }

    private fun setupLayout() {
        welcomeStartButton.setOnClickListener {
            viewModel.saveWelcome(true)
        }

        welcomeViewPager.adapter =
            WelcomeAdapter(
                this,
                parentFragmentManager
            )
        welcomeDots.setupWithViewPager(welcomeViewPager, true)
    }

    private fun observeViewModel() {
        viewModel.saved.observe(this) {
            navController.navigate(WelcomeFragmentDirections.welcomeToList())
        }
    }

    fun showStartButton() {
        welcomeStartButton?.visible()
    }

    fun hideStartButton() {
        welcomeStartButton?.invisible()
    }
}
