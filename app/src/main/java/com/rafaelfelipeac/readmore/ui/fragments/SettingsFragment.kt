package com.rafaelfelipeac.readmore.ui.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.rafaelfelipeac.readmore.R
import com.rafaelfelipeac.readmore.ui.activities.MainActivity

class SettingsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (activity as MainActivity).supportActionBar?.title = "Settings"

        return inflater.inflate(R.layout.fragment_settings, container, false)
    }
}
