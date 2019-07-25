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
import com.rafaelfelipeac.mountains.extension.*
import com.rafaelfelipeac.mountains.models.DayOfWeek
import com.rafaelfelipeac.mountains.models.Goal
import com.rafaelfelipeac.mountains.ui.activities.MainActivity
import com.rafaelfelipeac.mountains.ui.adapter.DayOfWeekAdapter
import com.rafaelfelipeac.mountains.ui.adapter.GoalsRepetitionAdapter
import com.rafaelfelipeac.mountains.ui.base.BaseFragment
import com.rafaelfelipeac.mountains.ui.helper.SwipeAndDragHelperGoal
import kotlinx.android.synthetic.main.fragment_today.*

class TodayFragment : BaseFragment() {

    private var weekVisible = false

    private var goalsLateAdapter = GoalsRepetitionAdapter(this)
    private var goalsTodayAdapter = GoalsRepetitionAdapter(this)
    private var goalsWeekAdapter = DayOfWeekAdapter(this)

    private lateinit var viewModel: TodayViewModel

    private var goalsLate: List<Goal>? = null
    private var goalsToday: List<Goal>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProviders.of(this).get(TodayViewModel::class.java)

        return inflater.inflate(R.layout.fragment_today, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fab.setOnClickListener {
            navController.navigate(R.id.action_navigation_today_to_navigation_goalForm)
        }

        today_week_week.setOnClickListener {
            weekVisible =
                if (!weekVisible) {
                    today_week_week_information.visible()
                    today_week_image.background = ContextCompat.getDrawable(context!!, R.drawable.ic_up)
                    true
                } else {
                    today_week_week_information.gone()
                    today_week_image.background = ContextCompat.getDrawable(context!!, R.drawable.ic_down)
                    false
                }
        }

        observeViewModel()

        showNavigation()
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
            var goalsFuture =
                goals.filter { it.userId == user.userId && !it.archived && it.repetition && !it.repetitionDoneToday && it.isFuture() }

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

        goalsLateAdapter.clickListener = {
            val action = TodayFragmentDirections.actionNavigationTodayToNavigationGoal(it)
            navController.navigate(action)
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

        goalsTodayAdapter.clickListener = {
            val action = TodayFragmentDirections.actionNavigationTodayToNavigationGoal(it)
            navController.navigate(action)
        }

        val swipeAndDragHelper = SwipeAndDragHelperGoal(goalsTodayAdapter)
        val touchHelper = ItemTouchHelper(swipeAndDragHelper)

        goalsTodayAdapter.touchHelper = touchHelper

        today_today_list.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        today_today_list.adapter = goalsTodayAdapter

        touchHelper.attachToRecyclerView(today_today_list)
    }

    private fun setItemsWeek() {
        val days = listOf(
            DayOfWeek("sexta-feira","26 JUL"),
            DayOfWeek("sábado", "27 JUL"),
            DayOfWeek("domingo", "28 JUL"),
            DayOfWeek("segunda-feira", "29 JUL"),
            DayOfWeek("terça-feira","30 JUL"),
            DayOfWeek("quarta-feira", "31 JUL"),
            DayOfWeek("quinta-feira", "1 AGO"))

        days.let { goalsWeekAdapter.setItems(it) }

        today_week_list.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        today_week_list.adapter = goalsWeekAdapter
    }

    fun onViewSwiped(position: Int, direction: Int, holder: RecyclerView.ViewHolder, items: MutableList<Goal>) {
        val goal = items[position]

        when (direction) {
            ItemTouchHelper.RIGHT -> {
                goal.repetitionDoneToday = true
                goal.repetitionLastDate = getCurrentTime()
                goal.addNextRepetitionDate(1)

                viewModel.saveGoal(goal)

                showSnackBarLong(String.format("%s %s", "Feito. Próxima ocorrência: ", goal.repetitionNextDate.format()))
            }
            ItemTouchHelper.LEFT -> {
                viewModel.saveGoal(goal)
            }
        }
    }
}
