package com.rafaelfelipeac.domore.ui.adapter

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.rafaelfelipeac.domore.R
import com.rafaelfelipeac.domore.extension.getPercentage
import com.rafaelfelipeac.domore.models.Goal
import com.rafaelfelipeac.domore.ui.base.BaseAdapter
import com.rafaelfelipeac.domore.ui.fragments.goals.GoalsFragment
import com.rafaelfelipeac.domore.ui.helper.ActionCompletionContract

class GoalsAdapter(private val fragment: GoalsFragment) : BaseAdapter<Goal>(), ActionCompletionContract {

    var clickListener: (goal: Goal) -> Unit = { }

    override fun getLayoutRes(): Int = R.layout.list_item_goal

    override fun View.bindView(item: Goal, viewHolder: ViewHolder) {
        setOnClickListener { clickListener(item) }

        val title = viewHolder.itemView.findViewById<TextView>(R.id.goal_item_title)
        val image = viewHolder.itemView.findViewById<ImageView>(R.id.goal_progress)
        val percentage = viewHolder.itemView.findViewById<TextView>(R.id.goal_percentage)

        title.text = item.name

        if (item.done) {
            image.background = ContextCompat.getDrawable(context!!, R.mipmap.ic_item_done)
            percentage.text = ""
        }
        else {
            image.background = ContextCompat.getDrawable(context!!, R.mipmap.ic_item_undone)

            percentage.text = if (item.getPercentage() >= 100) {
                String.format("100.00%s", "%")
            } else {
                String.format("%.2f %s", item.getPercentage(), "%")
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
