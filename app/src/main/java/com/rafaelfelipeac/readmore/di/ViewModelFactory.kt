package com.rafaelfelipeac.readmore.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.support.v7.app.AppCompatActivity
import com.rafaelfelipeac.readmore.network.RMApi
import com.rafaelfelipeac.readmore.network.RetrofitClient
import com.rafaelfelipeac.readmore.ui.fragments.home.HomeViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val activity: AppCompatActivity) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) ->
                HomeViewModel(RetrofitClient().getClient().create(RMApi::class.java)) as T

            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
