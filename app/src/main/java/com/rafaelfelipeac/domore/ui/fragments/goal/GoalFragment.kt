package com.rafaelfelipeac.domore.ui.fragments.goal

import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.*
import com.hookedonplay.decoviewlib.charts.SeriesItem
import com.hookedonplay.decoviewlib.events.DecoEvent
import com.rafaelfelipeac.domore.R
import com.rafaelfelipeac.domore.models.Goal
import com.rafaelfelipeac.domore.ui.activities.MainActivity
import com.rafaelfelipeac.domore.ui.adapter.ItemsAdapter
import com.rafaelfelipeac.domore.ui.base.BaseFragment
import com.rafaelfelipeac.domore.ui.helper.SwipeAndDragHelperItem
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_goal.*
import kotlinx.android.synthetic.main.fragment_metrics.*
import javax.inject.Inject

class GoalFragment : BaseFragment() {

    @Inject
    lateinit var itemsAdapter: ItemsAdapter

    var goal: Goal? = null

    var cont: Float = 0F

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        injector.inject(this)

        goal = GoalFragmentArgs.fromBundle(arguments!!).goal

        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        (activity as MainActivity).supportActionBar?.title = "Meta"

        return inflater.inflate(R.layout.fragment_goal, container, false)
    }

    override fun onStart() {
        super.onStart()

        hideNavigation()

        setupGoal()
    }

    override fun onResume() {
        super.onResume()

        setupGoal()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hideSoftKeyboard(activity!!)

        setupMedal()
        /////
        setupBronze()
        /////
        setupSilver()
        /////
        setupGold()

//        goal_btn_inc.setOnClickListener {
//            goal_inc_dec_total.text = (goal_inc_dec_total.text.toString().toFloat() + 1F).toString()
//            goal?.medalValue = goal_inc_dec_total.text.toString().toFloat()
//            goalDAO?.update(goal!!)
//        }
//
//        goal_btn_dec.setOnClickListener {
//            goal_inc_dec_total.text = (goal_inc_dec_total.text.toString().toFloat() - 1F).toString()
//            goal?.medalValue = goal_inc_dec_total.text.toString().toFloat()
//            goalDAO?.update(goal!!)
//        }

        goal_btn_save.setOnClickListener {
            if (goal_total_total.text.isNotEmpty()) {
                goal?.medalValue = goal_total_total.text.toString().toFloat()
                goalDAO?.update(goal!!)

                Snackbar
                    .make(view, "Valor atualizado.", Snackbar.LENGTH_LONG)
                    .show()
            }
        }

        setItemsAdapter()
    }

    private fun setupGold() {
        arcViewGold.configureAngles(300, 0) // formato e orientacão

        arcViewGold.addSeries(
            SeriesItem.Builder(Color.argb(255, 218, 218, 218))
                .setRange(0f, 100f, 100f)
                .setInitialVisibility(true)
                .setLineWidth(32f)
                .build()
        )
        // linha cinza
        // caminho total

        val seriesItem1Gold =
            SeriesItem.Builder(Color.argb(255, 64, 196, 0))
                .setRange(0f, 100f, 0f)
                .setLineWidth(32f)
                .build()
        // linha verde
        // progresso feito

        val series1IndexGold = arcViewGold.addSeries(seriesItem1Gold)
        arcViewGold.addEvent(DecoEvent.Builder(0F).setIndex(series1IndexGold).setDelay(10).build())
        arcViewGold.addEvent(DecoEvent.Builder(75F).setIndex(series1IndexGold).setDelay(10).build())
    }

    private fun setupSilver() {
        arcViewSilver.configureAngles(300, 0) // formato e orientacão

        arcViewSilver.addSeries(
            SeriesItem.Builder(Color.argb(255, 218, 218, 218))
                .setRange(0f, 100f, 100f)
                .setInitialVisibility(true)
                .setLineWidth(32f)
                .build()
        )
        // linha cinza
        // caminho total


        val seriesItem1Silver =
            SeriesItem.Builder(Color.argb(255, 64, 196, 0))
                .setRange(0f, 100f, 0f)
                .setLineWidth(32f)
                .build()
        // linha verde
        // progresso feito

        val series1IndexSilver = arcViewSilver.addSeries(seriesItem1Silver)
        arcViewSilver.addEvent(DecoEvent.Builder(0F).setIndex(series1IndexSilver).setDelay(10).build())
        arcViewSilver.addEvent(DecoEvent.Builder(75F).setIndex(series1IndexSilver).setDelay(10).build())
    }

    private fun setupBronze() {
        arcViewBronze.configureAngles(300, 0) // formato e orientacão

        arcViewBronze.addSeries(
            SeriesItem.Builder(Color.argb(255, 218, 218, 218))
                .setRange(0f, 100f, 100f)
                .setInitialVisibility(true)
                .setLineWidth(32f)
                .build()
        )
        // linha cinza
        // caminho total

        val seriesItem1Bronze =
            SeriesItem.Builder(Color.argb(255, 64, 196, 0))
                .setRange(0f, 100f, 0f)
                .setLineWidth(32f)
                .build()
        // linha verde
        // progresso feito

        val series1IndexBronze = arcViewBronze.addSeries(seriesItem1Bronze)
        arcViewBronze.addEvent(DecoEvent.Builder(0F).setIndex(series1IndexBronze).setDelay(10).build())
        arcViewBronze.addEvent(DecoEvent.Builder(75F).setIndex(series1IndexBronze).setDelay(10).build())
    }

    private fun setupMedal() {
        arcViewMedal.configureAngles(300, 0) // formato e orientacão

        arcViewMedal.addSeries(
            SeriesItem.Builder(Color.argb(255, 218, 218, 218))
                .setRange(0f, 100f, 100f)
                .setInitialVisibility(true)
                .setLineWidth(32f)
                .build()
        )
        // linha cinza
        // caminho total

        val seriesItem1 =
            SeriesItem.Builder(Color.argb(255, 64, 196, 0))
                .setRange(0f, 100f, 0f)
                .setLineWidth(32f)
                .build()
        // linha verde
        // progresso feito

        val series1Index = arcViewMedal.addSeries(seriesItem1)
//        arcViewMedal.addEvent(DecoEvent.Builder(0F).setIndex(series1Index).setDelay(10).build())
//        arcViewMedal.addEvent(DecoEvent.Builder(75F).setIndex(series1Index).setDelay(10).build())

        goal_btn_inc.setOnClickListener {
            cont += 10

            arcViewMedal.addEvent(DecoEvent.Builder(cont).setIndex(series1Index).setDelay(0).build())
        }

        goal_btn_dec.setOnClickListener {
            cont -= 10

            arcViewMedal.addEvent(DecoEvent.Builder(cont).setIndex(series1Index).setDelay(0).build())
        }
    }

    private fun setItemsAdapter() {
        val itemsList = itemDAO?.getAll()
        val orderItemsList =
            itemsList?.filter { !it.done && it.goalId == goal?.goalId && it.goalId != 0L }?.sortedBy { it.order }

        itemsAdapter.setItems(orderItemsList!!)

        itemsAdapter.clickListener = {
            navController.navigate(R.id.action_goalFragment_to_itemFragment)
        }

        goal_items_list.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        val swipeAndDragHelper = SwipeAndDragHelperItem(itemsAdapter)
        val touchHelper = ItemTouchHelper(swipeAndDragHelper)

        itemsAdapter.setTouchHelper(touchHelper)

        goal_items_list.adapter = itemsAdapter

        touchHelper.attachToRecyclerView(goal_items_list)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {

        if (goal?.type == 1) {
            inflater?.inflate(R.menu.menu_add, menu)
        }

        inflater?.inflate(R.menu.menu_edit, menu)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_goal_add -> {

                val action =
                    GoalFragmentDirections.actionGoalFragmentToItemFormFragment(goal!!)
                navController.navigate(action)

                return true
            }
            R.id.menu_goal_edit -> {
                val action =
                    GoalFragmentDirections.actionGoalFragmentToGoalFormFragment(goal!!)
                navController.navigate(action)
            }
        }

        return false
    }

    private fun setupGoal() {

        goal_title.text = goal?.name
        goal_inc_dec_total.text = goal?.medalValue.toString()
        goal_total_total.setText(goal?.medalValue.toString())

        if (goal?.trophies!!) {
            goal_medal.visibility = View.INVISIBLE
            goal_trophies.visibility = View.VISIBLE

            goal_trophy_bronze_text.text = goal?.bronzeValue.toString()
            goal_trophy_silver_text.text = goal?.silverValue.toString()
            goal_trophy_gold_text.text = goal?.goldValue.toString()
        }

        when (goal?.type) {
            1 -> {
                goal_items_list.visibility = View.VISIBLE
                (activity as MainActivity).toolbar.inflateMenu(R.menu.menu_add)
            }
            2 -> {
                goal_cl_dec_inc.visibility = View.VISIBLE
            }
            3 -> {
                goal_cl_total.visibility = View.VISIBLE
            }
        }
    }
}
