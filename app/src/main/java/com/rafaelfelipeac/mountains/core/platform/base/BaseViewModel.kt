package com.rafaelfelipeac.mountains.core.platform.base

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

abstract class BaseViewModel : ViewModel() {

    var auth: FirebaseAuth = FirebaseAuth.getInstance()
}