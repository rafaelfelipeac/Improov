package com.rafaelfelipeac.readmore.ui.adapter

import android.annotation.SuppressLint
import android.media.Image
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.View
import android.widget.ImageView
import com.rafaelfelipeac.readmore.R
import com.rafaelfelipeac.readmore.models.Goal
import com.rafaelfelipeac.readmore.ui.base.BaseAdapter
import com.rafaelfelipeac.readmore.ui.helper.SwipeAndDragHelper
import javax.inject.Inject

class BooksAdapter @Inject constructor() : BaseAdapter<Goal>(), SwipeAndDragHelper.ActionCompletionContract {
    var clickListener: (goal: Goal) -> Unit = { }
    private var touchHelper: ItemTouchHelper? = null

    override fun getLayoutRes(): Int = R.layout.list_item_book

    override fun View.bindView(item: Goal, viewHolder: ViewHolder) {
        setOnClickListener { clickListener(item) }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)

        val item = holder.itemView.findViewById<ImageView>(R.id.book_image_view)
            item.setOnTouchListener { _, _ ->
            touchHelper?.startDrag(holder)
            false
        }
    }

    override fun onViewMoved(oldPosition: Int, newPosition: Int) {
        val targetBook = this.items[oldPosition]
        val book = Goal(targetBook.message)

        this.items.removeAt(oldPosition)
        this.items.add(newPosition, book)

        notifyItemMoved(oldPosition, newPosition)
    }

    override fun onViewSwiped(position: Int) {
        this.items.removeAt(position)

        notifyItemRemoved(position)
    }

    fun setTouchHelper(touchHelper: ItemTouchHelper) {
        this.touchHelper = touchHelper
    }
}
