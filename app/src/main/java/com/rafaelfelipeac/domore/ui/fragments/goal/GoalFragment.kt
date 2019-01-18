package com.rafaelfelipeac.domore.ui.fragments.goal

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.*
import android.widget.ImageView
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

        if (goal?.trophies!!) {
            resetTrophyBronze()
            resetTrophySilver()
            resetTrophyGold()
        } else {
            resetMedal()
        }

        goal_btn_inc.setOnClickListener {
            cont += 10

            goal_inc_dec_total.text = cont.toString()

            saveAndUpdateGoal()
        }

        goal_btn_dec.setOnClickListener {
            cont -= 10

            goal_inc_dec_total.text = cont.toString()

            saveAndUpdateGoal()
        }

        goal_btn_save.setOnClickListener {
            if (goal_total_total.text.isNotEmpty()) {
                cont = goal_total_total.text.toString().toFloat()

                saveAndUpdateGoal()

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
                goal_inc_dec_total.text = cont.toString()
            }
            3 -> {
                goal_cl_total.visibility = View.VISIBLE
                goal_total_total.setText(goal?.actualValue.toString())
            }
        }

        medalOrTrophies()
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

    private fun saveAndUpdateGoal() {
        goal?.actualValue = cont
        goalDAO?.update(goal!!)

        if (goal?.trophies!!) {
            setTrophies()
        } else {
            setMedal()
        }
    }

    private fun medalOrTrophies() {
        if (cont < 0) return

        if (goal?.trophies!!) {
            setupTrophies()
        } else {
            setupMedal()
        }
    }

    private fun setupMedal() {
        when {
            goal?.actualValue!! == 0F -> {
                resetMedal()
            }
            goal?.actualValue!! <= goal?.medalValue!! -> {
                setupArcView(arcViewMedal, goal?.actualValue!!, seriesMedal)

                if (goal?.actualValue!! == goal?.medalValue!!)
                    changeMedalOrTrophyIcon(goal_medal_image, R.mipmap.ic_medal, R.mipmap.ic_medal_dark)
            }
            else -> {
                setupArcView(arcViewMedal, goal?.medalValue!!, seriesMedal)

                changeMedalOrTrophyIconDark(goal_medal_image, R.mipmap.ic_medal, R.mipmap.ic_medal_dark, false)
            }
        }
    }

    private fun setupTrophies() {
        when {
            goal?.actualValue!! == 0F -> {
                resetTrophyBronze()
            }
            goal?.actualValue!! <= goal?.bronzeValue!! -> {
                setupArcView(arcViewBronze, goal?.actualValue!!, seriesBronze)
            }
            goal?.actualValue!! <= goal?.silverValue!! -> {
                setupArcView(arcViewBronze, goal?.bronzeValue!!, seriesBronze)
                setupArcView(arcViewSilver, goal?.actualValue!!, seriesSilver)

                changeMedalOrTrophyIcon(goal_trophy_bronze_image, R.mipmap.ic_trophy_bronze, R.mipmap.ic_trophy_bronze_dark)
            }
            goal?.actualValue!! <= goal?.goldValue!! -> {
                setupArcView(arcViewBronze, goal?.bronzeValue!!, seriesBronze)
                setupArcView(arcViewSilver, goal?.silverValue!!, seriesSilver)
                setupArcView(arcViewGold, goal?.actualValue!!, seriesGold)

                changeMedalOrTrophyIcon(goal_trophy_bronze_image, R.mipmap.ic_trophy_bronze, R.mipmap.ic_trophy_bronze_dark)
                changeMedalOrTrophyIcon(goal_trophy_silver_image, R.mipmap.ic_trophy_silver, R.mipmap.ic_trophy_silver_dark)
            }
            else -> {
                setupArcView(arcViewBronze, goal?.bronzeValue!!, seriesBronze)
                setupArcView(arcViewSilver, goal?.silverValue!!, seriesSilver)
                setupArcView(arcViewGold, goal?.goldValue!!, seriesGold)

                changeMedalOrTrophyIcon(goal_trophy_bronze_image, R.mipmap.ic_trophy_bronze, R.mipmap.ic_trophy_bronze_dark)
                changeMedalOrTrophyIcon(goal_trophy_silver_image, R.mipmap.ic_trophy_silver, R.mipmap.ic_trophy_silver_dark)
                changeMedalOrTrophyIcon(goal_trophy_gold_image, R.mipmap.ic_trophy_gold, R.mipmap.ic_trophy_gold_dark)
            }
        }
    }

    private fun setMedal() {
        medalSituations()
    }

    private fun setTrophies() {
        changeIconsDark()

        trophiesSituations()
    }

    private fun medalSituations() {
        if (cont > 0 && cont <= goal?.medalValue!!) {
            setMedalValue()

            if (cont == goal?.medalValue!!)
                changeMedalOrTrophyIcon(goal_medal_image, R.mipmap.ic_medal, R.mipmap.ic_medal_dark)
            else
                changeMedalOrTrophyIconDark(goal_medal_image, R.mipmap.ic_medal, R.mipmap.ic_medal_dark, true)
        } else if (cont > goal?.medalValue!!) {
            medalOrTrophies()
        } else if (cont == 0F) {
            resetMedal()
        }
    }

    private fun trophiesSituations() {
        if (cont == 0F) {
            resetTrophyBronze()
            resetTrophySilver()
            resetTrophyGold()
        } else if (cont > 0) {
            if (cont <= goal?.bronzeValue!!) {
                setTrophyBronzeValue()

                if (cont == goal?.bronzeValue!!)
                    changeMedalOrTrophyIcon(goal_trophy_bronze_image, R.mipmap.ic_trophy_bronze, R.mipmap.ic_trophy_bronze_dark)

                if (cont < goal?.silverValue!!)
                    resetTrophySilver()
            } else if (cont <= goal?.silverValue!!) {
                setTrophySilverValue()

                if (cont == goal?.silverValue!!)
                    changeMedalOrTrophyIcon(goal_trophy_silver_image, R.mipmap.ic_trophy_silver, R.mipmap.ic_trophy_silver_dark)

                if (cont < goal?.goldValue!!)
                    resetTrophyGold()
            } else if (cont <= goal?.goldValue!!) {
                setTrophyGoldValue()

                if (cont == goal?.goldValue!!)
                    changeMedalOrTrophyIcon(goal_trophy_gold_image, R.mipmap.ic_trophy_gold, R.mipmap.ic_trophy_gold_dark)
            }
        }
    }

    private fun resetMedal() {
        arcViewMedal.configureAngles(300, 0)

        seriesItemMedal = setupArcViewAndSeriesItem(arcViewMedal, 0F,
            goal?.medalValue!!, 0F, false)

        seriesMedal = arcViewMedal.addSeries(seriesItemMedal!!)
    }

    private fun resetTrophyBronze() {
        arcViewBronze.configureAngles(300, 0)

        seriesItemBronze = setupArcViewAndSeriesItem(arcViewBronze, 0F,
            goal?.bronzeValue!!, 0F, false)

        seriesBronze = arcViewBronze.addSeries(seriesItemBronze!!)
    }

    private fun resetTrophySilver() {
        arcViewSilver.configureAngles(300, 0)

        seriesItemSilver = setupArcViewAndSeriesItem(arcViewSilver, goal?.bronzeValue!!,
            goal?.silverValue!!, goal?.bronzeValue!!, false)

        seriesSilver = arcViewSilver.addSeries(seriesItemSilver!!)
    }

    private fun resetTrophyGold() {
        arcViewGold.configureAngles(300, 0)

        seriesItemGold = setupArcViewAndSeriesItem(arcViewGold, goal?.silverValue!!,
            goal?.goldValue!!, goal?.silverValue!!, false)

        seriesGold = arcViewGold.addSeries(seriesItemGold!!)
    }


    private fun setTrophyBronzeValue() = setupArcView(arcViewBronze, cont, seriesBronze)

    private fun setTrophySilverValue() = setupArcView(arcViewSilver, cont, seriesSilver)

    private fun setTrophyGoldValue() = setupArcView(arcViewGold, cont, seriesGold)

    private fun setMedalValue() = setupArcView(arcViewMedal, cont, seriesMedal)

    private fun setupArcViewAndSeriesItem(arcView: DecoView, minValue: Float, maxValue: Float, initialValue: Float, visibility: Boolean): SeriesItem {
        arcView.addSeries(
            SeriesItem.Builder(ContextCompat.getColor(context!!, R.color.colorPrimaryAnother))
                .setRange(0F, 100F, 100F)
                .setInitialVisibility(true)
                .setLineWidth(lineWidth + 4F)
                .build()
        )

        return SeriesItem.Builder(ContextCompat.getColor(context!!, R.color.colorPrimary))
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

    private fun changeIconsDark() {
        if (cont < goal?.bronzeValue!!)
            changeMedalOrTrophyIconDark(
                goal_trophy_bronze_image,
                R.mipmap.ic_trophy_bronze,
                R.mipmap.ic_trophy_bronze_dark,
                true)
        if (cont < goal?.silverValue!!)
            changeMedalOrTrophyIconDark(
                goal_trophy_silver_image,
                R.mipmap.ic_trophy_silver,
                R.mipmap.ic_trophy_silver_dark,
                true)
        if (cont < goal?.goldValue!!)
            changeMedalOrTrophyIconDark(
                goal_trophy_gold_image,
                R.mipmap.ic_trophy_gold,
                R.mipmap.ic_trophy_gold_dark,
                true)
    }

    private fun changeMedalOrTrophyIconDark(image: ImageView, iconNormal: Int, iconDark: Int, dark: Boolean) {
        if (dark)
            image.background = ContextCompat.getDrawable(context!!, iconDark)
        else
            image.background = ContextCompat.getDrawable(context!!, iconNormal)
    }

    private fun changeMedalOrTrophyIcon(image: ImageView, iconNormal: Int, iconDark: Int) {
        if (image.background == ContextCompat.getDrawable(context!!, iconNormal))
            image.background = ContextCompat.getDrawable(context!!, iconDark)
        else
            image.background = ContextCompat.getDrawable(context!!, iconNormal)
    }
}
