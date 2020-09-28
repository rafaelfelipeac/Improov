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
import com.rafaelfelipeac.improov.core.extension.formatToDayMonth
import com.rafaelfelipeac.improov.core.platform.ActionCompleteContract
import com.rafaelfelipeac.improov.core.platform.base.BaseAdapter
import com.rafaelfelipeac.improov.features.commons.domain.model.Goal

const val MARGIN_START = 10
const val LOGICAL_DENSITY_MULTIPLIER = 4

class GoalListAdapter(private val fragment: GoalListFragment) : BaseAdapter<Goal>(), ActionCompleteContract {

    var clickListener: (goal: Goal) -> Unit = { }

    override fun getLayoutRes(): Int = R.layout.list_item_goal

    @SuppressLint("ClickableViewAccessibility")
    override fun View.bindView(item: Goal, viewHolder: ViewHolder) {
        setOnClickListener { clickListener(item) }

        val typeIcon = viewHolder.itemView.findViewById<ImageView>(R.id.itemGoalTypeIcon)
        val title = viewHolder.itemView.findViewById<TextView>(R.id.itemGoalTitle)!!
        val date = viewHolder.itemView.findViewById<TextView>(R.id.itemGoalDate)!!
        val score = viewHolder.itemView.findViewById<TextView>(R.id.itemGoalScore)!!
        val itemDrag = viewHolder.itemView.findViewById<ImageView>(R.id.itemGoalDragIcon)!!
        val progressDone = viewHolder.itemView.findViewById<ImageView>(R.id.itemGoalProgressDone)
        val progressTotal = viewHolder.itemView.findViewById<ImageView>(R.id.itemGoalProgressTotal)

        title.text = item.name

        if (item.done) {
            typeIcon.background = ContextCompat.getDrawable(fragment.requireContext(), R.mipmap.ic_item_done)

            val params = typeIcon.layoutParams as ConstraintLayout.LayoutParams
            params.marginStart = MARGIN_START
            typeIcon.layoutParams = params
        } else {
            typeIcon.background = ContextCompat.getDrawable(fragment.requireContext(), R.mipmap.ic_item_undone)

            val params = typeIcon.layoutParams as ConstraintLayout.LayoutParams
            params.marginStart = MARGIN_START
            typeIcon.layoutParams = params
        }

        if (item.divideAndConquer) {
            score.text = String.format(
                "%s/%s",
                item.value.getNumberInRightFormat(),
                item.goldValue.getNumberInRightFormat()
            )
        } else {
            score.text = String.format(
                "%s/%s",
                item.value.getNumberInRightFormat(),
                item.singleValue.getNumberInRightFormat()
            )
        }

        progressTotal.viewTreeObserver.addOnPreDrawListener(object :
            ViewTreeObserver.OnPreDrawListener {

            override fun onPreDraw(): Boolean {
                progressTotal.viewTreeObserver.removeOnPreDrawListener(this)

                val metrics = DisplayMetrics()
                fragment.activity?.windowManager?.defaultDisplay?.getMetrics(metrics)
                val logicalDensity = metrics.density
                val margin = (logicalDensity * LOGICAL_DENSITY_MULTIPLIER).toInt()

                progressDone.setWidthForProgress(item, progressTotal.measuredWidth - margin)

                return true
            }
        })

        if (item.date != null) {
            date.text = item.date.formatToDayMonth(fragment.requireContext())
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
        fragment.onViewSwiped(position, direction, items)
    }

    fun updateGoal(position: Int) {
        notifyItemRemoved(position)
        notifyItemInserted(position)
    }
}
