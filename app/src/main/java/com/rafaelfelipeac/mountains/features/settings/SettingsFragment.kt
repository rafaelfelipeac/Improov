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
        (activity as MainActivity).supportActionBar?.title = getString(R.string.fragment_title_settings)
    }

    override fun onPreferenceTreeClick(preference: Preference): Boolean {
        return when (preference.key) {
            getString(R.string.pref_key_language) -> {
                navController.navigate(SettingsFragmentDirections.actionNavigationSettingsToNavigationSettingsLanguage())
                true
            }
            getString(R.string.pref_key_rate) -> {
               false
                // abrir play store
            }
            getString(R.string.pref_key_contact) -> {
                false
                // abrir email
            }
            getString(R.string.pref_key_version) -> {
                false
            }
            else -> {
                super.onPreferenceTreeClick(preference)
            }
        }
    }
}
