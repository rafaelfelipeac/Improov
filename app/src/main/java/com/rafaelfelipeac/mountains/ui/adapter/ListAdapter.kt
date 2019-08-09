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
import com.rafaelfelipeac.mountains.ui.fragments.list.ListFragment
import com.rafaelfelipeac.mountains.ui.fragments.today.TodayFragment
import com.rafaelfelipeac.mountains.ui.helper.ActionCompletionContract
import kotlin.math.roundToInt

class ListAdapter(val fragment: BaseFragment) : RecyclerView.Adapter<RecyclerView.ViewHolder>(),
    ActionCompletionContract {

    private lateinit var list: List<GoalRoutine>

    var clickListener: (goalRoutine: GoalRoutine) -> Unit = { }

    lateinit var touchHelper: ItemTouchHelper

    companion object {
        const val TYPE_GOAL = 1
        const val TYPE_ROUTINE = 2
    }

    override fun getItemViewType(position: Int): Int {
        return when (list[position]) {
            is Goal -> TYPE_GOAL
            is Routine -> TYPE_ROUTINE
            else -> -1
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_GOAL -> {
                GoalViewHolder(parent.inflate(R.layout.list_item_goal), fragment, touchHelper, clickListener)
            }
            TYPE_ROUTINE -> {
                RoutineViewHolder(parent.inflate(R.layout.list_item_routine), fragment, touchHelper, clickListener)
            }
            else -> {
                GoalViewHolder(parent.inflate(R.layout.list_item_goal), fragment, touchHelper, clickListener)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.setIsRecyclable(false)

        (holder as ListViewHolder).bindViews(list[position])
    }

    override fun getItemCount() = list.size

    fun setItems(list: List<GoalRoutine>) {
        this.list = list
        notifyDataSetChanged()
    }

    class GoalViewHolder(itemView: View,
                         val fragment: BaseFragment,
                         private val touchHelper: ItemTouchHelper?,
                         private val clickListener: (goalRoutine: GoalRoutine) -> Unit) :
        RecyclerView.ViewHolder(itemView), ListViewHolder {

        private val typeIcon= itemView.findViewById<ImageView>(R.id.goal_type_icon)
        private val title = itemView.findViewById<TextView>(R.id.goal_title)!!
        private val date = itemView.findViewById<TextView>(R.id.goal_date)!!
        private val score = itemView.findViewById<TextView>(R.id.goal_score)!!
        private val itemDrag = itemView.findViewById<ImageView>(R.id.goal_drag_icon)!!

        @SuppressLint("ClickableViewAccessibility")
        override fun bindViews(goalRoutine: GoalRoutine) {

            itemView.setOnClickListener { clickListener(goalRoutine) }

            val goal = goalRoutine as Goal

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

    class RoutineViewHolder(itemView: View,
                            val fragment: BaseFragment,
                            private val touchHelper: ItemTouchHelper?,
                            val clickListener: (goalRoutine: GoalRoutine) -> Unit) :
        RecyclerView.ViewHolder(itemView), ListViewHolder {

        private val typeIcon = itemView.findViewById<ImageView>(R.id.routine_type_icon)!!
        private val title = itemView.findViewById<TextView>(R.id.routine_title)
        private val type = itemView.findViewById<TextView>(R.id.routine_type)
        private val late = itemView.findViewById<TextView>(R.id.routine_late_date)
        private val score = itemView.findViewById<TextView>(R.id.routine_score)
        private val archiveImage = itemView.findViewById<ImageView>(R.id.routine_archive_image)
        private val itemDrag = itemView.findViewById<ImageView>(R.id.routine_drag_icon)

        @SuppressLint("ClickableViewAccessibility")
        override fun bindViews(goalRoutine: GoalRoutine) {

            if (fragment is TodayFragment) {
                archiveImage.gone()
                itemDrag.gone()
            }

            itemView.setOnClickListener { clickListener(goalRoutine) }

            val routine = goalRoutine as Routine

            title.text = routine.name

            if (routine.nextDate != null && routine.isLate() && !routine.isToday()) {
                late.text = routine.nextDate.format()
                late.visible()
            }

            typeIcon.layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT
            typeIcon.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
            typeIcon.background = ContextCompat.getDrawable(fragment.context!!, R.drawable.ic_routine)

            when (routine.type) {
                RoutineType.ROUT_1 -> {
                    type.text = String.format(
                        "%s", "Todo dia"
                    )
                }
                RoutineType.ROUT_2 -> {
                    type.text = String.format(
                        "%s %s",
                        routine.weekDaysLong.filter { it > 0 }.size.toString(),
                        "vezes por semana"
                    )
                }
                RoutineType.ROUT_3 -> {
                    type.text = String.format(
                        "%s %s %s",
                        routine.periodTotal.toString(),
                        "dias por",
                        routine.periodType.getName()
                    )

                    score.text = String.format(
                        "%s/%s",
                        routine.periodDone.toString(),
                        routine.periodTotal.toString()
                    )
                    score.visible()
                }
                RoutineType.ROUT_4 -> {
                    type.text = String.format(
                        "%s %s %s",
                        "A cada",
                        routine.periodDaysBetween.toString(),
                        "dias"
                    )
                }
                RoutineType.ROUT_NONE -> TODO()
            }

            itemDrag.setOnTouchListener { _, _ ->
                touchHelper?.startDrag(this)
                false
            }
        }
    }

    override fun onViewMoved(oldPosition: Int, newPosition: Int) {
        when (fragment) {
            is ListFragment -> fragment.onViewMoved(oldPosition, newPosition, list, ::notifyItemMoved)
        }
    }

    override fun onViewSwiped(position: Int, direction: Int, holder: RecyclerView.ViewHolder) {
        when (fragment) {
            is ListFragment -> fragment.onViewSwiped(position, direction, holder, list)
            is TodayFragment -> fragment.onViewSwiped(position, direction, holder, list)
        }
    }

    interface ListViewHolder {
        fun bindViews(goalRoutine: GoalRoutine)
    }
}
