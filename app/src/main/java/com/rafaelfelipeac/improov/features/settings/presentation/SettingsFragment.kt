package com.rafaelfelipeac.improov.features.settings.presentation

import android.content.ActivityNotFoundException
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.rafaelfelipeac.improov.R
import com.rafaelfelipeac.improov.core.extension.gone
import com.rafaelfelipeac.improov.features.main.MainActivity
import android.content.Intent
import android.net.Uri
import com.rafaelfelipeac.improov.core.platform.AppConfig.MARKET_BASE_URL
import com.rafaelfelipeac.improov.core.platform.AppConfig.PLAY_STORE_BASE_URL

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
                val appPackageName = activity?.packageName

                try {
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("$MARKET_BASE_URL$appPackageName")
                        )
                    )
                } catch (exception: ActivityNotFoundException) {
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("$PLAY_STORE_BASE_URL$appPackageName")
                        )
                    )
                }

                false
            }
            getString(R.string.settings_pref_key_about_contact) -> {
                val emailIntent = Intent(
                    Intent.ACTION_SENDTO,
                    Uri.fromParts("mailto", getString(R.string.settings_email_to), null)
                )

                emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.settings_email_title))

                startActivity(
                    Intent.createChooser(
                        emailIntent,
                        getString(R.string.settings_email_sender_title)
                    )
                )

                false
            }
            getString(R.string.settings_pref_key_about_version) -> {
                false
            }
            else -> {
                super.onPreferenceTreeClick(preference)
            }
        }
    }
}
