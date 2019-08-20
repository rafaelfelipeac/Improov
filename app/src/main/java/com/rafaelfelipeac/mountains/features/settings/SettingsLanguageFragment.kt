package com.rafaelfelipeac.mountains.features.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.rafaelfelipeac.mountains.R
import com.rafaelfelipeac.mountains.core.platform.base.BaseFragment
import com.rafaelfelipeac.mountains.features.main.MainActivity

class SettingsLanguageFragment : BaseFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        injector.inject(this)

        setHasOptionsMenu(true)
    }

    override fun onResume() {
        super.onResume()

        (activity as MainActivity).closeToolbar()
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        (activity as MainActivity).supportActionBar?.title = "Language"

        val view = inflater.inflate(R.layout.fragment_settings_language, container, false)

        val radioGroup = view.findViewById(R.id.settings_language_radio_group) as RadioGroup
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radioButtonLanguage1 -> {
                    Toast.makeText(context, "portugues", Toast.LENGTH_SHORT).show()
                }
                R.id.radioButtonLanguage2 -> {
                    Toast.makeText(context, "ingles", Toast.LENGTH_SHORT).show()
                }
            }
        }

        return view
    }
}
