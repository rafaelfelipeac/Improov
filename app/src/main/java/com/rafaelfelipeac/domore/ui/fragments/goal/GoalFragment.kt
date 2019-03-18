package com.rafaelfelipeac.domore.ui.fragments.goal

import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.textfield.TextInputEditText
import com.hookedonplay.decoviewlib.DecoView
import com.hookedonplay.decoviewlib.charts.SeriesItem
import com.hookedonplay.decoviewlib.events.DecoEvent
import com.rafaelfelipeac.domore.R
import com.rafaelfelipeac.domore.app.App
import com.rafaelfelipeac.domore.extension.getNumberInRightFormat
import com.rafaelfelipeac.domore.models.Goal
import com.rafaelfelipeac.domore.models.Historic
import com.rafaelfelipeac.domore.models.Item
import com.rafaelfelipeac.domore.ui.activities.MainActivity
import com.rafaelfelipeac.domore.ui.adapter.HistoricAdapter
import com.rafaelfelipeac.domore.ui.adapter.ItemsAdapter
import com.rafaelfelipeac.domore.ui.base.BaseFragment
import com.rafaelfelipeac.domore.ui.helper.SwipeAndDragHelperItem
import kotlinx.android.synthetic.main.fragment_goal.*
import java.util.*

class GoalFragment : BaseFragment() {

    private var itemsAdapter = ItemsAdapter(this)
    private var historicAdapter = HistoricAdapter(this)

    var goal: Goal? = null

    private var count: Float = 0F

    private var seriesMedal: Int = 0
    private var seriesBronze: Int = 0
    private var seriesSilver: Int = 0
    private var seriesGold: Int = 0

    private var durationAnimation = 75L
    private var lineWidth = 32F

    private var sheetBehavior: BottomSheetBehavior<*>? = null
    private var bottomSheetItemButtonClose: ImageView? = null
    private var bottomSheetItemButtonSave: Button? = null
    private var bottomSheetItemName: TextInputEditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        injector.inject(this)

        goal = GoalFragmentArgs.fromBundle(arguments!!).goal

        setHasOptionsMenu(true)

        initElements()
        setBottomSheet()

        (activity as MainActivity).bottomNavigationVisible(false)
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

        if (goal?.trophies!!) {
            resetTrophies()
        } else {
            resetMedal()
        }

        setButtons()
        setupItems()

        if (goal?.type != 1) {
            setHistoricItems()
        }
    }

    private fun setButtons() {
        goal_btn_inc.setOnClickListener {
            count += goal?.incrementValue!!
            updateTextAndGoal(goal_inc_dec_total)

            historicDAO?.insert(Historic(value = goal?.incrementValue!!, date = Date(), goalId = goal?.goalId!!))

            setHistoricItems()
        }

        goal_btn_dec.setOnClickListener {
            count -= goal?.decrementValue!!
            updateTextAndGoal(goal_inc_dec_total)

            historicDAO?.insert(Historic(value = goal?.decrementValue!! * -1, date = Date(), goalId = goal?.goalId!!))

            setHistoricItems()
        }

        goal_btn_save.setOnClickListener {
            if (goal_total_total.text?.isNotEmpty()!!) {
                count = goal?.value!! + goal_total_total.text.toString().toFloat()
                updateTextAndGoal(goal_count)

                historicDAO?.insert(Historic(value = goal_total_total.text.toString().toFloat(),
                    date = Date(), goalId = goal?.goalId!!))

                goal_total_total.setText("")

                val oldDone = goal!!.done
                goal!!.done = verifyIfIsDone()
                goalDAO?.update(goal!!)

                if (!oldDone && goal!!.done)
                    showSnackBar(getString(R.string.message_goal_done))
                else
                    showSnackBar(getString(R.string.message_goal_value_updated))

                setHistoricItems()
            } else {
                showSnackBar(getString(R.string.message_goal_value_invalid))
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        if (goal?.type == 1) { inflater.inflate(R.menu.menu_add, menu) }

        inflater.inflate(R.menu.menu_edit, menu)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_goal_add -> {
                (activity as MainActivity).openBottomSheetAddItem(null)

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

    private fun verifyIfIsDone() =
        ((goal!!.trophies && goal!!.value >= goal!!.goldValue) ||
                (!goal!!.trophies && goal!!.value >= goal!!.medalValue))

    private fun updateTextAndGoal(textView: TextView) {
        goal_count.text = count.getNumberInRightFormat()
        textView.text = count.getNumberInRightFormat()

        updateGoal()
    }

    private fun setBottomSheet() {
        (activity as MainActivity).closeBottomSheetDoneGoal()

        val act = activity

        sheetBehavior?.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(view: View, status: Int) {
                when (status) {
                    BottomSheetBehavior.STATE_HIDDEN -> { }
                    BottomSheetBehavior.STATE_EXPANDED -> { }
                    BottomSheetBehavior.STATE_HALF_EXPANDED -> { }
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        hideSoftKeyboard(view, act)
                    }
                    BottomSheetBehavior.STATE_DRAGGING -> { }
                    BottomSheetBehavior.STATE_SETTLING -> { }
                }
            }

            override fun onSlide(view: View, statusSlide: Float) {}
        })

        bottomSheetItemButtonClose?.setOnClickListener {
            bottomSheetItemName?.setText("")
            (activity as MainActivity).closeBottomSheetAddItem()
        }

        bottomSheetItemButtonSave?.setOnClickListener {
            if (bottomSheetItemName?.text.toString().isEmpty())
                showSnackBar("Nome do item está vazio.")
            else {
                val order =
                    if (itemDAO?.getAll()?.none { it.goalId != 0L }!!) { 1 }
                    else { itemDAO?.getAll()?.filter { it.goalId == goal?.goalId }?.size!! + 1 }

                val item: Item?

                if ((activity as MainActivity).item == null) {
                    item = Item( goalId = goal?.goalId!!,
                        name = bottomSheetItemName?.text.toString(),
                        done = false,
                        order = order,
                        createdDate = getCurrentTime()
                    )

                    itemDAO?.insert(item)
                } else {
                    item = (activity as MainActivity).item
                    item?.name = bottomSheetItemName?.text.toString()
                    item?.updatedDate = getCurrentTime()

                    itemDAO?.update(item!!)
                }

                setupItems()

                bottomSheetItemName?.setText("")
                (activity as MainActivity).closeBottomSheetAddItem()
            }
        }
    }

    private fun initElements() {
        sheetBehavior = (activity as MainActivity).bottomSheetItem
        bottomSheetItemButtonClose = (activity as MainActivity).bottomSheetItemClose
        bottomSheetItemButtonSave = (activity as MainActivity).bottomSheetItemSave
        bottomSheetItemName = (activity as MainActivity).bottomSheetItemName
    }

    fun scoreFromList(done: Boolean) {
        setupItems()

        if (done) count++ else count--

        goal_count.text = count.getNumberInRightFormat()

        updateGoal()
    }

    private fun setupGoal() {
        count = goal?.value!!

        goal_title.text = goal?.name
        goal_count.text = count.getNumberInRightFormat()

        when {
            goal?.trophies!! -> {
                goal_medal.visibility = View.INVISIBLE
                goal_trophies.visibility = View.VISIBLE

                goal_trophy_bronze_text.text = goal?.bronzeValue?.getNumberInRightFormat()
                goal_trophy_silver_text.text = goal?.silverValue?.getNumberInRightFormat()
                goal_trophy_gold_text.text = goal?.goldValue?.getNumberInRightFormat()
            }
            else -> goal_medal_text.text = goal?.medalValue?.getNumberInRightFormat()
        }

        when (goal?.type) {
            1 -> {
                goal_cl_list.visibility = View.VISIBLE
                goal_cl_dec_inc.visibility = View.INVISIBLE
                goal_cl_total.visibility = View.INVISIBLE

                if ((activity as MainActivity).toolbar.menu.findItem(R.id.menu_goal_add) == null)
                    (activity as MainActivity).toolbar.inflateMenu(R.menu.menu_add)
            }
            2 -> {
                goal_cl_list.visibility = View.INVISIBLE
                goal_cl_dec_inc.visibility = View.VISIBLE
                goal_cl_total.visibility = View.INVISIBLE

                goal_inc_dec_total.text = count.getNumberInRightFormat()
            }
            3 -> {
                goal_cl_list.visibility = View.INVISIBLE
                goal_cl_dec_inc.visibility = View.INVISIBLE
                goal_cl_total.visibility = View.VISIBLE
            }
        }

        medalOrTrophies()
    }

    fun setupItems() {
        if (goal?.type == 1) {
            if (itemDAO?.getAll()?.any { it.goalId == goal?.goalId && it.goalId != 0L }!!) {
                setItems()

                goal_cl_list.visibility = View.VISIBLE
                items_placeholder.visibility = View.INVISIBLE
            } else {
                goal_cl_list.visibility = View.INVISIBLE
                items_placeholder.visibility = View.VISIBLE
            }
        }
    }

    private fun setItems() {
        val itemsList = itemDAO?.getAll()

        itemsAdapter.setItems(itemsList
            ?.filter { it.goalId == goal?.goalId && it.goalId != 0L }
            ?.sortedBy { it.order }!!)

        itemsAdapter.clickListener = { (activity as MainActivity).openBottomSheetAddItem(it) }

        val swipeAndDragHelper = SwipeAndDragHelperItem(itemsAdapter)
        val touchHelper = ItemTouchHelper(swipeAndDragHelper)

        itemsAdapter.touchHelper = touchHelper

        goal_items_list.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        goal_items_list.adapter = itemsAdapter

        touchHelper.attachToRecyclerView(goal_items_list)
    }

    private fun setHistoricItems() {
        val historicItems = historicDAO?.getAll()

        historicAdapter.setItems(historicItems!!.filter { it.goalId == goal?.goalId }. reversed())

        historic_items_list.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        historic_items_list.adapter = historicAdapter
    }

    private fun updateGoal() {
        goal?.value = count
        goal?.done = verifyIfIsDone()

        goalDAO?.update(goal!!)

        if (goal?.trophies!!) {
            setTrophies()
        } else {
            setMedal()
        }
    }

    private fun medalOrTrophies() {
        if (count < 0) return

        if (goal?.trophies!!) {
            setupTrophies()
        } else {
            setupMedal()
        }
    }

    private fun setupMedal() {
        when {
            goal?.value!! == 0F -> {
                resetMedal()
            }
            goal?.value!! <= goal?.medalValue!! -> {
                setupArcView(arcViewMedal, goal?.value!!, seriesMedal)

                if (goal?.value!! == goal?.medalValue!!)
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
            goal?.value!! == 0F -> {
                resetTrophyBronze()
            }
            goal?.value!! <= goal?.bronzeValue!! -> {
                setupArcView(arcViewBronze, goal?.value!!, seriesBronze)
            }
            goal?.value!! <= goal?.silverValue!! -> {
                setupArcView(arcViewBronze, goal?.bronzeValue!!, seriesBronze)
                setupArcView(arcViewSilver, goal?.value!!, seriesSilver)

                changeMedalOrTrophyIcon(goal_trophy_bronze_image, R.mipmap.ic_trophy_bronze, R.mipmap.ic_trophy_bronze_dark)
            }
            goal?.value!! <= goal?.goldValue!! -> {
                setupArcView(arcViewBronze, goal?.bronzeValue!!, seriesBronze)
                setupArcView(arcViewSilver, goal?.silverValue!!, seriesSilver)
                setupArcView(arcViewGold, goal?.value!!, seriesGold)

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
        if (count > 0 && count <= goal?.medalValue!!) {
            setMedalValue()

            if (count == goal?.medalValue!!)
                changeMedalOrTrophyIcon(goal_medal_image, R.mipmap.ic_medal, R.mipmap.ic_medal_dark)
            else
                changeMedalOrTrophyIconDark(goal_medal_image, R.mipmap.ic_medal, R.mipmap.ic_medal_dark, true)
        } else if (count > goal?.medalValue!!) { medalOrTrophies()
        } else if (count == 0F) { resetMedal() }
    }

    private fun trophiesSituations() {
        if (count == 0F) {
            resetTrophies()
        } else if (count > 0) {
            if (count <= goal?.bronzeValue!!) {
                setTrophyBronzeValue()

                if (count == goal?.bronzeValue!!)
                    changeMedalOrTrophyIcon(goal_trophy_bronze_image, R.mipmap.ic_trophy_bronze, R.mipmap.ic_trophy_bronze_dark)

                if (count < goal?.silverValue!!)
                    resetTrophySilver()
            } else if (count <= goal?.silverValue!!) {
                setTrophySilverValue()

                if (count == goal?.silverValue!!)
                    changeMedalOrTrophyIcon(goal_trophy_silver_image, R.mipmap.ic_trophy_silver, R.mipmap.ic_trophy_silver_dark)

                if (count < goal?.goldValue!!)
                    resetTrophyGold()
            } else if (count <= goal?.goldValue!!) {
                setTrophyGoldValue()

                if (count == goal?.goldValue!!)
                    changeMedalOrTrophyIcon(goal_trophy_gold_image, R.mipmap.ic_trophy_gold, R.mipmap.ic_trophy_gold_dark)
            }
        }
    }

    private fun resetTrophies() {
        resetTrophyBronze()
        resetTrophySilver()
        resetTrophyGold()
    }

    private fun resetTrophyBronze() {
        seriesBronze = resetMedalOrTrophy(
            arcViewBronze, 0F,
            goal?.bronzeValue!!, 0F)
    }

    private fun resetTrophySilver() {
        seriesSilver = resetMedalOrTrophy(
            arcViewSilver, goal?.bronzeValue!!,
            goal?.silverValue!!, goal?.bronzeValue!!)
    }

    private fun resetTrophyGold() {
        seriesGold = resetMedalOrTrophy(
            arcViewGold, goal?.silverValue!!,
            goal?.goldValue!!, goal?.silverValue!!)
    }

    private fun resetMedal() {
        seriesMedal = resetMedalOrTrophy(
            arcViewMedal, 0F,
            goal?.medalValue!!, 0F)
    }

    private fun resetMedalOrTrophy(arcView: DecoView, minValue: Float, maxValue: Float, initialValue: Float) : Int {
        arcView.configureAngles(300, 0)
        return arcView.addSeries(setupArcViewAndSeriesItem(arcView, minValue, maxValue, initialValue, false))
    }

    private fun setTrophyBronzeValue() = setupArcView(arcViewBronze, count, seriesBronze)

    private fun setTrophySilverValue() = setupArcView(arcViewSilver, count, seriesSilver)

    private fun setTrophyGoldValue() = setupArcView(arcViewGold, count, seriesGold)

    private fun setMedalValue() = setupArcView(arcViewMedal, count, seriesMedal)

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
        if (count < goal?.bronzeValue!!)
            changeMedalOrTrophyIconDark(
                goal_trophy_bronze_image,
                R.mipmap.ic_trophy_bronze,
                R.mipmap.ic_trophy_bronze_dark,
                true)
        if (count < goal?.silverValue!!)
            changeMedalOrTrophyIconDark(
                goal_trophy_silver_image,
                R.mipmap.ic_trophy_silver,
                R.mipmap.ic_trophy_silver_dark,
                true)
        if (count < goal?.goldValue!!)
            changeMedalOrTrophyIconDark(
                goal_trophy_gold_image,
                R.mipmap.ic_trophy_gold,
                R.mipmap.ic_trophy_gold_dark,
                true)
    }

    private fun changeMedalOrTrophyIconDark(image: ImageView, iconNormal: Int, iconDark: Int, dark: Boolean) {
        image.background =
            if (dark) ContextCompat.getDrawable(context!!, iconDark)
            else ContextCompat.getDrawable(context!!, iconNormal)
    }

    private fun changeMedalOrTrophyIcon(image: ImageView, iconNormal: Int, iconDark: Int) {
        image.background =
            if (image.background == ContextCompat.getDrawable(context!!, iconNormal)) ContextCompat.getDrawable(context!!, iconDark)
            else ContextCompat.getDrawable(context!!, iconNormal)
    }

    fun onViewMoved(oldPosition: Int, newPosition: Int, items: MutableList<Item>,
                    function: (oldPosition: Int, newPosition: Int) -> Unit) {
        val targetItem = items[oldPosition]
        val otherItem = items[newPosition]

        targetItem.order = newPosition
        otherItem.order = oldPosition

        App.database?.itemDAO()?.update(targetItem)
        App.database?.itemDAO()?.update(otherItem)

        items.removeAt(oldPosition)
        items.add(newPosition, targetItem)

        function(oldPosition, newPosition)
    }

    fun onViewSwiped(position: Int, direction: Int, holder: RecyclerView.ViewHolder, items: MutableList<Item>) {
        val item = items[position]

        when(direction) {
            ItemTouchHelper.RIGHT -> {
                if (item.done) {
                    item.done = false
                    item.undoneDate = getCurrentTime()

                    itemDAO?.update(item)

                    scoreFromList(false)
                } else {
                    item.done = true
                    item.doneDate = getCurrentTime()

                    itemDAO?.update(item)

                    scoreFromList(true)
                }
            }
            ItemTouchHelper.LEFT -> {
                item.deleteDate = getCurrentTime()

                itemDAO?.delete(item)

                showSnackBarWithAction(holder.itemView, "Item removido.", item, ::deleteItem)

                setupItems()
            }
        }
    }

    private fun deleteItem(item: Any) {
        itemDAO?.insert(item as Item)
        setupItems()
    }
}
