package com.rafaelfelipeac.mountains.ui.fragments.today

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rafaelfelipeac.mountains.R
import com.rafaelfelipeac.mountains.app.prefs
import com.rafaelfelipeac.mountains.extension.*
import com.rafaelfelipeac.mountains.models.GoalHabit
import com.rafaelfelipeac.mountains.models.Habit
import com.rafaelfelipeac.mountains.ui.activities.MainActivity
import com.rafaelfelipeac.mountains.ui.adapter.DayOfWeekAdapter
import com.rafaelfelipeac.mountains.ui.adapter.ListAdapter
import com.rafaelfelipeac.mountains.ui.base.BaseFragment
import com.rafaelfelipeac.mountains.ui.helper.SwipeAndDragHelperList
import kotlinx.android.synthetic.main.fragment_today.*
import java.util.*

class TodayFragment : BaseFragment() {

    private lateinit var viewModel: TodayViewModel

    private lateinit var goalsLateAdapter: ListAdapter
    private lateinit var goalsTodayAdapter: ListAdapter
    private var goalsWeekAdapter = DayOfWeekAdapter(this)

    private var habitsLate: List<Habit>? = null
    private var habitsToday: List<Habit>? = null
    private var habitsFuture: List<Habit>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProviders.of(this).get(TodayViewModel::class.java)

        return inflater.inflate(R.layout.fragment_today, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fab.setOnClickListener {
            (activity as MainActivity).openBottomSheetFAB()
        }

        setupWeekInformation()

        observeViewModel()

        showNavigation()
    }

    private fun setupWeekInformation() {
        if (prefs.openWeekDays) {
            today_week_week_information.visible()
        }

        today_week_week.setOnClickListener {
            prefs.openWeekDays =
                if (!prefs.openWeekDays) {
                    today_week_week_information.visible()
                    today_week_image.background = ContextCompat.getDrawable(context!!, R.drawable.ic_up)
                    true
                } else {
                    today_week_week_information.gone()
                    today_week_image.background = ContextCompat.getDrawable(context!!, R.drawable.ic_down)
                    false
                }
        }
    }

    private fun observeViewModel() {
        viewModel.user?.observe(this, Observer { user ->
            (activity as MainActivity).user = user
        })

        viewModel.getHabits()?.observe(this, Observer { habits ->

            this.habitsLate =
                habits.filter { it.userId == user.userId && !it.archived && !it.doneToday && it.isLate() && !it.isToday() }
            this.habitsToday =
                habits.filter { it.userId == user.userId && !it.archived && !it.doneToday && it.isToday() }
            this.habitsFuture =
                habits.filter { it.userId == user.userId && !it.archived && it.isFuture() }

            setItemsLate()
            setItemsToday()
            setItemsWeek()
        })
    }

    override fun onResume() {
        super.onResume()

        (activity as MainActivity).closeToolbar()
    }

    private fun setItemsLate() {
        goalsLateAdapter = ListAdapter(this)
        habitsLate?.sortedBy { it.order }.let { goalsLateAdapter.setItems(it!!) }

        goalsLateAdapter.clickListener = {
            if (it is Habit) {
                val action = TodayFragmentDirections.actionNavigationTodayToNavigationHabit(it.habitId)
                navController.navigate(action)
            }
        }

        val swipeAndDragHelper = SwipeAndDragHelperList(goalsLateAdapter)
        val touchHelper = ItemTouchHelper(swipeAndDragHelper)

        goalsLateAdapter.touchHelper = touchHelper

        today_late_list.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        today_late_list.adapter = goalsLateAdapter

        touchHelper.attachToRecyclerView(today_late_list)
    }

    private fun setItemsToday() {
        goalsTodayAdapter = ListAdapter(this)
        habitsToday?.sortedBy { it.order }.let { goalsTodayAdapter.setItems(it!!) }

        goalsTodayAdapter.clickListener = {
            if (it is Habit) {
                val action = TodayFragmentDirections.actionNavigationTodayToNavigationHabit(it.habitId)
                navController.navigate(action)
            }
        }

        val swipeAndDragHelper = SwipeAndDragHelperList(goalsTodayAdapter)
        val touchHelper = ItemTouchHelper(swipeAndDragHelper)

        goalsTodayAdapter.touchHelper = touchHelper

        today_today_list.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        today_today_list.adapter = goalsTodayAdapter

        touchHelper.attachToRecyclerView(today_today_list)
    }

    private fun setItemsWeek() {
        val calendar = Calendar.getInstance()
        val days = calendar.getNextWeek()

        days.forEach { day ->
            habitsFuture?.forEach { habit ->
                if (day.monthDay == habit.nextDate.format()) {
                    day.habits.add(habit)
                }
            }
        }

        days.let { goalsWeekAdapter.setItems(it) }

        today_week_list.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        today_week_list.adapter = goalsWeekAdapter
    }

    fun onViewSwiped(position: Int, direction: Int, holder: RecyclerView.ViewHolder, items: List<GoalHabit>) {
        val habit = items[position] as Habit

        when (direction) {
            ItemTouchHelper.RIGHT -> {
                val beforeHabit = habit.copy()

                habit.doneToday = true
                habit.doneDate = getCurrentTime()
                habit.nextHabitDateAfterDone()

                viewModel.saveHabit(habit)

                showSnackBarWithAction(holder.itemView, String.format("%s %s.", "Próxima ocorrência: ",
                    habit.nextDate.format()), beforeHabit, ::undoDone)
            }

            ItemTouchHelper.LEFT -> {
                viewModel.saveHabit(habit)
            }
        }
    }

    private fun undoDone(goal: Any) {
        viewModel.saveHabit(goal as Habit)
    }
}