package com.rafaelfelipeac.mountains.ui.adapter

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.rafaelfelipeac.mountains.R
import com.rafaelfelipeac.mountains.extension.*
import com.rafaelfelipeac.mountains.models.Goal
import com.rafaelfelipeac.mountains.ui.base.BaseAdapter
import com.rafaelfelipeac.mountains.ui.fragments.today.TodayFragment
import com.rafaelfelipeac.mountains.ui.helper.ActionCompletionContract

class GoalsRepetitionAdapter(private val fragment: TodayFragment) : BaseAdapter<Goal>(), ActionCompletionContract {

    var clickListener: (goalId: Long) -> Unit = { }

    override fun getLayoutRes(): Int = R.layout.list_item_goal_repetition

    override fun View.bindView(item: Goal, viewHolder: ViewHolder) {
        setOnClickListener { clickListener(item.goalId) }

        val title = viewHolder.itemView.findViewById<TextView>(R.id.goal_repetition_item_title)
        val image = viewHolder.itemView.findViewById<ImageView>(R.id.goal_repetition_progress)
        val lateDate = viewHolder.itemView.findViewById<TextView>(R.id.goal_repetition_late_date)

        title.text = item.name

        image.layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT
        image.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
        image.background = ContextCompat.getDrawable(context!!, R.drawable.ic_repetition)

        if (item.repetitionNextDate != null && item.isLate() && !item.isToday()) {
            lateDate.visible()
            lateDate.text = item.repetitionNextDate.format()
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)

        holder.setIsRecyclable(false)
    }

    override fun onViewMoved(oldPosition: Int, newPosition: Int) { }

    override fun onViewSwiped(position: Int, direction: Int, holder: RecyclerView.ViewHolder) {
        fragment.onViewSwiped(position, direction, holder, items)
    }
}
