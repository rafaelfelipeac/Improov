package com.rafaelfelipeac.mountains.ui.fragments.goal

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
import com.rafaelfelipeac.mountains.R
import com.rafaelfelipeac.mountains.app.App
import com.rafaelfelipeac.mountains.extension.getNumberInRightFormat
import com.rafaelfelipeac.mountains.models.Goal
import com.rafaelfelipeac.mountains.models.Historic
import com.rafaelfelipeac.mountains.models.Item
import com.rafaelfelipeac.mountains.ui.activities.MainActivity
import com.rafaelfelipeac.mountains.ui.adapter.HistoricAdapter
import com.rafaelfelipeac.mountains.ui.adapter.ItemsAdapter
import com.rafaelfelipeac.mountains.ui.base.BaseFragment
import com.rafaelfelipeac.mountains.ui.helper.SwipeAndDragHelperItem
import kotlinx.android.synthetic.main.fragment_goal.*
import java.util.*

class GoalFragment : BaseFragment() {

    private var itemsAdapter = ItemsAdapter(this)
    private var historicAdapter = HistoricAdapter(this)

    var goal: Goal? = null

    private var count: Float = 0F

    private var seriesSingle: Int = 0
    private var seriesBronze: Int = 0
    private var seriesSilver: Int = 0
    private var seriesGold: Int = 0

    private var durationAnimation = 75L
    private var lineWidth = 32F

    private var bottomSheetItem: BottomSheetBehavior<*>? = null
    private var bottomSheetItemClose: ImageView? = null
    private var bottomSheetItemSave: Button? = null
    private var bottomSheetItemName: TextInputEditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        injector.inject(this)

        goal = GoalFragmentArgs.fromBundle(arguments!!).goal

        setHasOptionsMenu(true)

        setupBottomSheet()

        (activity as MainActivity).bottomNavigationVisible(View.GONE)
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

        if (goal?.mountains!!) {
            resetMountains()
        } else {
            resetSingle()
        }

        setButtons()
        setupItems()

        if (goal?.type != 1) {
            setHistoricItems()
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
                (activity as MainActivity).openBottomSheetItem(null)

                return true
            }
            R.id.menu_goal_edit -> {
                val action =
                    GoalFragmentDirections.actionGoalFragmentToGoalFormFragment(goal!!)
                navController.navigate(action)
            }
            android.R.id.home -> {
                navController.navigateUp()
            }
        }
        return false
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

    private fun verifyIfIsDone() =
        ((goal!!.mountains && goal!!.value >= goal!!.goldValue) ||
                (!goal!!.mountains && goal!!.value >= goal!!.singleValue))

    private fun updateTextAndGoal(textView: TextView) {
        goal_count.text = count.getNumberInRightFormat()
        textView.text = count.getNumberInRightFormat()

        updateGoal()
    }

    private fun setupBottomSheet() {
        (activity as MainActivity).closeBottomSheetDoneGoal()

        bottomSheetItem = (activity as MainActivity).bottomSheetItem
        bottomSheetItemClose = (activity as MainActivity).bottomSheetItemClose
        bottomSheetItemSave = (activity as MainActivity).bottomSheetItemSave
        bottomSheetItemName = (activity as MainActivity).bottomSheetItemName

        bottomSheetItem?.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(view: View, status: Int) {
                when (status) {
                    BottomSheetBehavior.STATE_HIDDEN -> { }
                    BottomSheetBehavior.STATE_EXPANDED -> { }
                    BottomSheetBehavior.STATE_HALF_EXPANDED -> { }
                    BottomSheetBehavior.STATE_COLLAPSED -> { hideSoftKeyboard(view, activity) }
                    BottomSheetBehavior.STATE_DRAGGING -> { }
                    BottomSheetBehavior.STATE_SETTLING -> { }
                }
            }

            override fun onSlide(view: View, statusSlide: Float) {}
        })

        bottomSheetItemClose?.setOnClickListener {
            bottomSheetItemName?.setText("")
            (activity as MainActivity).closeBottomSheetItem()
        }

        bottomSheetItemSave?.setOnClickListener {
            if (bottomSheetItemName?.text.toString().isEmpty())
                showSnackBar(getString(R.string.bottom_sheet_empty_item_name))
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
                (activity as MainActivity).closeBottomSheetItem()
            }
        }
    }

    private fun scoreFromList(done: Boolean) {
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
            goal?.mountains!! -> {
                goal_single.visibility = View.INVISIBLE
                goal_mountains.visibility = View.VISIBLE

                goal_mountain_bronze_text.text = goal?.bronzeValue?.getNumberInRightFormat()
                goal_mountain_silver_text.text = goal?.silverValue?.getNumberInRightFormat()
                goal_mountain_gold_text.text = goal?.goldValue?.getNumberInRightFormat()
            }
            else -> goal_single_text.text = goal?.singleValue?.getNumberInRightFormat()
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

        singleOrMountains()
    }

    private fun setupItems() {
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

        itemsAdapter.clickListener = { (activity as MainActivity).openBottomSheetItem(it) }

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

        if (goal?.mountains!!) {
            setMountains()
        } else {
            setSingle()
        }
    }

    private fun singleOrMountains() {
        if (count < 0) return

        if (goal?.mountains!!) {
            setupMountains()
        } else {
            setupSingle()
        }
    }

    private fun setupSingle() {
        when {
            goal?.value!! == 0F -> {
                resetSingle()
            }
            goal?.value!! <= goal?.singleValue!! -> {
                setupArcView(arcViewSingle, goal?.value!!, seriesSingle)

                if (goal?.value!! == goal?.singleValue!!)
                    changeSingleOrMountainsIcon(goal_single_image, R.mipmap.ic_single, R.mipmap.ic_single_dark)
            }
            else -> {
                setupArcView(arcViewSingle, goal?.singleValue!!, seriesSingle)

                changeSingleOrMountainsIconDark(goal_single_image, R.mipmap.ic_single, R.mipmap.ic_single_dark, false)
            }
        }
    }

    private fun setupMountains() {
        when {
            goal?.value!! == 0F -> {
                resetMountainBronze()
            }
            goal?.value!! <= goal?.bronzeValue!! -> {
                setupArcView(arcViewBronze, goal?.value!!, seriesBronze)
            }
            goal?.value!! <= goal?.silverValue!! -> {
                setupArcView(arcViewBronze, goal?.bronzeValue!!, seriesBronze)
                setupArcView(arcViewSilver, goal?.value!!, seriesSilver)

                changeSingleOrMountainsIcon(goal_mountain_bronze_image, R.mipmap.ic_mountain_bronze, R.mipmap.ic_mountain_bronze_dark)
            }
            goal?.value!! <= goal?.goldValue!! -> {
                setupArcView(arcViewBronze, goal?.bronzeValue!!, seriesBronze)
                setupArcView(arcViewSilver, goal?.silverValue!!, seriesSilver)
                setupArcView(arcViewGold, goal?.value!!, seriesGold)

                changeSingleOrMountainsIcon(goal_mountain_bronze_image, R.mipmap.ic_mountain_bronze, R.mipmap.ic_mountain_bronze_dark)
                changeSingleOrMountainsIcon(goal_mountain_silver_image, R.mipmap.ic_mountain_silver, R.mipmap.ic_mountain_silver_dark)
            }
            else -> {
                setupArcView(arcViewBronze, goal?.bronzeValue!!, seriesBronze)
                setupArcView(arcViewSilver, goal?.silverValue!!, seriesSilver)
                setupArcView(arcViewGold, goal?.goldValue!!, seriesGold)

                changeSingleOrMountainsIcon(goal_mountain_bronze_image, R.mipmap.ic_mountain_bronze, R.mipmap.ic_mountain_bronze_dark)
                changeSingleOrMountainsIcon(goal_mountain_silver_image, R.mipmap.ic_mountain_silver, R.mipmap.ic_mountain_silver_dark)
                changeSingleOrMountainsIcon(goal_mountain_gold_image, R.mipmap.ic_mountain_gold, R.mipmap.ic_mountain_gold_dark)
            }
        }
    }

    private fun setSingle() {
        singleSituations()
    }

    private fun setMountains() {
        changeIconsDark()

        mountainsSituations()
    }

    private fun singleSituations() {
        if (count > 0 && count <= goal?.singleValue!!) {
            setSingleValue()

            if (count == goal?.singleValue!!)
                changeSingleOrMountainsIcon(goal_single_image, R.mipmap.ic_single, R.mipmap.ic_single_dark)
            else
                changeSingleOrMountainsIconDark(goal_single_image, R.mipmap.ic_single, R.mipmap.ic_single_dark, true)
        } else if (count > goal?.singleValue!!) { singleOrMountains()
        } else if (count == 0F) { resetSingle() }
    }

    private fun mountainsSituations() {
        if (count == 0F) {
            resetMountains()
        } else if (count > 0) {
            if (count <= goal?.bronzeValue!!) {
                setMountainBronzeValue()

                if (count == goal?.bronzeValue!!)
                    changeSingleOrMountainsIcon(goal_mountain_bronze_image, R.mipmap.ic_mountain_bronze, R.mipmap.ic_mountain_bronze_dark)

                if (count < goal?.silverValue!!)
                    resetMountainSilver()
            } else if (count <= goal?.silverValue!!) {
                setMountainSilverValue()

                if (count == goal?.silverValue!!)
                    changeSingleOrMountainsIcon(goal_mountain_silver_image, R.mipmap.ic_mountain_silver, R.mipmap.ic_mountain_silver_dark)

                if (count < goal?.goldValue!!)
                    resetMountainGold()
            } else if (count <= goal?.goldValue!!) {
                setMountainGoldValue()

                if (count == goal?.goldValue!!)
                    changeSingleOrMountainsIcon(goal_mountain_gold_image, R.mipmap.ic_mountain_gold, R.mipmap.ic_mountain_gold_dark)
            }
        }
    }

    private fun resetMountains() {
        resetMountainBronze()
        resetMountainSilver()
        resetMountainGold()
    }

    private fun resetMountainBronze() {
        seriesBronze = resetSingleOrMountain(
            arcViewBronze, 0F,
            goal?.bronzeValue!!, 0F)
    }

    private fun resetMountainSilver() {
        seriesSilver = resetSingleOrMountain(
            arcViewSilver, goal?.bronzeValue!!,
            goal?.silverValue!!, goal?.bronzeValue!!)
    }

    private fun resetMountainGold() {
        seriesGold = resetSingleOrMountain(
            arcViewGold, goal?.silverValue!!,
            goal?.goldValue!!, goal?.silverValue!!)
    }

    private fun resetSingle() {
        seriesSingle = resetSingleOrMountain(
            arcViewSingle, 0F,
            goal?.singleValue!!, 0F)
    }

    private fun resetSingleOrMountain(arcView: DecoView, minValue: Float, maxValue: Float, initialValue: Float) : Int {
        arcView.configureAngles(300, 0)
        return arcView.addSeries(setupArcViewAndSeriesItem(arcView, minValue, maxValue, initialValue, false))
    }

    private fun setMountainBronzeValue() = setupArcView(arcViewBronze, count, seriesBronze)

    private fun setMountainSilverValue() = setupArcView(arcViewSilver, count, seriesSilver)

    private fun setMountainGoldValue() = setupArcView(arcViewGold, count, seriesGold)

    private fun setSingleValue() = setupArcView(arcViewSingle, count, seriesSingle)

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
            changeSingleOrMountainsIconDark(
                goal_mountain_bronze_image,
                R.mipmap.ic_mountain_bronze,
                R.mipmap.ic_mountain_bronze_dark,
                true)
        if (count < goal?.silverValue!!)
            changeSingleOrMountainsIconDark(
                goal_mountain_silver_image,
                R.mipmap.ic_mountain_silver,
                R.mipmap.ic_mountain_silver_dark,
                true)
        if (count < goal?.goldValue!!)
            changeSingleOrMountainsIconDark(
                goal_mountain_gold_image,
                R.mipmap.ic_mountain_gold,
                R.mipmap.ic_mountain_gold_dark,
                true)
    }

    private fun changeSingleOrMountainsIconDark(image: ImageView, iconNormal: Int, iconDark: Int, dark: Boolean) {
        image.background =
            if (dark) ContextCompat.getDrawable(context!!, iconDark)
            else ContextCompat.getDrawable(context!!, iconNormal)
    }

    private fun changeSingleOrMountainsIcon(image: ImageView, iconNormal: Int, iconDark: Int) {
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
