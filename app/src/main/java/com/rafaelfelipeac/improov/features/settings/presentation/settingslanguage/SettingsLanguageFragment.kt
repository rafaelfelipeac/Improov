package com.rafaelfelipeac.improov.features.settings.presentation.settingslanguage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rafaelfelipeac.improov.R
import com.rafaelfelipeac.improov.core.LocaleHelper
import com.rafaelfelipeac.improov.core.extension.observe
import com.rafaelfelipeac.improov.core.platform.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_settings_language.*

class SettingsLanguageFragment : BaseFragment() {

    private val viewModel by lazy { viewModelFactory.get<SettingsLanguageViewModel>(this) }

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

        return inflater.inflate(R.layout.fragment_settings_language, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()
    }

    private fun setScreen() {
        setTitle(getString(R.string.settings_language_language_title))
        showBackArrow()
    }

    private fun observeViewModel() {
        viewModel.saved.observe(this) {
            recreateFragment()
        }

        viewModel.language.observe(this) {
            LocaleHelper.setLocale(requireContext(), it)

            setupLanguage(it)
        }
    }

    private fun setupLanguage(language: String) {
        when (language) {
            getString(R.string.settings_language_key_portuguese) -> {
                settingsLanguageRadioPortuguese.isChecked = true
            }
            getString(R.string.settings_language_key_english) -> {
                settingsLanguageRadioEnglish.isChecked = true
            }
        }

        setupLayout()
    }

    private fun setupLayout() {
        settingsLanguageRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.settingsLanguageRadioPortuguese -> {
                    viewModel.saveLanguage(getString(R.string.settings_language_key_portuguese))
                }
                R.id.settingsLanguageRadioEnglish -> {
                    viewModel.saveLanguage(getString(R.string.settings_language_key_english))
                }
            }
        }
    }

    private fun recreateFragment() {
        activity?.recreate()
        navController.navigateUp()
    }
}
