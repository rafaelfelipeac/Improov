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
import com.rafaelfelipeac.mountains.models.Goal
import com.rafaelfelipeac.mountains.ui.activities.MainActivity
import com.rafaelfelipeac.mountains.ui.adapter.DayOfWeekAdapter
import com.rafaelfelipeac.mountains.ui.adapter.GoalsAdapter
import com.rafaelfelipeac.mountains.ui.base.BaseFragment
import com.rafaelfelipeac.mountains.ui.helper.SwipeAndDragHelperGoal
import kotlinx.android.synthetic.main.fragment_today.*
import java.util.*

class TodayFragment : BaseFragment() {

    private lateinit var viewModel: TodayViewModel

    private var goalsLateAdapter = GoalsAdapter(this)
    private var goalsTodayAdapter = GoalsAdapter(this)
    private var goalsWeekAdapter = DayOfWeekAdapter(this)

    private var goalsLate: List<Goal>? = null
    private var goalsToday: List<Goal>? = null
    private var goalsFuture: List<Goal>? = null

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

        viewModel.getGoals()?.observe(this, Observer { goals ->
            this.goalsLate =
                goals.filter { it.userId == user.userId && !it.archived && it.repetition && !it.repetitionDoneToday && it.isLate() && !it.isToday() }
            this.goalsToday =
                goals.filter { it.userId == user.userId && !it.archived && it.repetition && !it.repetitionDoneToday && it.isToday() }
            this.goalsFuture =
                goals.filter { it.userId == user.userId && !it.archived && it.repetition && it.isFuture() }

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
        goalsLate?.sortedBy { it.order }.let { goalsLateAdapter.setItems(it!!) }

        goalsLateAdapter.clickListener = { goalId: Long, repetition: Boolean ->
            if (repetition) {
                val action = TodayFragmentDirections.actionNavigationTodayToNavigationOtherGoal()
                navController.navigate(action)
            } else {
                val action = TodayFragmentDirections.actionNavigationTodayToNavigationGoal(goalId)
                navController.navigate(action)
            }
        }

        val swipeAndDragHelper = SwipeAndDragHelperGoal(goalsLateAdapter)
        val touchHelper = ItemTouchHelper(swipeAndDragHelper)

        goalsLateAdapter.touchHelper = touchHelper

        today_late_list.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        today_late_list.adapter = goalsLateAdapter

        touchHelper.attachToRecyclerView(today_late_list)
    }

    private fun setItemsToday() {
        goalsToday?.sortedBy { it.order }.let { goalsTodayAdapter.setItems(it!!) }

        goalsTodayAdapter.clickListener = { goalId: Long, repetition: Boolean ->
            if (repetition) {
                val action = TodayFragmentDirections.actionNavigationTodayToNavigationOtherGoal()
                navController.navigate(action)
            } else {
                val action = TodayFragmentDirections.actionNavigationTodayToNavigationGoal(goalId)
                navController.navigate(action)
            }
        }

        val swipeAndDragHelper = SwipeAndDragHelperGoal(goalsTodayAdapter)
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
            goalsFuture?.forEach { goal ->
                if (day.title2 == goal.repetitionNextDate.format()) {
                    day.list.add(goal)
                }
            }
        }

        days.let { goalsWeekAdapter.setItems(it) }

        today_week_list.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        today_week_list.adapter = goalsWeekAdapter
    }

    fun onViewSwiped(position: Int, direction: Int, holder: RecyclerView.ViewHolder, items: MutableList<Goal>) {
        val goal = items[position]

        when (direction) {
            ItemTouchHelper.RIGHT -> {
                val beforeGoal = goal.copy()

                goal.repetitionDoneToday = true
                goal.repetitionDoneDate = getCurrentTime()
                goal.nextRepetitionDateAfterDone()

                viewModel.saveGoal(goal)

                showSnackBarWithAction(
                    holder.itemView, String.format(
                        "%s %s.", "Próxima ocorrência: ",
                        goal.repetitionNextDate.format()
                    ), beforeGoal, ::undoDone
                )
            }
            ItemTouchHelper.LEFT -> {
                viewModel.saveGoal(goal)
            }
        }
    }

    private fun undoDone(goal: Any) {
        viewModel.saveGoal(goal as Goal)
    }
}