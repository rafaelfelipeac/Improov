package com.rafaelfelipeac.mountains.ui.adapter

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.rafaelfelipeac.mountains.R
import com.rafaelfelipeac.mountains.models.Item
import com.rafaelfelipeac.mountains.ui.base.BaseAdapter
import com.rafaelfelipeac.mountains.ui.fragments.goal.GoalFragment
import com.rafaelfelipeac.mountains.ui.helper.ActionCompletionContract

class ItemsAdapter(private val fragment: GoalFragment) : BaseAdapter<Item>(), ActionCompletionContract {

    var clickListener: (item: Item) -> Unit = { }

    override fun getLayoutRes(): Int = R.layout.list_item_item

    override fun View.bindView(item: Item, viewHolder: ViewHolder) {
        setOnClickListener { clickListener(item) }

        val image = viewHolder.itemView.findViewById<ImageView>(R.id.item_progress)
        val title = viewHolder.itemView.findViewById<TextView>(R.id.item_title)

        title.text = item.name
        image.background =
            if (item.done) ContextCompat.getDrawable(context!!, R.mipmap.ic_item_done)
            else           ContextCompat.getDrawable(context!!, R.mipmap.ic_item_undone)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)

        holder.setIsRecyclable(false)

        val itemDrag = holder.itemView.findViewById<ImageView>(R.id.item_image_view)
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
