package com.rafaelfelipeac.improov.features.settings.presentation.settingslanguage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.rafaelfelipeac.improov.R
import com.rafaelfelipeac.improov.core.LocaleHelper
import com.rafaelfelipeac.improov.core.extension.viewBinding
import com.rafaelfelipeac.improov.core.platform.base.BaseFragment
import com.rafaelfelipeac.improov.databinding.FragmentSettingsLanguageBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SettingsLanguageFragment : BaseFragment() {

    private val viewModel by lazy { viewModelProvider.settingsLanguageViewModel() }

    private var binding by viewBinding<FragmentSettingsLanguageBinding>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        setScreen()

        return FragmentSettingsLanguageBinding.inflate(inflater, container, false).run {
            binding = this
            binding.root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.loadData()

        observeViewModel()
    }

    private fun setScreen() {
        setTitle(getString(R.string.settings_language_language_title))
        showBackArrow()
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.saved.collect {
                recreateFragment()
            }
        }

        lifecycleScope.launch {
            viewModel.language.collect {
                LocaleHelper.setLocale(requireContext(), it)

                setupLanguage(it)
            }
        }
    }

    private fun setupLanguage(language: String) {
        when (language) {
            getString(R.string.settings_language_key_portuguese) -> {
                binding.settingsLanguageRadioPortuguese.isChecked = true
            }
            getString(R.string.settings_language_key_english) -> {
                binding.settingsLanguageRadioEnglish.isChecked = true
            }
        }

        setupLayout()
    }

    private fun setupLayout() {
        binding.settingsLanguageRadioGroup.setOnCheckedChangeListener { _, checkedId ->
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
