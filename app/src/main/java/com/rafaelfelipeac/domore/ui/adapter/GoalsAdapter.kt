package com.rafaelfelipeac.domore.ui.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.support.design.widget.Snackbar
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.View
import android.widget.ImageView
import com.rafaelfelipeac.domore.R
import com.rafaelfelipeac.domore.app.App
import com.rafaelfelipeac.domore.models.Goal
import com.rafaelfelipeac.domore.ui.base.BaseAdapter
import com.rafaelfelipeac.domore.ui.helper.SwipeAndDragHelperGoal
import kotlinx.android.synthetic.main.list_item_goal.view.*
import javax.inject.Inject

class GoalsAdapter @Inject constructor() : BaseAdapter<Goal>(), SwipeAndDragHelperGoal.ActionCompletionContract {

    var clickListener: (goal: Goal) -> Unit = { }
    private var touchHelper: ItemTouchHelper? = null

    override fun getLayoutRes(): Int = R.layout.list_item_goal

    override fun View.bindView(item: Goal, viewHolder: ViewHolder) {
        setOnClickListener { clickListener(item) }

        goal_item_title.text = item.name
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
        val targetGoal = this.items[oldPosition]
        val otherGoal = this.items[newPosition]

        targetGoal.order = newPosition
        otherGoal.order = oldPosition

        App.database?.goalDAO()?.update(targetGoal)
        App.database?.goalDAO()?.update(otherGoal)

        this.items.removeAt(oldPosition)
        this.items.add(newPosition, targetGoal)

        notifyItemMoved(oldPosition, newPosition)
    }

    override fun onViewSwiped(position: Int, holder: RecyclerView.ViewHolder) {
        val book = this.items[position]

        this.items.removeAt(position)

        notifyItemRemoved(position)

        Snackbar
            .make(holder.itemView, "Excluído.", Snackbar.LENGTH_LONG)
            .setAction("DESFAZER") {
                this.items.add(position, book)

                notifyItemInserted(position)
            }
            .setActionTextColor(Color.WHITE)
            .show()
    }

    fun setTouchHelper(touchHelper: ItemTouchHelper) {
        this.touchHelper = touchHelper
    }
}
