package com.rafaelfelipeac.improov.future.habit

import com.rafaelfelipeac.improov.R

package com.rafaelfelipeac.improov.features.commons.presentation

class HistoricAdapter : BaseAdapter<Historic>() {

class HabitsAdapter(val fragment: BaseFragment) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(),
    ActionCompletionContract {

    private lateinit var list: MutableList<Habit>

    var clickListener: (habit: Habit) -> Unit = { }

    lateinit var touchHelper: ItemTouchHelper

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return HabitViewHolder(
            parent.inflate(R.layout.list_item_habit),
            fragment,
            touchHelper,
            clickListener
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        (holder as ListViewHolder).bindViews(list[position])
    }

    override fun getItemCount() = list.size

    fun setItems(list: List<Habit>) {
        this.list = list.toMutableList()
        notifyDataSetChanged()
    }

    class HabitViewHolder(itemView: View,
                          val fragment: BaseFragment,
                          private val touchHelper: ItemTouchHelper?,
                          val clickListener: (habit: Habit) -> Unit) :
        RecyclerView.ViewHolder(itemView),
        ListViewHolder {

        private val typeIcon = itemView.findViewById<ImageView>(R.id.habit_type_icon)!!
        private val title = itemView.findViewById<TextView>(R.id.habit_title)
        private val type = itemView.findViewById<TextView>(R.id.habit_type)
        private val late = itemView.findViewById<TextView>(R.id.habit_late_date)
        private val score = itemView.findViewById<TextView>(R.id.habit_score)
        //private val archiveImage = itemView.findViewById<ImageView>(R.id.habit_archive_image)
        private val itemDrag = itemView.findViewById<ImageView>(R.id.habit_drag_icon)

        @SuppressLint("ClickableViewAccessibility")
        override fun bindViews(habit: Habit) {

            if (fragment is TodayFragment) {
                //archiveImage.gone()
                itemDrag.gone()
            }

            itemView.setOnClickListener { clickListener(habit) }

            title.text = habit.name

            if (habit.nextDate != null && habit.isLate() && !habit.isToday()) {
                late.text = fragment.context?.let { habit.nextDate.format(it) }
                late.visible()
            }

            typeIcon.layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT
            typeIcon.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
            typeIcon.background = ContextCompat.getDrawable(fragment.context!!, R.drawable.ic_habit)

            when (habit.type) {
                HabitType.HAB_EVERYDAY -> {
                    type.text = String.format(
                        "%s", fragment.context!!.getString(R.string.list_adapter_habit_type_everyday)
                    )
                }
                HabitType.HAB_WEEKDAYS -> {
                    type.text = String.format(
                        "%s %s",
                        habit.weekDaysLong.filter { it > 0 }.size.toString(),
                       fragment.context!!.getString(R.string.list_adapter_habit_type_weekdays)
                    )
                }
                HabitType.HAB_PERIOD -> {
                    type.text = String.format(
                        "%s %s %s",
                        habit.periodTotal.toString(),
                        fragment.context!!.getString(R.string.list_adapter_habit_type_period),
                        habit.periodType.getName(fragment.context!!)
                    )

                    score.text = String.format(
                        "%s/%s",
                        habit.periodDone.toString(),
                        habit.periodTotal.toString()
                    )
                    score.visible()
                }
                HabitType.HAB_CUSTOM -> {
                    type.text = String.format(
                        "%s %s %s",
                        fragment.context!!.getString(R.string.list_adapter_habit_type_custom_1),
                        habit.periodDaysBetween.toString(),
                        fragment.context!!.getString(R.string.list_adapter_habit_type_custom_2)
                    )
                }
                HabitType.HAB_NONE -> TODO()
            }

            itemDrag.setOnTouchListener { _, _ ->
                touchHelper?.startDrag(this)
                false
            }
        }
    }
    override fun onViewMoved(fromPosition: Int, toPosition: Int) {
        when (fragment) {
            is GoalsFragment -> fragment.onViewMoved(fromPosition, toPosition, list, ::notifyItemMoved)
        }
    }

    override fun onViewSwiped(position: Int, direction: Int, holder: RecyclerView.ViewHolder) {
        when (fragment) {
            //is GoalsFragment -> fragment.onViewSwiped(position, direction, holder, list)
            is TodayFragment -> fragment.onViewSwiped(position, direction, holder, list)
        }
    }

    interface ListViewHolder {
        fun bindViews(goalHabit: GoalHabit)
    }
}
