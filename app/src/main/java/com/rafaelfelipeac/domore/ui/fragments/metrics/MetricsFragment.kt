package com.rafaelfelipeac.domore.ui.fragments.metrics

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rafaelfelipeac.domore.R
import com.rafaelfelipeac.domore.ui.activities.MainActivity

class MetricsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (activity as MainActivity).supportActionBar?.title = "Metricas"

        return inflater.inflate(R.layout.fragment_metrics, container, false)
    }
}
