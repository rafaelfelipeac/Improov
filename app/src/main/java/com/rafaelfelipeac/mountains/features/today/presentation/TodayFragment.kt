package com.rafaelfelipeac.mountains.features.today.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rafaelfelipeac.mountains.R
import com.rafaelfelipeac.mountains.core.extension.*
import com.rafaelfelipeac.mountains.core.platform.base.BaseFragment
import com.rafaelfelipeac.mountains.features.commons.Goal
import com.rafaelfelipeac.mountains.features.commons.GoalHabit
import com.rafaelfelipeac.mountains.features.commons.Habit
import com.rafaelfelipeac.mountains.features.commons.presentation.ListAdapter
import com.rafaelfelipeac.mountains.features.commons.presentation.SwipeAndDragHelperList
import com.rafaelfelipeac.mountains.features.main.MainActivity
import kotlinx.android.synthetic.main.fragment_today.*
import java.util.*

class TodayFragment : BaseFragment() {

    private val todayViewModel by lazy { viewModelFactory.get<TodayViewModel>(this) }

    lateinit var listLateAdapter: ListAdapter
    lateinit var listTodayAdapter: ListAdapter
    private var dayOfWeekAdapter = DayOfWeekAdapter(this)

    private var habitsLate: List<Habit>? = listOf()
    private var habitsToday: List<Habit>? = listOf()
    private var habitsFuture: List<Habit>? = listOf()
    private var habitsDone: List<Habit>? = listOf()

    private var goalsLate: List<Goal>? = listOf()
    private var goalsToday: List<Goal>? = listOf()
    private var goalsFuture: List<Goal>? = listOf()
    private var goalsDone: List<Goal>? = listOf()

    private var seriesToday: Int = 0

    private var maxValue = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        injector.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        (activity as MainActivity).supportActionBar?.title = getString(R.string.fragment_title_today)

        showNavigation()

        return inflater.inflate(R.layout.fragment_today, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fab.setOnClickListener {
            navController.navigate(TodayFragmentDirections.actionNavigationTodayToNavigationAdd())
        }

        setupWeekInformation()

        listLateAdapter = ListAdapter(this)
        listTodayAdapter = ListAdapter(this)

        observeViewModel()
    }

    private fun setupWeekInformation() {
        if (preferences.openWeekDays) {
            today_week_week_information.visible()
        }

        today_week_week.setOnClickListener {
            preferences.openWeekDays =
                if (!preferences.openWeekDays) {
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
        todayViewModel.user?.observe(this, Observer { user ->
            (activity as MainActivity).user = user
        })

        todayViewModel.getGoals()?.observe(this, Observer { goals ->

            this.goalsDone =
                goals.filter { it.userId == user.userId && !it.archived && it.finalDate != null && it.finalDate.isToday() && it.done }

            this.goalsLate =
                goals.filter { it.userId == user.userId && !it.archived && !it.done && it.finalDate != null && it.finalDate.isLate() && !it.finalDate.isToday() }
            this.goalsToday =
                goals.filter { it.userId == user.userId && !it.archived && !it.done && it.finalDate != null && it.finalDate.isToday() }
            this.goalsFuture =
                goals.filter { it.userId == user.userId && !it.archived && it.finalDate != null && it.finalDate.isFuture() }

            setupItems()
        })

        todayViewModel.getHabits()?.observe(this, Observer { habits ->

            this.habitsDone =
                habits.filter { it.userId == user.userId && !it.archived && it.doneToday && (it.nextDate.isToday() || it.doneDate.isToday()) }

            this.habitsLate =
                habits.filter { it.userId == user.userId && !it.archived && !it.doneToday && it.isLate() && !it.isToday() }
            this.habitsToday =
                habits.filter { it.userId == user.userId && !it.archived && !it.doneToday && it.isToday() }
            this.habitsFuture =
                habits.filter { it.userId == user.userId && !it.archived && it.isFuture() }

            setupItems()
        })
    }

    override fun onResume() {
        super.onResume()

        (activity as MainActivity).closeToolbar()
    }

    private fun setupItems() {
        setItemsLate()
        setItemsToday()
        setItemsWeek()
    }

    private fun setItemsLate() {
        if (goalsLate?.isEmpty()!! && habitsLate?.isEmpty()!!) {
            today_late_cl.gone()
        } else {
            today_late_cl.visible()

            val late = mutableListOf<GoalHabit>()

            goalsLate?.let { late.addAll(it) }
            habitsLate?.let { late.addAll(it) }

            late
                .sortedBy { it.order }
                .let { listLateAdapter.setItems(it) }

            listLateAdapter.clickListener = {
                when (it) {
                    is Habit -> {
                        val action =
                            TodayFragmentDirections.actionNavigationTodayToNavigationHabit(
                                it.habitId
                            )
                        navController.navigate(action)
                    }
                    is Goal -> {
                        val action =
                            TodayFragmentDirections.actionNavigationTodayToNavigationGoal(
                                it.goalId
                            )
                        navController.navigate(action)
                    }
                }
            }

            val swipeAndDragHelper =
                SwipeAndDragHelperList(listLateAdapter)
            val touchHelper = ItemTouchHelper(swipeAndDragHelper)

            listLateAdapter.touchHelper = touchHelper

            today_late_list.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            today_late_list.adapter = listLateAdapter

            touchHelper.attachToRecyclerView(today_late_list)
        }
    }

    private fun setItemsToday() {
        val calendar = Calendar.getInstance()
        today_day.text = calendar.time.format()

        if (goalsToday!!.isEmpty() && habitsToday!!.isEmpty() && goalsDone?.isNotEmpty()!! && habitsDone?.isNotEmpty()!!) {
            today_today_list.gone()
            today_today_placeholder.visible()

            setTodayGraph()
        } else {
            today_today_list.visible()
            today_today_placeholder.gone()

            setTodayGraph()

            val today = mutableListOf<GoalHabit>()

            goalsToday?.let { today.addAll(it) }
            habitsToday?.let { today.addAll(it) }

            today
                .sortedBy { it.order }
                .let { listTodayAdapter.setItems(it) }

            listTodayAdapter.clickListener = {
                when (it) {
                    is Habit -> {
                        val action =
                            TodayFragmentDirections.actionNavigationTodayToNavigationHabit(
                                it.habitId
                            )
                        navController.navigate(action)
                    }
                    is Goal -> {
                        val action =
                            TodayFragmentDirections.actionNavigationTodayToNavigationGoal(
                                it.goalId
                            )
                        navController.navigate(action)
                    }
                }
            }

            val swipeAndDragHelper =
                SwipeAndDragHelperList(listTodayAdapter)
            val touchHelper = ItemTouchHelper(swipeAndDragHelper)

            listTodayAdapter.touchHelper = touchHelper

            today_today_list.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            today_today_list.adapter = listTodayAdapter

            touchHelper.attachToRecyclerView(today_today_list)
        }
    }

    private fun setItemsWeek() {
        val calendar = Calendar.getInstance()
        val days = calendar.getNextWeek(context!!)

        days.forEach { day ->
            habitsFuture?.forEach { habit ->
                if (day.monthDay == habit.nextDate.format()) {
                    day.list.add(habit)
                }
            }
            goalsFuture?.forEach { goal ->
                if (day.monthDay == goal.finalDate.format()) {
                    day.list.add(goal)
                }
            }
        }

        days.let { dayOfWeekAdapter.setItems(it) }

        today_week_list.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        today_week_list.adapter = dayOfWeekAdapter
    }

    private fun setTodayGraph() {
        val valueDone = habitsDone?.size!! + goalsDone?.size!!
        val valueFinal = habitsDone?.size!! + goalsDone?.size!! + goalsToday?.size!! + habitsToday?.size!!

        if (valueFinal > 0) {
            if (valueFinal > maxValue) {
                maxValue = valueFinal
                resetToday()
            }
            setTodayValue(valueDone.toFloat())
        }

        today_value.text = String.format("%s/%s", valueDone, valueFinal)
    }

    fun onViewSwiped(position: Int, direction: Int, holder: RecyclerView.ViewHolder, items: List<GoalHabit>) {
        val goalHabit = items[position]

        when (direction) {
            ItemTouchHelper.RIGHT -> {
                when (goalHabit) {
                    is Goal -> {

                    }
                    is Habit -> {
                        val beforeHabit = goalHabit.copy()

                        goalHabit.doneToday = true
                        goalHabit.doneDate = getCurrentTime()
                        goalHabit.nextHabitDateAfterDone()

                        todayViewModel.saveHabit(goalHabit)

                        showSnackBarWithAction(
                            holder.itemView, String.format(
                                "%s %s.", getString(R.string.fragment_today_next_ocurrence),
                                goalHabit.nextDate.format()
                            ), beforeHabit, ::undoDone
                        )
                    }
                }
            }

            ItemTouchHelper.LEFT -> {
                when (goalHabit) {
                    is Goal -> {
                        todayViewModel.saveGoal(goalHabit)
                    }
                    is Habit -> {
                        todayViewModel.saveHabit(goalHabit)
                    }
                }
            }
        }
    }

    private fun undoDone(goal: Any) {
        todayViewModel.saveHabit(goal as Habit)
    }

    private fun resetToday() {
        seriesToday = today_arcView.resetValue(0F, maxValue.toFloat(), 0F)

    }

    private fun setTodayValue(value: Float) = today_arcView.setup(value, seriesToday)
}
