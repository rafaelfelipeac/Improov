package com.rafaelfelipeac.readmore.ui.fragments.book

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.rafaelfelipeac.readmore.R
import com.rafaelfelipeac.readmore.ui.activities.MainActivity

class BookFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        (activity as MainActivity).supportActionBar?.title = "Livro"

        return inflater.inflate(R.layout.fragment_book, container, false)
    }
}
