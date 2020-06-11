package com.rafaelfelipeac.improov.features.goal.presentation.goallist

import android.annotation.SuppressLint
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewTreeObserver
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.rafaelfelipeac.improov.R
import com.rafaelfelipeac.improov.core.extension.getNumberInRightFormat
import com.rafaelfelipeac.improov.core.extension.setWidthForProgress
import com.rafaelfelipeac.improov.core.extension.format
import com.rafaelfelipeac.improov.core.platform.ActionCompleteContract
import com.rafaelfelipeac.improov.core.platform.base.BaseAdapter
import com.rafaelfelipeac.improov.features.goal.domain.model.Goal

class GoalListAdapter(private val fragment: GoalListFragment) : BaseAdapter<Goal>(), ActionCompleteContract {

    var clickListener: (goal: Goal) -> Unit = { }

    override fun getLayoutRes(): Int = R.layout.list_item_goal

    @SuppressLint("ClickableViewAccessibility")
    override fun View.bindView(goal: Goal, viewHolder: ViewHolder) {
        setOnClickListener { clickListener(goal) }

        val typeIcon = viewHolder.itemView.findViewById<ImageView>(R.id.goal_type_icon)
        val title = viewHolder.itemView.findViewById<TextView>(R.id.goal_title)!!
        val date = viewHolder.itemView.findViewById<TextView>(R.id.goal_date)!!
        val score = viewHolder.itemView.findViewById<TextView>(R.id.goal_score)!!
        // private val archiveImage = itemView.findViewById<ImageView>(R.id.goal_archive_image)
        val itemDrag = viewHolder.itemView.findViewById<ImageView>(R.id.goal_drag_icon)!!
        val progressDone = viewHolder.itemView.findViewById<ImageView>(R.id.goal_progress_done)
        val progressTotal = viewHolder.itemView.findViewById<ImageView>(R.id.goal_progress_total)

        title.text = goal.name

        if (goal.done) {
            typeIcon.background = ContextCompat.getDrawable(fragment.context!!, R.mipmap.ic_item_done)

            val params = typeIcon.layoutParams as ConstraintLayout.LayoutParams
            params.marginStart = 10
            typeIcon.layoutParams = params
        } else {
            typeIcon.background = ContextCompat.getDrawable(fragment.context!!, R.mipmap.ic_item_undone)

            val params = typeIcon.layoutParams as ConstraintLayout.LayoutParams
            params.marginStart = 10
            typeIcon.layoutParams = params
        }

        if (goal.divideAndConquer) {
            score.text = String.format(
                "%s/%s",
                goal.value.getNumberInRightFormat(),
                goal.goldValue.getNumberInRightFormat()
            )
        } else {
            score.text = String.format(
                "%s/%s",
                goal.value.getNumberInRightFormat(),
                goal.singleValue.getNumberInRightFormat()
            )
        }

        progressTotal.viewTreeObserver.addOnPreDrawListener(object :
            ViewTreeObserver.OnPreDrawListener {

            override fun onPreDraw(): Boolean {
                progressTotal.viewTreeObserver.removeOnPreDrawListener(this)

                val metrics = DisplayMetrics()
                fragment.activity?.windowManager?.defaultDisplay?.getMetrics(metrics)
                val logicalDensity = metrics.density
                val margin = (logicalDensity * 4).toInt()

                progressDone.setWidthForProgress(goal, progressTotal.measuredWidth - margin)

                return true
            }
        })

        if (goal.date != null) {
            date.text = goal.date.format(fragment.context!!)
        } else {
            date.text = ""
        }

        itemDrag.setOnTouchListener { _, _ ->
            touchHelper?.startDrag(viewHolder)
            false
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)

        holder.setIsRecyclable(false)
    }

    override fun onViewMoved(fromPosition: Int, toPosition: Int) {
        fragment.onViewMoved(fromPosition, toPosition, items, ::notifyItemMoved)
    }

    override fun onViewSwiped(position: Int, direction: Int, holder: RecyclerView.ViewHolder) {
        fragment.onViewSwiped(position, direction, holder, items)
    }
}
