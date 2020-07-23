package com.rafaelfelipeac.improov.features.settings.presentation.settings

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.preference.Preference
import com.rafaelfelipeac.improov.R
import com.rafaelfelipeac.improov.core.platform.AppConfig.MARKET_BASE_URL
import com.rafaelfelipeac.improov.core.platform.AppConfig.PLAY_STORE_BASE_URL
import com.rafaelfelipeac.improov.core.platform.base.BasePreferenceFragment

class SettingsFragment : BasePreferenceFragment() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        setScreen()

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onPreferenceTreeClick(preference: Preference): Boolean {
        return when (preference.key) {
            getString(R.string.settings_pref_key_language_message) -> {
                navController.navigate(
                    SettingsFragmentDirections.settingsToSettingsLanguage()
                )
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

    private fun setScreen() {
        setTitle(getString(R.string.settings_title))
        showBackArrow()
        hasMenu()
        hideNavigation()
    }
}
