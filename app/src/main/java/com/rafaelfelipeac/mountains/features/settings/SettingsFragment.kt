package com.rafaelfelipeac.mountains.features.settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceFragmentCompat
import com.rafaelfelipeac.mountains.R
import com.rafaelfelipeac.mountains.core.extension.gone
import com.rafaelfelipeac.mountains.features.main.MainActivity

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        (activity as MainActivity).supportActionBar?.title = "Settings"

        (activity as MainActivity).openToolbar()
        (activity as MainActivity).navLayout.gone()
        (activity as MainActivity).fakeBottomNav.gone()
    }
}