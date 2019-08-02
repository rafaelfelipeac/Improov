package com.rafaelfelipeac.mountains.ui.adapter

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.rafaelfelipeac.mountains.R
import com.rafaelfelipeac.mountains.extension.*
import com.rafaelfelipeac.mountains.models.Goal
import com.rafaelfelipeac.mountains.models.RepetitionType.*
import com.rafaelfelipeac.mountains.ui.base.BaseAdapter
import com.rafaelfelipeac.mountains.ui.base.BaseFragment
import com.rafaelfelipeac.mountains.ui.fragments.goals.GoalsFragment
import com.rafaelfelipeac.mountains.ui.fragments.today.TodayFragment
import com.rafaelfelipeac.mountains.ui.helper.ActionCompletionContract

class GoalsAdapter(private val fragment: BaseFragment) : BaseAdapter<Goal>(), ActionCompletionContract {

    var clickListener: (goalId: Long) -> Unit = { }

    override fun getLayoutRes(): Int = R.layout.list_item_goal

    override fun View.bindView(item: Goal, viewHolder: ViewHolder) {
        setOnClickListener { clickListener(item.goalId) }

        val title = viewHolder.itemView.findViewById<TextView>(R.id.goal_item_title)
        val image = viewHolder.itemView.findViewById<ImageView>(R.id.goal_progress)
        val percentage = viewHolder.itemView.findViewById<TextView>(R.id.goal_percentage)
        val goalRepetition = viewHolder.itemView.findViewById<TextView>(R.id.goal_repetition_type)
        val goalDrag = viewHolder.itemView.findViewById<ImageView>(R.id.goal_image_drag)
        val archiveImage = viewHolder.itemView.findViewById<ImageView>(R.id.goal_archive_image)
        val late = viewHolder.itemView.findViewById<TextView>(R.id.goal_repetition_late_date)
        val score = viewHolder.itemView.findViewById<TextView>(R.id.goal_repetition_score)

        title.text = item.name

        if (item.done) {
            image.background = ContextCompat.getDrawable(context!!, R.mipmap.ic_item_done)
            percentage.gone()
        }
        else {
            image.background = ContextCompat.getDrawable(context!!, R.mipmap.ic_item_undone)

            percentage.text = if (item.getPercentage() >= 100) {
                String.format(context.getString(R.string.percentage_100), context.getString(R.string.percentage_symbol))
            } else {
                String.format(context.getString(R.string.percentage_others), item.getPercentage(), context.getString(R.string.percentage_symbol))
            }
        }

        if (fragment is TodayFragment) {
            goalDrag.gone()
            archiveImage.gone()
        }

        if (item.repetition) {
            percentage.gone()

            if (item.repetitionNextDate != null && item.isLate() && !item.isToday()) {
                late.text = item.repetitionNextDate.format()
                late.visible()
            }

            image.layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT
            image.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT

            image.background = ContextCompat.getDrawable(context!!, R.drawable.ic_repetition)

            goalRepetition.visible()

            when (item.repetitionType) {
                REP1 -> {
                    goalRepetition.text = String.format("%s %s.",
                        context.getString(R.string.goals_adapter_item_repetition),
                        "Todo dia")
                }
                REP2 -> {
                    goalRepetition.text = String.format("%s %s %s.",
                        context.getString(R.string.goals_adapter_item_repetition),
                        item.repetitionWeekDaysLong.filter { it > 0 }.size.toString(),
                        "vezes por semana")
                }
                REP3 -> {
                    goalRepetition.text = String.format("%s %s %s %s.",
                        context.getString(R.string.goals_adapter_item_repetition),
                        item.repetitionPeriodTotal.toString(),
                        "dias por",
                        item.repetitionPeriodType.getName())

                    score.text = String.format("%s/%s",
                        item.repetitionPeriodDone.toString(),
                        item.repetitionPeriodTotal.toString())
                    score.visible()
                }
                REP4 -> {
                    goalRepetition.text = String.format("%s %s %s %s.",
                        context.getString(R.string.goals_adapter_item_repetition),
                        "A cada",
                        item.repetitionPeriodDaysBetween.toString(),
                        "dias")
                }
                REP_NONE -> TODO()
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)

        holder.setIsRecyclable(false)

        val itemDrag = holder.itemView.findViewById<ImageView>(R.id.goal_image_drag)

        itemDrag.setOnTouchListener { _, _ ->
            touchHelper?.startDrag(holder)
            false
        }
    }

    override fun onViewMoved(oldPosition: Int, newPosition: Int) {
        if (fragment is GoalsFragment) {
            fragment.onViewMoved(oldPosition, newPosition, items, ::notifyItemMoved)
        }
    }

    override fun onViewSwiped(position: Int, direction: Int, holder: RecyclerView.ViewHolder) {
        if (fragment is GoalsFragment) {
            fragment.onViewSwiped(position, direction, holder, items)
        } else if (fragment is TodayFragment) {
            fragment.onViewSwiped(position, direction, holder, items)
        }
    }
}

