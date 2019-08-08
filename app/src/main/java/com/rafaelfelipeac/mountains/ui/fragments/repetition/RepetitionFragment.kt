package com.rafaelfelipeac.mountains.ui.fragments.repetition

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
import com.rafaelfelipeac.mountains.models.Repetition
import com.rafaelfelipeac.mountains.models.RepetitionType
import com.rafaelfelipeac.mountains.ui.activities.MainActivity
import com.rafaelfelipeac.mountains.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_repetition.*

class RepetitionFragment : BaseFragment() {

    var repetitionId: Long? = null
    var repetitionNew: Boolean? = null

    var repetition = Repetition()

    private lateinit var viewModel: RepetitionViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //injector.inject(this)

        setHasOptionsMenu(true)

        (activity as MainActivity).openToolbar()

        (activity as MainActivity).bottomNavigationVisible(View.GONE)

        repetitionId = arguments?.let { RepetitionFragmentArgs.fromBundle(it).repetitionId }
        repetitionNew = arguments?.let { RepetitionFragmentArgs.fromBundle(it).repetitionNew }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        (activity as MainActivity).supportActionBar?.title = "Repetição"

        viewModel = ViewModelProviders.of(this).get(RepetitionViewModel::class.java)
        viewModel.init(repetitionId!!)

        return inflater.inflate(R.layout.fragment_repetition, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.user?.observe(this, Observer { user ->
            (activity as MainActivity).user = user
        })

        viewModel.getRepetition()?.observe(this, Observer { repetition ->
            this.repetition = repetition

            setupRepetition()
        })

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                if (repetitionNew!!) {
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

    private fun setupRepetition() {
        repetition_name.text = repetition.name

        repetition_next_date.text = String.format("%s %s", "Próxima ocorrência: ", repetition.nextDate.format())

        if (repetition.type == RepetitionType.REP3 || repetition.type == RepetitionType.REP4) {
            repetition_last_date.visible()
            repetition_last_date.text = String.format("%s %s", "Último dia do ciclo:", repetition.lastDate.format())
        }
    }
 }
