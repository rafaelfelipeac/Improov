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
import com.rafaelfelipeac.domore.ui.base.BaseAdapter
import com.rafaelfelipeac.domore.ui.fragments.goals.GoalsFragment
import com.rafaelfelipeac.domore.ui.helper.ActionCompletionContract
import kotlinx.android.synthetic.main.list_item_goal.view.*
import org.w3c.dom.Text

class GoalsAdapter(private val fragment: Fragment) : BaseAdapter<Goal>(), ActionCompletionContract {

    var clickListener: (goal: Goal) -> Unit = { }
    private var touchHelper: ItemTouchHelper? = null

    override fun getLayoutRes(): Int = R.layout.list_item_goal

    override fun View.bindView(goal: Goal, viewHolder: ViewHolder) {
        setOnClickListener { clickListener(goal) }

        val title = viewHolder.itemView.findViewById<TextView>(R.id.goal_item_title)
        val image = viewHolder.itemView.findViewById<ImageView>(R.id.goal_progress)

        if (goal.done)
            title.text = goal.name + "feito"
        else
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
                // done/undone

                if (!goal.done) {
                    // done

                    goal.done = true
                    goal.doneDate = getCurrentTime()

                    goalDAO?.update(goal)

                    //showSnackBarWithActionGoal(holder.itemView, "Meta - Done.", position, goal, ::doneGoal)

                    (fragment as GoalsFragment).setItems()
                } else {
                    // undone

                    goal.done = false
                    goal.undoneDate = getCurrentTime()

                    goalDAO?.update(goal)

                    //showSnackBarWithActionGoal(holder.itemView, "Meta - Undone.", position, goal, ::doneGoal)

                    (fragment as GoalsFragment).setItems()
                }
            }
            ItemTouchHelper.LEFT -> {
                // delete

                goal.deleteDate = getCurrentTime()

                goalDAO?.delete(goal)

                showSnackBarWithActionGoal(holder.itemView, "Meta removida.", position, goal, ::deleteGoal)
            }
        }
    }

//    private fun doneGoal(position: Int, goal: Goal) {
//        this.items.add(position, goal)
//        goal.done = false
//        goalDAO?.update(goal)
//        notifyItemInserted(position)
//    }

    private fun deleteGoal(position: Int, goal: Goal) {
        this.items.add(position, goal)
        goalDAO?.insert(goal)
        notifyItemInserted(position)
    }

    fun setTouchHelper(touchHelper: ItemTouchHelper) {
        this.touchHelper = touchHelper
    }
}
