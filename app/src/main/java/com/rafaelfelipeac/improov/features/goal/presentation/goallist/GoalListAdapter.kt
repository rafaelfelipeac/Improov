package com.rafaelfelipeac.improov.features.goal.presentation.goallist

import android.annotation.SuppressLint
import android.graphics.Paint
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewTreeObserver
import androidx.recyclerview.widget.RecyclerView
import com.rafaelfelipeac.improov.R
import com.rafaelfelipeac.improov.core.extension.visible
import com.rafaelfelipeac.improov.core.extension.getNumberInExhibitionFormat
import com.rafaelfelipeac.improov.core.extension.setWidthForProgress
import com.rafaelfelipeac.improov.core.extension.formatToDayMonth
import com.rafaelfelipeac.improov.core.platform.ActionCompleteContract
import com.rafaelfelipeac.improov.core.platform.base.BaseAdapter
import com.rafaelfelipeac.improov.databinding.ListItemGoalBinding
import com.rafaelfelipeac.improov.features.commons.domain.model.Goal

const val LOGICAL_DENSITY_MULTIPLIER = 4

class GoalListAdapter(private val fragment: GoalListFragment) : BaseAdapter<Goal>(), ActionCompleteContract {

    var clickListener: (goal: Goal) -> Unit = { }

    override fun getLayoutRes(): Int = R.layout.list_item_goal

    @SuppressLint("ClickableViewAccessibility")
    override fun View.bindView(item: Goal, viewHolder: ViewHolder) {
        val binding = ListItemGoalBinding.bind(this)

        setOnClickListener { clickListener(item) }

        binding.itemGoalTitle.text = item.name

        if (item.done) {
            binding.itemGoalDoneIcon.visible()

            binding.itemGoalTitle.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        }

        if (item.divideAndConquer) {
            binding.itemGoalScore.text = String.format(
                "%s/%s",
                item.value.getNumberInExhibitionFormat(),
                item.goldValue.getNumberInExhibitionFormat()
            )
        } else {
            binding.itemGoalScore.text = String.format(
                "%s/%s",
                item.value.getNumberInExhibitionFormat(),
                item.singleValue.getNumberInExhibitionFormat()
            )
        }

        binding.itemGoalProgressTotal.viewTreeObserver
            .addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {

            override fun onPreDraw(): Boolean {
                binding.itemGoalProgressTotal.viewTreeObserver.removeOnPreDrawListener(this)

                val metrics = DisplayMetrics()
                fragment.activity?.windowManager?.defaultDisplay?.getMetrics(metrics)
                val logicalDensity = metrics.density
                val margin = (logicalDensity * LOGICAL_DENSITY_MULTIPLIER).toInt()

                binding.itemGoalProgressDone
                    .setWidthForProgress(item, binding.itemGoalProgressTotal.measuredWidth - margin)

                return true
            }
        })

        if (item.date != null) {
            binding.itemGoalDate.text = item.date.formatToDayMonth(fragment.requireContext())
        } else {
            binding.itemGoalDate.text = ""
        }

        binding.itemGoalDragIcon.setOnTouchListener { _, _ ->
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
