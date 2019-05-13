package com.rafaelfelipeac.mountains.ui.fragments.createUser

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.rafaelfelipeac.mountains.R
import com.rafaelfelipeac.mountains.extension.visible
import com.rafaelfelipeac.mountains.ui.activities.MainActivity
import com.rafaelfelipeac.mountains.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_create_user.*

class CreateUserFragment : BaseFragment() {

    private lateinit var viewModel: CreateUserViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        (activity as MainActivity).supportActionBar?.title = getString(R.string.fragment_create_user_title)

        viewModel = ViewModelProviders.of(this).get(CreateUserViewModel::class.java)

        hideNavigation()

        return inflater.inflate(R.layout.fragment_create_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()

        create_user_create_button.setOnClickListener {
            // verificar email
            // verificar se email existe
            // verificar senha (min)
            // verificar se senha coincide

            if (verifyElements()) {
                viewModel.createUser(create_user_email.text.toString(), create_user_password.text.toString())
            } else {
                create_user_error_message.text = ""
                create_user_error_message.visible()
            }
        }
    }

    private fun verifyElements(): Boolean {
        return create_user_name.text.toString().isNotEmpty() &&
                create_user_email.text.toString().isNotEmpty() &&
                create_user_password.text.toString().isNotEmpty() &&
                create_user_confirm_password.text.toString().isNotEmpty()
    }

    private fun observeViewModel() {
        viewModel.createUserSuccess.observe(this, Observer { createUser ->
            if (createUser) {
                navController.navigate(CreateUserFragmentDirections.actionNavigationCreateUserToNavigationGoals())
            } else {
                showSnackBar("create error")
            }
        })
    }


}
