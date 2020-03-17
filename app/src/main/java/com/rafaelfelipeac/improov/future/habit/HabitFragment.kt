package com.rafaelfelipeac.improov.future.habit

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.rafaelfelipeac.improov.R
import com.rafaelfelipeac.improov.core.extension.format
import com.rafaelfelipeac.improov.core.extension.visible
import com.rafaelfelipeac.improov.core.platform.base.BaseFragment
import com.rafaelfelipeac.improov.features.main.MainActivity
import kotlinx.android.synthetic.main.fragment_habit.*

class HabitFragment : BaseFragment() {

    var habitId: Long? = null
    var habitNew: Boolean? = null

    var habit = Habit()

    private val habitViewModel by lazy { viewModelFactory.get<HabitViewModel>(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //injector.inject(this)

        setHasOptionsMenu(true)

        (activity as MainActivity).openToolbar()

        habitId = arguments?.let { HabitFragmentArgs.fromBundle(it).habitId }
        habitNew = arguments?.let { HabitFragmentArgs.fromBundle(it).habitNew }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        (activity as MainActivity).supportActionBar?.title = getString(R.string.habit_title)

        hideNavigation()

        habitViewModel.init(habitId!!)

        return inflater.inflate(R.layout.fragment_habit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()
    }

    private fun observeViewModel() {
        habitViewModel.getHabits()?.observe(this, Observer { habit ->
            this.habit = habit

            setupHabit()
        })

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_edit, menu)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_edit -> {
                val action = HabitFragmentDirections.actionNavigationHabitToNavigationHabitForm()
                action.habitId = habit.habitId
                navController.navigate(action)
            }
            android.R.id.home -> {
                if (habitNew!!) {
                    navController.navigateUp()
                }
            }
        }

        return false
    }

    private fun setupHabit() {
        //habit_name.text = habit.name

        habit_next_date.text = String.format("%s %s", getString(R.string.habit_next_ocurrence),
            context?.let { habit.nextDate.format(it) })

        if (habit.type == HabitType.HAB_PERIOD || habit.type == HabitType.HAB_CUSTOM) {
            habit_last_date.visible()
            habit_last_date.text = String.format("%s %s", getString(R.string.habit_last_day_cycle),
                context?.let { habit.lastDate.format(it) })
        }
    }
}
