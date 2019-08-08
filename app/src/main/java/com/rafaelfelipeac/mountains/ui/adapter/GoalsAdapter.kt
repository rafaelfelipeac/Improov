package com.rafaelfelipeac.mountains.ui.adapter

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.rafaelfelipeac.mountains.R
import com.rafaelfelipeac.mountains.extension.*
import com.rafaelfelipeac.mountains.models.*
import com.rafaelfelipeac.mountains.ui.base.BaseFragment
import com.rafaelfelipeac.mountains.ui.base.inflate
import com.rafaelfelipeac.mountains.ui.fragments.goals.GoalsFragment
import com.rafaelfelipeac.mountains.ui.fragments.today.TodayFragment
import com.rafaelfelipeac.mountains.ui.helper.ActionCompletionContract
import kotlin.math.roundToInt

class GoalsAdapter(val fragment: BaseFragment) : RecyclerView.Adapter<RecyclerView.ViewHolder>(),
    ActionCompletionContract {

    private lateinit var goals: List<GoalAbstract>

    var clickListener: (goalAbstract: GoalAbstract) -> Unit = { }

    lateinit var touchHelper: ItemTouchHelper

    companion object {
        const val TYPE_GOAL = 1
        const val TYPE_REPETITION = 2
    }

    override fun getItemViewType(position: Int): Int {
        return when (goals[position]) {
            is Goal -> TYPE_GOAL
            is Repetition -> TYPE_REPETITION
            else -> -1
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_GOAL -> {
                GoalViewHolder(parent.inflate(R.layout.list_item_goal), fragment, touchHelper, clickListener)
            }
            TYPE_REPETITION -> {
                RepetitionViewHolder(parent.inflate(R.layout.list_item_repetition), fragment, touchHelper, clickListener)
            }
            else -> {
                GoalViewHolder(parent.inflate(R.layout.list_item_goal), fragment, touchHelper, clickListener)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.setIsRecyclable(false)

        (holder as GoalAbstractViewHolder).bindViews(goals[position])
    }

    override fun getItemCount() = goals.size

    fun setItems(goals: List<GoalAbstract>) {
        this.goals = goals
        notifyDataSetChanged()
    }

    class GoalViewHolder(itemView: View,
                         val fragment: BaseFragment,
                         private val touchHelper: ItemTouchHelper?,
                         private val clickListener: (goalAbstract: GoalAbstract) -> Unit) :
        RecyclerView.ViewHolder(itemView), GoalAbstractViewHolder {

        private val typeIcon= itemView.findViewById<ImageView>(R.id.goal_type_icon)
        private val title = itemView.findViewById<TextView>(R.id.goal_title)!!
        private val date = itemView.findViewById<TextView>(R.id.goal_date)!!
        private val score = itemView.findViewById<TextView>(R.id.goal_score)!!
        private val itemDrag = itemView.findViewById<ImageView>(R.id.goal_drag_icon)!!

        @SuppressLint("ClickableViewAccessibility")
        override fun bindViews(goalAbstract: GoalAbstract) {

            itemView.setOnClickListener { clickListener(goalAbstract) }

            val goal = goalAbstract as Goal

            title.text = goal.name

            if (goal.done) {
                typeIcon.background = ContextCompat.getDrawable(fragment.context!!, R.mipmap.ic_item_done)

                val params = typeIcon.layoutParams as ConstraintLayout.LayoutParams
                params.marginStart = 10
                typeIcon.layoutParams = params
            } else {
                typeIcon.layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT
                typeIcon.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT

                typeIcon.background = ContextCompat.getDrawable(fragment.context!!, R.drawable.ic_today)
            }

            score.text = String.format(
                "%s/%s",
                goal.value.roundToInt().toString(),
                goal.singleValue.roundToInt().toString()
            )

            date.text = "13 AGO"

            itemDrag.setOnTouchListener { _, _ ->
                touchHelper?.startDrag(this)
                false
            }
        }
    }

    class RepetitionViewHolder(itemView: View,
                               val fragment: BaseFragment,
                               private val touchHelper: ItemTouchHelper?,
                               val clickListener: (goalAbstract: GoalAbstract) -> Unit) :
        RecyclerView.ViewHolder(itemView), GoalAbstractViewHolder {

        private val typeIcon = itemView.findViewById<ImageView>(R.id.repetition_type_icon)!!
        private val title = itemView.findViewById<TextView>(R.id.repetition_title)
        private val type = itemView.findViewById<TextView>(R.id.repetition_type)
        private val late = itemView.findViewById<TextView>(R.id.repetition_late_date)
        private val score = itemView.findViewById<TextView>(R.id.repetition_score)
        private val archiveImage = itemView.findViewById<ImageView>(R.id.repetition_archive_image)
        private val itemDrag = itemView.findViewById<ImageView>(R.id.repetition_drag_icon)

        @SuppressLint("ClickableViewAccessibility")
        override fun bindViews(goalAbstract: GoalAbstract) {

            if (fragment is TodayFragment) {
                archiveImage.gone()
                itemDrag.gone()
            }

            itemView.setOnClickListener { clickListener(goalAbstract) }

            val repetition = goalAbstract as Repetition

            title.text = repetition.name

            if (repetition.nextDate != null && repetition.isLate() && !repetition.isToday()) {
                late.text = repetition.nextDate.format()
                late.visible()
            }

            typeIcon.layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT
            typeIcon.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
            typeIcon.background = ContextCompat.getDrawable(fragment.context!!, R.drawable.ic_repetition)

            when (repetition.type) {
                RepetitionType.REP1 -> {
                    type.text = String.format(
                        "%s", "Todo dia"
                    )
                }
                RepetitionType.REP2 -> {
                    type.text = String.format(
                        "%s %s",
                        repetition.weekDaysLong.filter { it > 0 }.size.toString(),
                        "vezes por semana"
                    )
                }
                RepetitionType.REP3 -> {
                    type.text = String.format(
                        "%s %s %s",
                        repetition.periodTotal.toString(),
                        "dias por",
                        repetition.periodType.getName()
                    )

                    score.text = String.format(
                        "%s/%s",
                        repetition.periodDone.toString(),
                        repetition.periodTotal.toString()
                    )
                    score.visible()
                }
                RepetitionType.REP4 -> {
                    type.text = String.format(
                        "%s %s %s",
                        "A cada",
                        repetition.periodDaysBetween.toString(),
                        "dias"
                    )
                }
                RepetitionType.REP_NONE -> TODO()
            }

            itemDrag.setOnTouchListener { _, _ ->
                touchHelper?.startDrag(this)
                false
            }
        }
    }

    override fun onViewMoved(oldPosition: Int, newPosition: Int) {
        when (fragment) {
            is GoalsFragment -> fragment.onViewMoved(oldPosition, newPosition, goals, ::notifyItemMoved)
        }
    }

    override fun onViewSwiped(position: Int, direction: Int, holder: RecyclerView.ViewHolder) {
        when (fragment) {
            is GoalsFragment -> fragment.onViewSwiped(position, direction, holder, goals)
            is TodayFragment -> fragment.onViewSwiped(position, direction, holder, goals)
        }
    }

    interface GoalAbstractViewHolder {
        fun bindViews(goalAbstract: GoalAbstract)
    }
}
