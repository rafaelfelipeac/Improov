package com.rafaelfelipeac.mountains.ui.fragments.habit

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.rafaelfelipeac.mountains.R
import com.rafaelfelipeac.mountains.extension.format
import com.rafaelfelipeac.mountains.extension.visible
import com.rafaelfelipeac.mountains.models.Habit
import com.rafaelfelipeac.mountains.models.HabitType
import com.rafaelfelipeac.mountains.ui.activities.MainActivity
import com.rafaelfelipeac.mountains.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_habit.*

class HabitFragment : BaseFragment() {

    var habitId: Long? = null
    var habitNew: Boolean? = null

    var habit = Habit()

    private lateinit var viewModel: HabitViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        injector.inject(this)

        setHasOptionsMenu(true)

        (activity as MainActivity).openToolbar()

        (activity as MainActivity).bottomNavigationVisible(View.GONE)

        habitId = arguments?.let { HabitFragmentArgs.fromBundle(it).habitId }
        habitNew = arguments?.let { HabitFragmentArgs.fromBundle(it).habitNew }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        (activity as MainActivity).supportActionBar?.title = "Rotina"

        viewModel = ViewModelProviders.of(this).get(HabitViewModel::class.java)
        viewModel.init(habitId!!)

        return inflater.inflate(R.layout.fragment_habit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.user?.observe(this, Observer { user ->
            (activity as MainActivity).user = user
        })

        viewModel.getHabits()?.observe(this, Observer { habit ->
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
            R.id.menu_goal_edit -> {
                navController.navigate(HabitFragmentDirections.actionNavigationHabitToNavigationHabitForm(habit.habitId))
            }
            android.R.id.home -> {
                if (habitNew!!) {
                    navController.navigateUp()
                }
            }
        }

        return false
    }

    override fun onStart() {
        super.onStart()

        hideNavigation()
    }

    private fun setupHabit() {
        habit_name.text = habit.name

        habit_next_date.text = String.format("%s %s", "Próxima ocorrência: ", habit.nextDate.format())

        if (habit.type == HabitType.HAB_PERIOD || habit.type == HabitType.HAB_CUSTOM) {
            habit_last_date.visible()
            habit_last_date.text = String.format("%s %s", "Último dia do ciclo:", habit.lastDate.format())
        }
    }
 }
