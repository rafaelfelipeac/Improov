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
import javax.inject.Inject

class GoalFragment : BaseFragment() {

    @Inject
    lateinit var itemsAdapter: ItemsAdapter

    var goal: Goal? = null

    private var cont: Float = 0F

    private var series1IndexBronze: Int = 0
    private var series1IndexSilver: Int = 0
    private var series1IndexGold: Int = 0
    private var series1IndexMedal: Int = 0

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

        if (!goal?.trophies!!) {
            setupMedal()
        } else {
            setupTrophyBronze()
            setupTrophySilver()
            setupTrophyGold()
        }

        goal_btn_inc.setOnClickListener {
            cont += 10

            goal_inc_dec_total.text = cont.toString()
            goal?.actualValue = cont
            goalDAO?.update(goal!!)

            if (goal_trophies.visibility == View.INVISIBLE) {
                setMedalValue()
            } else {
                setTrophiesValueInc()
            }
        }

        goal_btn_dec.setOnClickListener {
            cont -= 10

            goal_inc_dec_total.text = cont.toString()
            goal?.actualValue = cont
            goalDAO?.update(goal!!)

            if (goal_trophies.visibility == View.INVISIBLE) {
                setMedalValue()
            } else {
                setTrophiesValueDec()
            }
        }

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

    private fun setupMedal() {
        arcViewMedal.configureAngles(300, 0)

        arcViewMedal.addSeries(
            SeriesItem.Builder(Color.argb(255, 218, 218, 218))
                .setRange(0f, 100f, 100f)
                .setInitialVisibility(true)
                .setLineWidth(32f)
                .build()
        )

        val seriesItem1 =
            SeriesItem.Builder(Color.argb(255, 64, 196, 0))
                .setRange(0f, goal?.medalValue!!, 0f)
                .setLineWidth(32f)
                .build()

        series1IndexMedal = arcViewMedal.addSeries(seriesItem1)
    }

    private fun setupTrophyBronze() {
        arcViewBronze.configureAngles(300, 0)

        arcViewBronze.addSeries(
            SeriesItem.Builder(Color.argb(255, 218, 218, 218))
                .setRange(0f, 100f, 100f)
                .setInitialVisibility(true)
                .setLineWidth(32f)
                .build()
        )

        val seriesItem1Bronze =
            SeriesItem.Builder(Color.argb(255, 64, 196, 0))
                .setRange(0f, goal?.bronzeValue!!, 0f)
                .setLineWidth(32f)
                .build()

        series1IndexBronze = arcViewBronze.addSeries(seriesItem1Bronze)
    }

    private fun setupTrophySilver() {
        arcViewSilver.configureAngles(300, 0)

        arcViewSilver.addSeries(
            SeriesItem.Builder(Color.argb(255, 218, 218, 218))
                .setRange(0f, 100f, 100f)
                .setInitialVisibility(true)
                .setLineWidth(32f)
                .build()
        )

        val seriesItem1Silver =
            SeriesItem.Builder(Color.argb(255, 64, 196, 0))
                .setRange(goal?.bronzeValue!!, goal?.silverValue!!, goal?.bronzeValue!!)
                .setLineWidth(32f)
                .build()

        series1IndexSilver = arcViewSilver.addSeries(seriesItem1Silver)
    }

    private fun setupTrophyGold() {
        arcViewGold.configureAngles(300, 0)

        arcViewGold.addSeries(
            SeriesItem.Builder(Color.argb(255, 218, 218, 218))
                .setRange(0f, 100f, 100f)
                .setInitialVisibility(true)
                .setLineWidth(32f)
                .build()
        )

        val seriesItem1Gold =
            SeriesItem.Builder(Color.argb(255, 64, 196, 0))
                .setRange(goal?.silverValue!!, goal?.goldValue!!, goal?.silverValue!!)
                .setLineWidth(32f)
                .build()

        series1IndexGold = arcViewGold.addSeries(seriesItem1Gold)
    }

    private fun setTrophiesValueInc() {
        if (cont >= 0) {
            when {
                cont <= goal_trophy_bronze_text.text.toString().toFloat() -> setTrophyBronzeValue()
                cont <= goal_trophy_silver_text.text.toString().toFloat() -> setTrophySilverValue()
                cont <= goal_trophy_gold_text.text.toString().toFloat() -> setTrophyGoldValue()
            }
        }
    }

    private fun setTrophiesValueDec() {
        if (cont >= 0) {
            when {
                cont < goal_trophy_bronze_text.text.toString().toFloat() -> setTrophyBronzeValue()
                cont < goal_trophy_silver_text.text.toString().toFloat() -> setTrophySilverValue()
                cont < goal_trophy_gold_text.text.toString().toFloat() -> setTrophyGoldValue()
            }
        }
    }

    private fun setTrophyBronzeValue() {
        arcViewBronze.addEvent(DecoEvent.Builder(cont).setIndex(series1IndexBronze).build())
    }

    private fun setTrophySilverValue() {
        arcViewSilver.addEvent(DecoEvent.Builder(cont).setIndex(series1IndexSilver).build())
    }

    private fun setTrophyGoldValue() {
        arcViewGold.addEvent(DecoEvent.Builder(cont).setIndex(series1IndexGold).build())
    }

    private fun setMedalValue() {
        arcViewMedal.addEvent(DecoEvent.Builder(cont).setIndex(series1IndexMedal).build())
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

        cont = goal?.actualValue!!

        goal_title.text = goal?.name
        goal_inc_dec_total.text = cont.toString()//goal?.medalValue.toString()
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

        setupPreMedalOrTrophies()
    }

    private fun setupPreMedalOrTrophies() {
        if (goal?.trophies!!) {
            when {
                goal?.actualValue!! <= goal?.bronzeValue!! -> {
                    arcViewBronze.addEvent(DecoEvent.Builder(goal?.actualValue!!).setIndex(series1IndexBronze).build())
                }
                goal?.actualValue!! <= goal?.silverValue!! -> {
                    arcViewBronze.addEvent(DecoEvent.Builder(100F).setIndex(series1IndexBronze).build())
                    arcViewSilver.addEvent(DecoEvent.Builder(goal?.actualValue!!).setIndex(series1IndexSilver).build())
                }
                goal?.actualValue!! <= goal?.goldValue!! -> {
                    arcViewBronze.addEvent(DecoEvent.Builder(100F).setIndex(series1IndexBronze).build())
                    arcViewSilver.addEvent(DecoEvent.Builder(200F).setIndex(series1IndexSilver).build())
                    arcViewGold.addEvent(DecoEvent.Builder(goal?.actualValue!!).setIndex(series1IndexGold).build())
                }
            }
        } else {
            arcViewMedal.addEvent(DecoEvent.Builder(goal?.actualValue!!).setIndex(series1IndexMedal).build())
        }
    }
}
