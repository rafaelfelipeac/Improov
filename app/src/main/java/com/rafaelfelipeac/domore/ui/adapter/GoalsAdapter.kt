package com.rafaelfelipeac.domore.ui.adapter

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.rafaelfelipeac.domore.R
import com.rafaelfelipeac.domore.app.App
import com.rafaelfelipeac.domore.models.Goal
import com.rafaelfelipeac.domore.ui.base.BaseAdapter
import com.rafaelfelipeac.domore.ui.helper.ActionCompletionContract
import kotlinx.android.synthetic.main.list_item_goal.view.*
import javax.inject.Inject

class GoalsAdapter @Inject constructor() : BaseAdapter<Goal>(), ActionCompletionContract {

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

    override fun onViewSwiped(position: Int, direction: Int, holder: RecyclerView.ViewHolder) {
        val goal = this.items[position]

        this.items.removeAt(position)
        notifyItemRemoved(position)

        when(direction) {
            ItemTouchHelper.RIGHT -> { // done
                goal.done = true

                goalDAO?.update(goal)

                showSnackBarWithAction(holder.itemView, "Meta resolvida.", position, goal, ::doneGoal)
            }
            ItemTouchHelper.LEFT -> {  // delete
                goalDAO?.delete(goal)

                showSnackBarWithAction(holder.itemView, "Meta removida.", position, goal, ::deleteGoal)
            }
        }
    }

    private fun doneGoal(position: Int, goal: Goal) {
        this.items.add(position, goal)
        goal.done = false
        goalDAO?.update(goal)
        notifyItemInserted(position)
    }

    private fun deleteGoal(position: Int, goal: Goal) {
        this.items.add(position, goal)
        goalDAO?.insert(goal)
        notifyItemInserted(position)
    }

    fun setTouchHelper(touchHelper: ItemTouchHelper) {
        this.touchHelper = touchHelper
    }
}
