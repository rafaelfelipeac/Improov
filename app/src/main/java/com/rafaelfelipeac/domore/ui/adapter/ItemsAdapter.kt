package com.rafaelfelipeac.domore.ui.adapter

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.rafaelfelipeac.domore.R
import com.rafaelfelipeac.domore.app.App
import com.rafaelfelipeac.domore.models.Goal
import com.rafaelfelipeac.domore.models.Item
import com.rafaelfelipeac.domore.ui.base.BaseAdapter
import com.rafaelfelipeac.domore.ui.fragments.goal.GoalFragment
import com.rafaelfelipeac.domore.ui.helper.ActionCompletionContract
import kotlinx.android.synthetic.main.list_item_goal.view.*

class ItemsAdapter(private val fragment: Fragment) : BaseAdapter<Item>(), ActionCompletionContract {

    var clickListener: (book: Item) -> Unit = { }

    override fun getLayoutRes(): Int = R.layout.list_item_item

    override fun View.bindView(item: Item, viewHolder: ViewHolder) {
        setOnClickListener { clickListener(item) }

        val image = viewHolder.itemView.findViewById<ImageView>(R.id.item_progress)
        val title = viewHolder.itemView.findViewById<TextView>(R.id.item_title)

        title.text = item.title
        image.background = if (item.done) ContextCompat.getDrawable(context!!, R.mipmap.ic_item_done)
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
        val targetItem = this.items[oldPosition]
        val otherItem = this.items[newPosition]

        targetItem.order = newPosition
        otherItem.order = oldPosition

        App.database?.itemDAO()?.update(targetItem)
        App.database?.itemDAO()?.update(otherItem)

        this.items.removeAt(oldPosition)
        this.items.add(newPosition, targetItem)

        notifyItemMoved(oldPosition, newPosition)
    }

    override fun onViewSwiped(position: Int, direction: Int, holder: RecyclerView.ViewHolder) {
        val item = this.items[position]

        when(direction) {
            ItemTouchHelper.RIGHT -> {
                if (!item.done) {
                    item.done = true
                    item.doneDate = getCurrentTime()

                    itemDAO?.update(item)

                    (fragment as GoalFragment).scoreFromList(true)
                } else {
                    item.done = false
                    item.undoneDate = getCurrentTime()

                    itemDAO?.update(item)

                    (fragment as GoalFragment).scoreFromList(false)
                }
            }
            ItemTouchHelper.LEFT -> {
                item.deleteDate = getCurrentTime()

                itemDAO?.delete(item)

                showSnackBarWithAction(holder.itemView, "Item removido.", item, ::deleteItem)

                (fragment as GoalFragment).setItems()
            }
        }
    }

    private fun deleteItem(item: Item) {
        itemDAO?.insert(item)
        (fragment as GoalFragment).setItems()
    }
}
