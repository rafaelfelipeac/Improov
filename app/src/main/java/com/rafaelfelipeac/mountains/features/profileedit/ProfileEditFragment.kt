package com.rafaelfelipeac.mountains.features.profileedit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.rafaelfelipeac.mountains.R
import com.rafaelfelipeac.mountains.core.extension.isEmpty
import com.rafaelfelipeac.mountains.core.platform.base.BaseFragment
import com.rafaelfelipeac.mountains.features.main.MainActivity
import kotlinx.android.synthetic.main.fragment_profile_edit.*

class ProfileEditFragment : BaseFragment() {

    private val profileEditViewModel by lazy { viewModelFactory.get<ProfileEditViewModel>(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        injector.inject(this)

        (activity as MainActivity).openToolbar()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()

        profile_edit_name.setText(preferences.name)

        profile_edit_save_button.setOnClickListener {
            if (verifyElements()) {
                hideSoftKeyboard()
                preferences.name = profile_edit_name.text.toString()
                navController.navigateUp()
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        (activity as MainActivity).supportActionBar?.title = getString(R.string.profile_edit_title)

        hideNavigation()

        return inflater.inflate(R.layout.fragment_profile_edit, container, false)
    }

    private fun verifyElements(): Boolean {
        when {
            profile_edit_name.isEmpty() -> {
                setErrorMessage(getString(R.string.profile_edit_empty_fields))
                return false
            }
        }

        return true
    }

    private fun observeViewModel() { }

    private fun setErrorMessage(message: String) {
        profile_edit_error_message.text = message
    }
}
