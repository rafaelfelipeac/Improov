package com.rafaelfelipeac.mountains.features.settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.rafaelfelipeac.mountains.R
import com.rafaelfelipeac.mountains.core.extension.gone
import com.rafaelfelipeac.mountains.features.main.MainActivity

class SettingsFragment : PreferenceFragmentCompat() {

    val navController get() = (activity as MainActivity).navController

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }

    override fun onResume() {
        super.onResume()

        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as MainActivity).openToolbar()
        (activity as MainActivity).navLayout.gone()
        (activity as MainActivity).fakeBottomNav.gone()
        (activity as MainActivity).supportActionBar?.title = getString(R.string.settings_title)
    }

    override fun onPreferenceTreeClick(preference: Preference): Boolean {
        return when (preference.key) {
            getString(R.string.settings_pref_key_language_message) -> {
                navController.navigate(SettingsFragmentDirections.actionNavigationSettingsToNavigationSettingsLanguage())
                true
            }
            getString(R.string.settings_pref_key_about_rate) -> {
               false
                // abrir play store
            }
            getString(R.string.settings_pref_key_about_contact) -> {
                false
                // abrir email
            }
            getString(R.string.settings_pref_key_about_version) -> {
                false
                // pegar versão
            }
            else -> {
                super.onPreferenceTreeClick(preference)
            }
        }
    }
}
