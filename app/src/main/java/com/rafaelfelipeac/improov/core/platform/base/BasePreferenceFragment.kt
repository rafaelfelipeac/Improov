package com.rafaelfelipeac.improov.core.platform.base

import androidx.preference.PreferenceFragmentCompat
import com.rafaelfelipeac.improov.features.main.MainActivity

abstract class BasePreferenceFragment: PreferenceFragmentCompat() {

    val main get() = (activity as MainActivity)

    val navController get() = main.navController

}
