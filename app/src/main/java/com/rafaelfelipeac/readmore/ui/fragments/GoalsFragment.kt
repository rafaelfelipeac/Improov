package com.rafaelfelipeac.readmore.ui.fragments

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rafaelfelipeac.readmore.R
import com.rafaelfelipeac.readmore.models.Goal
import com.rafaelfelipeac.readmore.ui.activities.MainActivity
import com.rafaelfelipeac.readmore.ui.adapter.GoalsAdapter
import com.rafaelfelipeac.readmore.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_goals.*
import javax.inject.Inject

class GoalsFragment : BaseFragment() {

    @Inject
    lateinit var adapter: GoalsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        injector.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        (activity as MainActivity).supportActionBar?.title = "Goals"

        return inflater.inflate(R.layout.fragment_goals, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fab_goals.setOnClickListener {
            navController.navigate(R.id.action_navigation_metas_to_goalFormFragment)
        }

        adapter.setItems(goalDAO?.getAll()!!)

        adapter.clickListener = {
            navController.navigate(R.id.action_navigation_metas_to_goalFragment)
        }

        goals_list.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        goals_list.adapter = adapter
    }
}
