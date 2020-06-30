package com.rafaelfelipeac.improov.features.welcome.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.rafaelfelipeac.improov.R
import com.rafaelfelipeac.improov.core.extension.invisible
import com.rafaelfelipeac.improov.core.extension.observe
import com.rafaelfelipeac.improov.core.extension.visible
import com.rafaelfelipeac.improov.core.platform.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_welcome.*

class WelcomeFragment : BaseFragment() {

    private val viewModel by lazy { viewModelFactory.get<WelcomeViewModel>(this) }

    private val stateObserver = Observer<WelcomeViewModel.ViewState> { response ->
        if (response.welcome || response.welcomeSaved) {
            navController.navigate(WelcomeFragmentDirections.actionNavigationWelcomeToNavigationList())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        injector.inject(this)

        observe(viewModel.stateLiveData, stateObserver)
        viewModel.loadData()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        main.supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        main.supportActionBar?.title = getString(R.string.welcome_title)

        hideNavigation()

        main.closeToolbar()

        return inflater.inflate(R.layout.fragment_welcome, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        welcome_start_button.setOnClickListener {
            viewModel.onSaveWelcome(true)
        }

        welcome_viewpager.adapter =
            WelcomeAdapter(
                this,
                parentFragmentManager
            )
        welcome_dots.setupWithViewPager(welcome_viewpager, true)
    }

    fun showStartButton() {
        welcome_start_button?.visible()
    }

    fun hideStartButton() {
        welcome_start_button?.invisible()
    }
}
