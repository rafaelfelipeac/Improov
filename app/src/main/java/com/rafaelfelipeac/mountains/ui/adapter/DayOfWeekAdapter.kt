package com.rafaelfelipeac.mountains.ui.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rafaelfelipeac.mountains.R
import com.rafaelfelipeac.mountains.models.DayOfWeek
import com.rafaelfelipeac.mountains.ui.base.BaseAdapter
import com.rafaelfelipeac.mountains.ui.fragments.today.TodayFragment
import com.rafaelfelipeac.mountains.ui.fragments.today.TodayFragmentDirections
import com.rafaelfelipeac.mountains.ui.helper.SwipeAndDragHelperGoal

class DayOfWeekAdapter(val fragment: TodayFragment) : BaseAdapter<DayOfWeek>() {

    var clickListener: (dayOfWeek: DayOfWeek) -> Unit = { }

    override fun getLayoutRes(): Int = R.layout.list_item_day_of_week

    override fun View.bindView(item: DayOfWeek, viewHolder: ViewHolder) {
        setOnClickListener { clickListener(item) }

        val title1 = viewHolder.itemView.findViewById<TextView>(R.id.day_of_week_title_1)
        val title2 = viewHolder.itemView.findViewById<TextView>(R.id.day_of_week_title_2)
        val list = viewHolder.itemView.findViewById<RecyclerView>(R.id.day_of_week_list)

        title1.text = item.title1
        title2.text = item.title2

        val goalsAdapter = GoalsRepetitionAdapter(fragment)

        goalsAdapter.clickListener = {
            val action = TodayFragmentDirections.actionNavigationTodayToNavigationGoal(it)
            fragment.navController.navigate(action)
        }

        val fakeGoals = item.list

        fakeGoals.sortedBy { it.order }.let { goalsAdapter.setItems(it) }

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