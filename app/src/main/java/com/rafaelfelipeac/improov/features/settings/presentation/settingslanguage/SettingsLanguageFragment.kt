package com.rafaelfelipeac.improov.features.settings.presentation.settingslanguage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.rafaelfelipeac.improov.R
import com.rafaelfelipeac.improov.core.extension.observe
import com.rafaelfelipeac.improov.core.platform.base.BaseFragment
import com.rafaelfelipeac.improov.core.LocaleHelper
import kotlinx.android.synthetic.main.fragment_settings_language.*

class SettingsLanguageFragment : BaseFragment() {

    private val viewModel by lazy { viewModelFactory.get<SettingsLanguageViewModel>(this) }

    private val stateObserver = Observer<SettingsLanguageViewModel.ViewState> { response ->
        if (!response.languageSaved) {
            LocaleHelper.setLocale(
                context!!,
                response.language
            )

            setupLanguage(response.language)
        } else {
            recreateFragment()
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
        main.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        main.supportActionBar?.title = getString(R.string.settings_language_language_title)

        return inflater.inflate(R.layout.fragment_settings_language, container, false)
    }

    private fun setupLanguage(language: String) {
        when (language) {
            getString(R.string.settings_language_key_portuguese) -> {
                settings_language_radio_portuguese.isChecked = true
            }
            getString(R.string.settings_language_key_english) -> {
                settings_language_radio_english.isChecked = true
            }
        }

        setupLayout()
    }

    private fun setupLayout() {
        settings_language_radio_group.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.settings_language_radio_portuguese -> {
                    viewModel.onSaveLanguage(getString(R.string.settings_language_key_portuguese))
                }
                R.id.settings_language_radio_english -> {
                    viewModel.onSaveLanguage(getString(R.string.settings_language_key_english))
                }
            }
        }
    }

    private fun recreateFragment() {
        activity?.recreate()
        navController.navigateUp()
    }
}
