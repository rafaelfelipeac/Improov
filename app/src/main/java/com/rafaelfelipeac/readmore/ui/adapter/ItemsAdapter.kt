package com.rafaelfelipeac.readmore.ui.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.support.design.widget.Snackbar
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.rafaelfelipeac.readmore.R
import com.rafaelfelipeac.readmore.models.Item
import com.rafaelfelipeac.readmore.ui.base.BaseAdapter
import com.rafaelfelipeac.readmore.ui.helper.SwipeAndDragHelperItem
import javax.inject.Inject

class ItemsAdapter @Inject constructor() : BaseAdapter<Item>(), SwipeAndDragHelperItem.ActionCompletionContract {

    var clickListener: (book: Item) -> Unit = { }
    private var touchHelper: ItemTouchHelper? = null

    override fun getLayoutRes(): Int = R.layout.list_item_item

    override fun View.bindView(item: Item, viewHolder: ViewHolder) {
        setOnClickListener { clickListener(item) }

        val title = viewHolder.itemView.findViewById<TextView>(R.id.item_title)
        title.text = item.title
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
        val targetBook = this.items[oldPosition]
        //val book = Item(1, 1, targetBook.title, "", "")

        this.items.removeAt(oldPosition)
        this.items.add(newPosition, targetBook)

        notifyItemMoved(oldPosition, newPosition)
    }

    override fun onViewSwiped(position: Int, holder: RecyclerView.ViewHolder) {
        val item = this.items[position]

        this.items.removeAt(position)

        notifyItemRemoved(position)

        Snackbar
            .make(holder.itemView, "Excluído.", Snackbar.LENGTH_LONG)
            .setAction("DESFAZER") {
                this.items.add(position, item)

                notifyItemInserted(position)
            }
            .setActionTextColor(Color.WHITE)
            .show()
    }

    fun setTouchHelper(touchHelper: ItemTouchHelper) {
        this.touchHelper = touchHelper
    }
}
