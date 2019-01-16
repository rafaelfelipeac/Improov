package com.rafaelfelipeac.domore.ui.fragments.goal

import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.*
import com.hookedonplay.decoviewlib.DecoView
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

    private var seriesMedal: Int = 0
    private var seriesBronze: Int = 0
    private var seriesSilver: Int = 0
    private var seriesGold: Int = 0

    private var seriesItemMedal: SeriesItem? = null
    private var seriesItemBronze: SeriesItem? = null
    private var seriesItemSilver: SeriesItem? = null
    private var seriesItemGold: SeriesItem? = null

    private var durationAnimation = 75L
    private var lineWidth = 32F

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

            if (!goal?.trophies!!) {
                setMedalValueIncDec()
            } else {
                setTrophiesValueInc()
            }
        }

        goal_btn_dec.setOnClickListener {
            cont -= 10

            goal_inc_dec_total.text = cont.toString()
            goal?.actualValue = cont
            goalDAO?.update(goal!!)

            if (!goal?.trophies!!) {
                setMedalValueIncDec()
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
        } else {
            goal_medal_text.text = goal?.medalValue.toString()
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

    private fun setupPreMedalOrTrophies() {
        if (goal?.trophies!!) {
            when {
                goal?.actualValue!! == 0F -> {
                    setupTrophyBronze()
                }
                goal?.actualValue!! <= goal?.bronzeValue!! -> {
                    setupArcView(arcViewBronze, goal?.actualValue!!, seriesBronze)
                }
                goal?.actualValue!! <= goal?.silverValue!! -> {
                    setupArcView(arcViewBronze, goal?.bronzeValue!!, seriesBronze)
                    setupArcView(arcViewSilver, goal?.actualValue!!, seriesSilver)
                }
                goal?.actualValue!! <= goal?.goldValue!! -> {
                    setupArcView(arcViewBronze, goal?.bronzeValue!!, seriesBronze)
                    setupArcView(arcViewSilver, goal?.silverValue!!, seriesSilver)
                    setupArcView(arcViewGold, goal?.actualValue!!, seriesGold)
                }
                else -> {
                    setupArcView(arcViewBronze, goal?.bronzeValue!!, seriesBronze)
                    setupArcView(arcViewSilver, goal?.silverValue!!, seriesSilver)
                    setupArcView(arcViewGold, goal?.goldValue!!, seriesGold)
                }
            }
        } else {
            when {
                goal?.actualValue!! == 0F -> {
                    setupMedal()
                }
                goal?.actualValue!! <= goal?.medalValue!! -> {
                    setupArcView(arcViewMedal, goal?.actualValue!!, seriesMedal)
                }
                else -> {
                    setupArcView(arcViewMedal, goal?.medalValue!!, seriesMedal)
                }
            }
        }
    }

    private fun setupMedal() {
        arcViewMedal.configureAngles(300, 0)

        setupArcViewBuilder(arcViewMedal)

        seriesItemMedal = setupSeriesItemBuilder(0F, goal?.medalValue!!, 0F, false)

        seriesMedal = arcViewMedal.addSeries(seriesItemMedal!!)
    }

    private fun setupTrophyBronze() {
        arcViewBronze.configureAngles(300, 0)

        setupArcViewBuilder(arcViewBronze)

        seriesItemBronze = setupSeriesItemBuilder(0F, goal?.bronzeValue!!, 0F, false)

        seriesBronze = arcViewBronze.addSeries(seriesItemBronze!!)
    }

    private fun setupTrophySilver() {
        arcViewSilver.configureAngles(300, 0)

        setupArcViewBuilder(arcViewSilver)

        seriesItemSilver = setupSeriesItemBuilder(goal?.bronzeValue!!, goal?.silverValue!!, goal?.bronzeValue!!, false)

        seriesSilver = arcViewSilver.addSeries(seriesItemSilver!!)
    }

    private fun setupTrophyGold() {
        arcViewGold.configureAngles(300, 0)

        setupArcViewBuilder(arcViewGold)

        seriesItemGold = setupSeriesItemBuilder(goal?.silverValue!!, goal?.goldValue!!, goal?.silverValue!!, false)

        seriesGold = arcViewGold.addSeries(seriesItemGold!!)
    }

    private fun setMedalValueIncDec() {
        if (cont > 0 && cont <= goal?.medalValue!!) {
            setMedalValue()
        } else if (cont == 0F) {
            setupMedal()
        }
    }

    private fun setTrophiesValueInc() {
        if (cont > 0) {
            when {
                cont <= goal_trophy_bronze_text.text.toString().toFloat() -> setTrophyBronzeValue()
                cont <= goal_trophy_silver_text.text.toString().toFloat() -> setTrophySilverValue()
                cont <= goal_trophy_gold_text.text.toString().toFloat() -> setTrophyGoldValue()
            }
        } else if (cont == 0F) {
            setupTrophyBronze()
        }
    }

    private fun setTrophiesValueDec() {
        if (cont > 0) {
            when {
                cont <= goal_trophy_bronze_text.text.toString().toFloat() -> {
                    setTrophyBronzeValue()

                    setupTrophySilver()
                }
                cont <= goal_trophy_silver_text.text.toString().toFloat() -> {
                    setTrophySilverValue()

                    setupTrophyGold()
                }
                cont <= goal_trophy_gold_text.text.toString().toFloat() -> setTrophyGoldValue()
            }
        } else if (cont == 0F) {
            setupTrophyBronze()
        }
    }

    private fun setTrophyBronzeValue() = setupArcView(arcViewBronze, cont, seriesBronze)

    private fun setTrophySilverValue() = setupArcView(arcViewSilver, cont, seriesSilver)

    private fun setTrophyGoldValue() = setupArcView(arcViewGold, cont, seriesGold)

    private fun setMedalValue() = setupArcView(arcViewMedal, cont, seriesMedal)

    private fun setupArcViewBuilder(arcView: DecoView) {
        arcView.addSeries(
            SeriesItem.Builder(Color.argb(255, 218, 218, 218))
                .setRange(0F, 100F, 100F)
                .setInitialVisibility(true)
                .setLineWidth(lineWidth)
                .build()
        )
    }

    private fun setupSeriesItemBuilder(minValue: Float, maxValue: Float, initialValue: Float, visibility: Boolean): SeriesItem {
        return SeriesItem.Builder(Color.argb(255, 64, 196, 0))
            .setRange(minValue, maxValue, initialValue)
            .setInitialVisibility(visibility)
            .setLineWidth(lineWidth)
            .build()

    }

    private fun setupArcView(arcView: DecoView, value: Float, index: Int) {
        arcView.addEvent(
            DecoEvent.Builder(value)
                .setIndex(index)
                .setDuration(durationAnimation)
                .build()
        )
    }
}
