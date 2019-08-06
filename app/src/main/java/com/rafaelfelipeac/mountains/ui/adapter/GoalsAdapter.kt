package com.rafaelfelipeac.mountains.ui.adapter

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
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
import kotlin.math.roundToInt

class GoalsAdapter(private val fragment: BaseFragment) : BaseAdapter<Goal>(), ActionCompletionContract {

    var clickListener: (goalId: Long, repetition: Boolean) -> Unit = { _: Long, _: Boolean -> }

    override fun getLayoutRes(): Int = R.layout.list_item_goal

    override fun View.bindView(item: Goal, viewHolder: ViewHolder) {
        setOnClickListener { clickListener(item.goalId, item.repetition) }

        val typeIcon = viewHolder.itemView.findViewById<ImageView>(R.id.goal_type_icon)

        val archiveImage = viewHolder.itemView.findViewById<ImageView>(R.id.goal_archive_image)

        if (fragment is TodayFragment) {
            archiveImage.gone()
        }

        if (item.repetition) {
            val repetitionType = viewHolder.itemView.findViewById<ConstraintLayout>(R.id.goal_type_repetition)
            val title = viewHolder.itemView.findViewById<TextView>(R.id.goal_type_repetition_title)
            val type = viewHolder.itemView.findViewById<TextView>(R.id.goal_type_repetition_type)
            val late = viewHolder.itemView.findViewById<TextView>(R.id.goal_type_repetition_late_date)
            val score = viewHolder.itemView.findViewById<TextView>(R.id.goal_type_repetition_score)

            repetitionType.visible()

            title.text = item.name

            if (item.repetitionNextDate != null && item.isLate() && !item.isToday()) {
                late.text = item.repetitionNextDate.format()
                late.visible()
            }

            typeIcon.layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT
            typeIcon.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
            typeIcon.background = ContextCompat.getDrawable(context!!, R.drawable.ic_repetition)

            when (item.repetitionType) {
                REP1 -> {
                    type.text = String.format(
                        "%s", "Todo dia"
                    )
                }
                REP2 -> {
                    type.text = String.format(
                        "%s %s",
                        item.repetitionWeekDaysLong.filter { it > 0 }.size.toString(),
                        "vezes por semana"
                    )
                }
                REP3 -> {
                    type.text = String.format(
                        "%s %s %s",
                        item.repetitionPeriodTotal.toString(),
                        "dias por",
                        item.repetitionPeriodType.getName()
                    )

                    score.text = String.format(
                        "%s/%s",
                        item.repetitionPeriodDone.toString(),
                        item.repetitionPeriodTotal.toString()
                    )
                    score.visible()
                }
                REP4 -> {
                    type.text = String.format(
                        "%s %s %s",
                        "A cada",
                        item.repetitionPeriodDaysBetween.toString(),
                        "dias"
                    )
                }
                REP_NONE -> TODO()
            }
        } else {
            val repetitionType = viewHolder.itemView.findViewById<ConstraintLayout>(R.id.goal_type_default)
            val title = viewHolder.itemView.findViewById<TextView>(R.id.goal_type_default_title)
            val type = viewHolder.itemView.findViewById<TextView>(R.id.goal_type_default_type)
            val date = viewHolder.itemView.findViewById<TextView>(R.id.goal_type_default_date)
            val score = viewHolder.itemView.findViewById<TextView>(R.id.goal_type_default_score)

            repetitionType.visible()

            title.text = item.name

            if (item.done) {
                typeIcon.background = ContextCompat.getDrawable(context!!, R.mipmap.ic_item_done)

                val params = typeIcon.layoutParams as ConstraintLayout.LayoutParams
                params.marginStart = 10
                typeIcon.layoutParams = params
            } else {
                typeIcon.layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT
                typeIcon.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT

                typeIcon.background = ContextCompat.getDrawable(context!!, R.drawable.ic_today)
            }

            score.text = String.format(
                "%s/%s",
                item.value.roundToInt().toString(),
                item.singleValue.roundToInt().toString()
            )

            date.text = "13 AGO"
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)

        holder.setIsRecyclable(false)

        val itemDrag = holder.itemView.findViewById<ImageView>(R.id.goal_drag_icon)

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

