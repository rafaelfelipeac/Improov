package com.rafaelfelipeac.mountains.ui.fragments.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.rafaelfelipeac.mountains.R
import com.rafaelfelipeac.mountains.ui.activities.MainActivity

class SettingsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (activity as MainActivity).supportActionBar?.title = "Settings"

        return inflater.inflate(R.layout.fragment_settings, container, false)
    }
}
