package com.rafaelfelipeac.improov.core.platform.base

import androidx.navigation.NavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.rafaelfelipeac.improov.core.di.provider.ViewModelProvider
import com.rafaelfelipeac.improov.features.main.MainActivity

open class BaseBottomSheet : BottomSheetDialogFragment() {

    protected val viewModelProvider: ViewModelProvider
        get() = (activity as BaseActivity).viewModelProvider

    val main: MainActivity get() = (activity as MainActivity)

    val navController: NavController get() = main.navController
}
