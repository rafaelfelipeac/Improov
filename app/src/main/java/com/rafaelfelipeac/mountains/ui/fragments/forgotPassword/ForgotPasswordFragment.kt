package com.rafaelfelipeac.mountains.ui.fragments.forgotPassword

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.rafaelfelipeac.mountains.R
import com.rafaelfelipeac.mountains.ui.activities.MainActivity
import com.rafaelfelipeac.mountains.ui.base.BaseFragment

class ForgotPasswordFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        (activity as MainActivity).supportActionBar?.title = "ForgotPassword"

        hideNavigation()

        return inflater.inflate(R.layout.fragment_forgot_password, container, false)
    }
}
