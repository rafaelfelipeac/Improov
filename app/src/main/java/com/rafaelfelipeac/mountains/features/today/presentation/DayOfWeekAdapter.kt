package com.rafaelfelipeac.mountains.features.today.presentation

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rafaelfelipeac.mountains.R
import com.rafaelfelipeac.mountains.core.platform.base.BaseAdapter
import com.rafaelfelipeac.mountains.features.commons.presentation.ListAdapter
import com.rafaelfelipeac.mountains.features.commons.Goal
import com.rafaelfelipeac.mountains.features.commons.Habit
import com.rafaelfelipeac.mountains.features.today.DayOfWeek
import com.rafaelfelipeac.mountains.features.commons.presentation.SwipeAndDragHelperList

class DayOfWeekAdapter(val fragment: TodayFragment) : BaseAdapter<DayOfWeek>() {

    var clickListener: (dayOfWeek: DayOfWeek) -> Unit = { }

    override fun getLayoutRes(): Int = R.layout.list_item_day_of_week

    override fun View.bindView(item: DayOfWeek, viewHolder: ViewHolder) {
        setOnClickListener { clickListener(item) }

        val weekDay = viewHolder.itemView.findViewById<TextView>(R.id.day_of_week_week_day)
        val monthDay = viewHolder.itemView.findViewById<TextView>(R.id.day_of_week_month_day)
        val itemsList = viewHolder.itemView.findViewById<RecyclerView>(R.id.day_of_week_items_list)

        weekDay.text = item.weekDay
        monthDay.text = item.monthDay

        val list = item.list

        val listAdapter = ListAdapter(fragment)

        listAdapter.clickListener = {
            when (it) {
                is Goal -> {
                    val action =
                        TodayFragmentDirections.actionNavigationTodayToNavigationGoal(
                            it.goalId
                        )
                    fragment.navController.navigate(action)
                }
                is Habit -> {
                    val action =
                        TodayFragmentDirections.actionNavigationTodayToNavigationHabit(
                            it.habitId
                        )
                    fragment.navController.navigate(action)
                }
            }
        }

        list.sortedBy { it.order }.let { listAdapter.setItems(it) }

        val swipeAndDragHelper =
            SwipeAndDragHelperList(listAdapter)
        val touchHelper = ItemTouchHelper(swipeAndDragHelper)

        listAdapter.touchHelper = touchHelper

        itemsList.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        itemsList.adapter = listAdapter

        touchHelper.attachToRecyclerView(itemsList)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)

        holder.setIsRecyclable(false)
    }
}
