package com.rafaelfelipeac.readmore.ui.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.rafaelfelipeac.readmore.R
import com.rafaelfelipeac.readmore.models.Book
import com.rafaelfelipeac.readmore.models.Goal
import com.rafaelfelipeac.readmore.ui.base.BaseAdapter
import com.rafaelfelipeac.readmore.ui.helper.SwipeAndDragHelper
import javax.inject.Inject

class BooksAdapter @Inject constructor() : BaseAdapter<Book>(), SwipeAndDragHelper.ActionCompletionContract {

    var clickListener: (book: Book) -> Unit = { }
    private var touchHelper: ItemTouchHelper? = null

    override fun getLayoutRes(): Int = R.layout.list_item_book

    override fun View.bindView(item: Book, viewHolder: ViewHolder) {
        setOnClickListener { clickListener(item) }

        val title = viewHolder.itemView.findViewById<TextView>(R.id.book_title)
        title.text = item.title
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)

        holder.setIsRecyclable(false)

        val itemDrag = holder.itemView.findViewById<ImageView>(R.id.book_image_view)
            itemDrag.setOnTouchListener { _, _ ->
                touchHelper?.startDrag(holder)
            false
        }
    }

    override fun onViewMoved(oldPosition: Int, newPosition: Int) {
        val targetBook = this.items[oldPosition]
        val book = Book(targetBook.title, "", "")

        this.items.removeAt(oldPosition)
        this.items.add(newPosition, book)

        notifyItemMoved(oldPosition, newPosition)
    }

    override fun onViewSwiped(position: Int, holder: RecyclerView.ViewHolder) {
        val item = this.items[position]

        this.items.removeAt(position)

        notifyItemRemoved(position)

        Snackbar
            .make(holder.itemView, "Deletado.", Snackbar.LENGTH_LONG)
            .setAction("UNDO") {
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
