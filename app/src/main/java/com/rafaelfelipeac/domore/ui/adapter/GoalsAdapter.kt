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
import com.rafaelfelipeac.domore.ui.activities.MainActivity
import com.rafaelfelipeac.domore.ui.base.BaseAdapter
import com.rafaelfelipeac.domore.ui.fragments.goals.GoalsFragment
import com.rafaelfelipeac.domore.ui.helper.ActionCompletionContract

class GoalsAdapter(private val fragment: Fragment) : BaseAdapter<Goal>(), ActionCompletionContract {

    var clickListener: (goal: Goal) -> Unit = { }

    override fun getLayoutRes(): Int = R.layout.list_item_goal

    override fun View.bindView(goal: Goal, viewHolder: ViewHolder) {
        setOnClickListener { clickListener(goal) }

        val title = viewHolder.itemView.findViewById<TextView>(R.id.goal_item_title)
        val image = viewHolder.itemView.findViewById<ImageView>(R.id.goal_progress)

        title.text = goal.name

        if (goal.done)
            image.background = ContextCompat.getDrawable(context!!, R.mipmap.ic_item_done)
        else
            image.background = ContextCompat.getDrawable(context!!, R.mipmap.ic_item_undone)
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

        when(direction) {
            ItemTouchHelper.RIGHT -> {
                if (!goal.done) {
                    (fragment as GoalsFragment).setItems()

                    (fragment.activity as MainActivity).openBottomSheetDoneGoal(goal, ::doneGoal)
                } else {
                    goal.done = false
                    goal.undoneDate = getCurrentTime()

                    goalDAO?.update(goal)

                    (fragment as GoalsFragment).setItems()
                }
            }
            ItemTouchHelper.LEFT -> {
                goal.deleteDate = getCurrentTime()

                goalDAO?.delete(goal)

                showSnackBarWithAction(holder.itemView, "Meta removida.", goal, ::deleteGoal)

                (fragment as GoalsFragment).setItems()
            }
        }
    }

    private fun doneGoal(goal: Goal, done: Boolean) {
        goal.done = done
        goal.undoneDate = getCurrentTime()

        goalDAO?.update(goal)

        (fragment as GoalsFragment).setItems()
    }

    private fun deleteGoal(goal: Goal) {
        goalDAO?.insert(goal)
        (fragment as GoalsFragment).setItems()
    }
}
