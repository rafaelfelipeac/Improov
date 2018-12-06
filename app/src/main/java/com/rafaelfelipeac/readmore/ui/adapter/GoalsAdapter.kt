package com.rafaelfelipeac.readmore.ui.adapter

import android.view.View
import com.rafaelfelipeac.readmore.R
import com.rafaelfelipeac.readmore.models.Goal
import com.rafaelfelipeac.readmore.ui.base.BaseAdapter
import javax.inject.Inject

class GoalsAdapter @Inject constructor() : BaseAdapter<Goal>() {

    var clickListener: (goal: Goal) -> Unit = { }

    override fun getLayoutRes(): Int = R.layout.list_item_goal

    override fun View.bindView(item: Goal, viewHolder: ViewHolder) {
        setOnClickListener { clickListener(item) }
    }
}
