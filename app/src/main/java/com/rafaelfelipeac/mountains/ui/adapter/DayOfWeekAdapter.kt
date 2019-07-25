package com.rafaelfelipeac.mountains.ui.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rafaelfelipeac.mountains.R
import com.rafaelfelipeac.mountains.models.DayOfWeek
import com.rafaelfelipeac.mountains.models.Goal
import com.rafaelfelipeac.mountains.ui.base.BaseAdapter
import com.rafaelfelipeac.mountains.ui.fragments.today.TodayFragment
import com.rafaelfelipeac.mountains.ui.helper.SwipeAndDragHelperGoal

class DayOfWeekAdapter(fragment: TodayFragment) : BaseAdapter<DayOfWeek>() {

    private var goalsAdapter = GoalsRepetitionAdapter(fragment)

    var clickListener: (dayOfWeek: DayOfWeek) -> Unit = { }

    override fun getLayoutRes(): Int = R.layout.list_item_day_of_week

    override fun View.bindView(item: DayOfWeek, viewHolder: ViewHolder) {
        setOnClickListener { clickListener(item) }

        val title1 = viewHolder.itemView.findViewById<TextView>(R.id.day_of_week_title_1)
        val title2 = viewHolder.itemView.findViewById<TextView>(R.id.day_of_week_title_2)
        val list = viewHolder.itemView.findViewById<RecyclerView>(R.id.day_of_week_list)

        title1.text = item.title1
        title2.text = item.title2

        val fakeGoals = listOf(Goal(name = "fake goal 1"), Goal(name = "fake goal 2"), Goal(name = "fake goal 3"))

        fakeGoals.sortedBy { it.order }.let { goalsAdapter.setItems(it) }

        goalsAdapter.clickListener = { }

        val swipeAndDragHelper = SwipeAndDragHelperGoal(goalsAdapter)
        val touchHelper = ItemTouchHelper(swipeAndDragHelper)

        goalsAdapter.touchHelper = touchHelper

        list.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        list.adapter = goalsAdapter

        touchHelper.attachToRecyclerView(list)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)

        holder.setIsRecyclable(false)
    }
}