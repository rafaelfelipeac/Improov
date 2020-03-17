package com.rafaelfelipeac.improov.features.goal.presentation.goal

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.rafaelfelipeac.improov.R
import com.rafaelfelipeac.improov.features.goal.Item
import com.rafaelfelipeac.improov.core.platform.base.BaseAdapter
import com.rafaelfelipeac.improov.core.platform.ActionCompletionContract

class ItemsAdapter(private val fragment: GoalFragment) : BaseAdapter<Item>(),
    ActionCompletionContract {

    var clickListener: (item: Item) -> Unit = { }

    override fun getLayoutRes(): Int = R.layout.list_item_item

    override fun View.bindView(item: Item, viewHolder: ViewHolder) {
        setOnClickListener { clickListener(item) }

        val image = viewHolder.itemView.findViewById<ImageView>(R.id.item_progress)
        val title = viewHolder.itemView.findViewById<TextView>(R.id.bottom_sheet_item_title)

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

    override fun onViewMoved(fromPosition: Int, toPosition: Int) {
        fragment.onViewMoved(fromPosition, toPosition, items, ::notifyItemMoved)
    }

    override fun onViewSwiped(position: Int, direction: Int, holder: RecyclerView.ViewHolder) {
        fragment.onViewSwiped(position, direction, holder, items)
    }
}
