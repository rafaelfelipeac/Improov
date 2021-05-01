package com.rafaelfelipeac.improov.features.goal.presentation.goaldetail

import android.annotation.SuppressLint
import android.graphics.Paint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.rafaelfelipeac.improov.R
import com.rafaelfelipeac.improov.core.extension.visible
import com.rafaelfelipeac.improov.features.commons.domain.model.Item
import com.rafaelfelipeac.improov.core.platform.base.BaseAdapter
import com.rafaelfelipeac.improov.core.platform.ActionCompleteContract
import com.rafaelfelipeac.improov.databinding.ListItemItemBinding

class ItemsAdapter(private val detailFragment: GoalDetailFragment) : BaseAdapter<Item>(),
    ActionCompleteContract {

    var clickListener: (item: Item) -> Unit = { }

    override fun getLayoutRes(): Int = R.layout.list_item_item

    @SuppressLint("ClickableViewAccessibility")
    override fun View.bindView(item: Item, viewHolder: ViewHolder) {
        val binding = ListItemItemBinding.bind(this)

        setOnClickListener { clickListener(item) }

        binding.itemItemTitle.text = item.name

        if (item.done) {
            binding.itemItemDoneIcon.visible()

            binding.itemItemTitle.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        }

        binding.itemItemImageView.setOnTouchListener { _, _ ->
            touchHelper?.startDrag(viewHolder)
            false
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)

        holder.setIsRecyclable(false)
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
