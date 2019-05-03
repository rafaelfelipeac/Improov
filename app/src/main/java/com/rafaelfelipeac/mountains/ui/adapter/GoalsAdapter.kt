package com.rafaelfelipeac.mountains.ui.adapter

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.rafaelfelipeac.mountains.R
import com.rafaelfelipeac.mountains.extension.getPercentage
import com.rafaelfelipeac.mountains.models.Goal
import com.rafaelfelipeac.mountains.ui.base.BaseAdapter
import com.rafaelfelipeac.mountains.ui.fragments.goals.GoalsFragment
import com.rafaelfelipeac.mountains.ui.helper.ActionCompletionContract

class GoalsAdapter(private val fragment: GoalsFragment) : BaseAdapter<Goal>(), ActionCompletionContract {

    var clickListener: (goalId: Long) -> Unit = { }

    override fun getLayoutRes(): Int = R.layout.list_item_goal

    override fun View.bindView(item: Goal, viewHolder: ViewHolder) {
        setOnClickListener { clickListener(item.goalId) }

        val title = viewHolder.itemView.findViewById<TextView>(R.id.goal_item_title)
        val image = viewHolder.itemView.findViewById<ImageView>(R.id.goal_progress)
        val percentage = viewHolder.itemView.findViewById<TextView>(R.id.goal_percentage)

        title.text = item.name

        if (item.done) {
            image.background = ContextCompat.getDrawable(context!!, R.mipmap.ic_item_done)
            percentage.text = context.getString(R.string.empty_string)
        }
        else {
            image.background = ContextCompat.getDrawable(context!!, R.mipmap.ic_item_undone)

            percentage.text = if (item.getPercentage() >= 100) {
                String.format(context.getString(R.string.percentage_100), context.getString(R.string.percentage_symbol))
            } else {
                String.format(context.getString(R.string.percentage_others), item.getPercentage(), context.getString(R.string.percentage_symbol))
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)

        holder.setIsRecyclable(false)

        val itemDrag = holder.itemView.findViewById<ImageView>(R.id.goal_image_view)

        itemDrag.setOnTouchListener { _, _ ->
            touchHelper?.startDrag(holder)
            false
        }
    }

    override fun onViewMoved(oldPosition: Int, newPosition: Int) {
        fragment.onViewMoved(oldPosition, newPosition, items, ::notifyItemMoved)
    }

    override fun onViewSwiped(position: Int, direction: Int, holder: RecyclerView.ViewHolder) {
        fragment.onViewSwiped(position, direction, holder, items)
    }
}
