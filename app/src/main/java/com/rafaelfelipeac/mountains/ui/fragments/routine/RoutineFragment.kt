package com.rafaelfelipeac.mountains.ui.fragments.routine

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.rafaelfelipeac.mountains.R
import com.rafaelfelipeac.mountains.extension.format
import com.rafaelfelipeac.mountains.extension.visible
import com.rafaelfelipeac.mountains.models.Routine
import com.rafaelfelipeac.mountains.models.RoutineType
import com.rafaelfelipeac.mountains.ui.activities.MainActivity
import com.rafaelfelipeac.mountains.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_routine.*

class RoutineFragment : BaseFragment() {

    var routineId: Long? = null
    var routineNew: Boolean? = null

    var routine = Routine()

    private lateinit var viewModel: RoutineViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //injector.inject(this)

        setHasOptionsMenu(true)

        (activity as MainActivity).openToolbar()

        (activity as MainActivity).bottomNavigationVisible(View.GONE)

        routineId = arguments?.let { RoutineFragmentArgs.fromBundle(it).routineId }
        routineNew = arguments?.let { RoutineFragmentArgs.fromBundle(it).routineNew }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        (activity as MainActivity).supportActionBar?.title = "Rotina"

        viewModel = ViewModelProviders.of(this).get(RoutineViewModel::class.java)
        viewModel.init(routineId!!)

        return inflater.inflate(R.layout.fragment_routine, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.user?.observe(this, Observer { user ->
            (activity as MainActivity).user = user
        })

        viewModel.getRoutines()?.observe(this, Observer { routine ->
            this.routine = routine

            setupRoutine()
        })

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                if (routineNew!!) {
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

    private fun setupRoutine() {
        routine_name.text = routine.name

        routine_next_date.text = String.format("%s %s", "Próxima ocorrência: ", routine.nextDate.format())

        if (routine.type == RoutineType.ROUT_3 || routine.type == RoutineType.ROUT_4) {
            routine_last_date.visible()
            routine_last_date.text = String.format("%s %s", "Último dia do ciclo:", routine.lastDate.format())
        }
    }
 }
