package com.rafaelfelipeac.readmore.ui.base

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import com.rafaelfelipeac.readmore.app.App

@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity() {

    val injector get() = (application as App).appComponent

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        injector.inject(this)

        super.onCreate(savedInstanceState, persistentState)
    }
}