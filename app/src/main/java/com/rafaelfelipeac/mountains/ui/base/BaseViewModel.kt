package com.rafaelfelipeac.mountains.ui.base

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

abstract class BaseViewModel : ViewModel() {

    var auth: FirebaseAuth = FirebaseAuth.getInstance()
}