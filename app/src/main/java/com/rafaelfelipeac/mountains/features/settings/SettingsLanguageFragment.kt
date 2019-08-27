package com.rafaelfelipeac.mountains.features.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.rafaelfelipeac.mountains.R
import com.rafaelfelipeac.mountains.core.persistence.sharedpreferences.Preferences
import com.rafaelfelipeac.mountains.core.platform.base.BaseFragment
import com.rafaelfelipeac.mountains.features.main.MainActivity
import kotlinx.android.synthetic.main.fragment_settings_language.*

class SettingsLanguageFragment : BaseFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        injector.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        (activity as MainActivity).supportActionBar?.title = getString(R.string.settings_language_language_title)

        return inflater.inflate(R.layout.fragment_settings_language, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupLayout()
    }

    private fun setupLayout() {
        when (Preferences(context!!).language) {
            getString(R.string.settings_language_key_portuguese) -> settings_language_radio_portuguese.isChecked = true
            getString(R.string.settings_language_key_english) -> settings_language_radio_english.isChecked = true
        }

        settings_language_radio_group.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.settings_language_radio_portuguese -> {
                    LocaleHelper.setLocale(activity!!, getString(R.string.settings_language_key_portuguese))
                }
                R.id.settings_language_radio_english -> {
                    LocaleHelper.setLocale(activity!!, getString(R.string.settings_language_key_english))
                }
            }

            activity?.recreate()
            navController.navigateUp()
        }
    }
}
