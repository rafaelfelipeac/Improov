package com.rafaelfelipeac.improov.core.extension

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

const val VIBRATE_SECONDS = 20L

fun Fragment.vibrate() {
    val vibrator = context?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        vibrator.vibrate(VibrationEffect.createOneShot(VIBRATE_SECONDS, VibrationEffect.DEFAULT_AMPLITUDE))
    } else {
        @Suppress("DEPRECATION")
        vibrator.vibrate(VIBRATE_SECONDS)
    }
}

fun <T> Fragment.viewBinding(): ReadWriteProperty<Fragment, T> =
    object : ReadWriteProperty<Fragment, T>, DefaultLifecycleObserver {
        private var binding: T? = null

        init {
            viewLifecycleOwnerLiveData.observe(this@viewBinding) { owner: LifecycleOwner? ->
                owner?.lifecycle?.addObserver(this)
            }
        }

        override fun onDestroy(owner: LifecycleOwner) {
            binding = null
        }

        override fun getValue(thisRef: Fragment, property: KProperty<*>): T =
            binding ?: error("Called before onCreateView or after onDestroyView.")

        override fun setValue(thisRef: Fragment, property: KProperty<*>, value: T) {
            binding = value
        }
    }
