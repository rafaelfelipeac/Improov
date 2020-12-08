package com.rafaelfelipeac.improov.features.goal.presentation.goaldetail

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.rafaelfelipeac.improov.R
import com.rafaelfelipeac.improov.features.commons.domain.model.Item
import com.rafaelfelipeac.improov.core.platform.base.BaseAdapter
import com.rafaelfelipeac.improov.core.platform.ActionCompleteContract

class ItemsAdapter(private val detailFragment: GoalDetailFragment) : BaseAdapter<Item>(),
    ActionCompleteContract {

    var clickListener: (item: Item) -> Unit = { }

    override fun getLayoutRes(): Int = R.layout.list_item_item

    override fun View.bindView(item: Item, viewHolder: ViewHolder) {
        setOnClickListener { clickListener(item) }

        val image = viewHolder.itemView.findViewById<ImageView>(R.id.itemItemProgress)
        val title = viewHolder.itemView.findViewById<TextView>(R.id.itemItemTitle)

        title.text = item.name
        image.background =
            if (item.done) {
                ContextCompat.getDrawable(context, R.mipmap.ic_item_done)
            } else {
                ContextCompat.getDrawable(context, R.mipmap.ic_item_undone)
            }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)

        holder.setIsRecyclable(false)

        val itemDrag = holder.itemView.findViewById<ImageView>(R.id.itemItemImageView)
            itemDrag.setOnTouchListener { _, _ ->
                touchHelper?.startDrag(holder)
            false
        }
    }

    override fun onViewMoved(fromPosition: Int, toPosition: Int) {
        detailFragment.onViewMoved(fromPosition, toPosition, items, ::notifyItemMoved)
    }

    override fun onViewSwiped(position: Int, direction: Int, holder: RecyclerView.ViewHolder) {
        detailFragment.onViewSwiped(position, direction, items)
    }

    fun updateItem(position: Int) {
        notifyItemRemoved(position)
        notifyItemInserted(position)
    }

    fun updateItem(item: Item) {
        items.find { it.itemId == item.itemId }?.name = item.name
        notifyDataSetChanged()
    }
}
