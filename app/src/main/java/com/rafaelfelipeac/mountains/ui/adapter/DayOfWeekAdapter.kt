package com.rafaelfelipeac.mountains.ui.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rafaelfelipeac.mountains.R
import com.rafaelfelipeac.mountains.models.DayOfWeek
import com.rafaelfelipeac.mountains.models.Repetition
import com.rafaelfelipeac.mountains.ui.base.BaseAdapter
import com.rafaelfelipeac.mountains.ui.fragments.today.TodayFragment
import com.rafaelfelipeac.mountains.ui.fragments.today.TodayFragmentDirections
import com.rafaelfelipeac.mountains.ui.helper.SwipeAndDragHelperGoal

class DayOfWeekAdapter(val fragment: TodayFragment) : BaseAdapter<DayOfWeek>() {

    var clickListener: (dayOfWeek: DayOfWeek) -> Unit = { }

    override fun getLayoutRes(): Int = R.layout.list_item_day_of_week

    override fun View.bindView(item: DayOfWeek, viewHolder: ViewHolder) {
        setOnClickListener { clickListener(item) }

        val weekDay = viewHolder.itemView.findViewById<TextView>(R.id.day_of_week_week_day)
        val monthDay = viewHolder.itemView.findViewById<TextView>(R.id.day_of_week_month_day)
        val list = viewHolder.itemView.findViewById<RecyclerView>(R.id.day_of_week_list)

        weekDay.text = item.weekDay
        monthDay.text = item.monthDay

        val repetitions = item.list

        val goalsAdapter = GoalsAdapter(fragment)

        goalsAdapter.clickListener = {
            val action =
                TodayFragmentDirections.actionNavigationTodayToNavigationRepetition((it as Repetition).repetitionId)
            fragment.navController.navigate(action)
        }

        repetitions.sortedBy { it.order }.let { goalsAdapter.setItems(it) }

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