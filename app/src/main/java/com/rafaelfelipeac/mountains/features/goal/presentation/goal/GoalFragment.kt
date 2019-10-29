package com.rafaelfelipeac.mountains.features.goal.presentation.goal

import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputEditText
import com.rafaelfelipeac.mountains.R
import com.rafaelfelipeac.mountains.core.extension.*
import com.rafaelfelipeac.mountains.features.goal.Historic
import com.rafaelfelipeac.mountains.features.goal.Item
import com.rafaelfelipeac.mountains.core.platform.base.BaseFragment
import com.rafaelfelipeac.mountains.features.commons.Goal
import com.rafaelfelipeac.mountains.features.commons.GoalType
import com.rafaelfelipeac.mountains.features.main.MainActivity
import kotlinx.android.synthetic.main.fragment_goal.*
import java.text.SimpleDateFormat
import java.util.*

class GoalFragment : BaseFragment() {

    private var isFromDragAndDrop: Boolean = false

    private var itemsAdapter = ItemsAdapter(this)
    private var historicAdapter = HistoricAdapter()

    private lateinit var bottomSheetItem: BottomSheetDialog
    private lateinit var bottomSheetItemSave: Button
    private lateinit var bottomSheetItemName: TextInputEditText
    private lateinit var bottomSheetItemEmptyName: TextView
    private lateinit var bottomSheetItemTitle: TextView
    private lateinit var bottomSheetItemDate: TextView

    private var seriesSingle: Int = 0
    private var seriesBronze: Int = 0
    private var seriesSilver: Int = 0
    private var seriesGold: Int = 0

    var goalId: Long? = null
    var goalNew: Boolean? = null
    var goal: Goal? = null
    private var items: List<Item>? = null
    private var history: List<Historic>? = null

    private var count: Float = 0F

    var item: Item? = null

    private val goalViewModel by lazy { viewModelFactory.get<GoalViewModel>(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        injector.inject(this)

        setHasOptionsMenu(true)

        (activity as MainActivity).openToolbar()

        goalId = arguments?.let { GoalFragmentArgs.fromBundle(it).goalId }
        goalNew = arguments?.let { GoalFragmentArgs.fromBundle(it).goalNew }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        (activity as MainActivity).supportActionBar?.title = getString(R.string.goal_title)

        hideNavigation()

        goalViewModel.init(goalId!!)

        return inflater.inflate(R.layout.fragment_goal, container, false)
    }

    override fun onResume() {
        super.onResume()

        isFromDragAndDrop = false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()

        setupBottomSheetItem()
    }

    private fun setupBottomSheetItem() {
        bottomSheetItem = BottomSheetDialog(this.activity!!)
        val sheetView = layoutInflater.inflate(R.layout.bottom_sheet_item, null)
        bottomSheetItem.setContentView(sheetView)

        bottomSheetItemSave = sheetView.findViewById(R.id.bottom_sheet_item_button_save)
        bottomSheetItemName = sheetView.findViewById(R.id.bottom_sheet_item_name)
        bottomSheetItemTitle = sheetView.findViewById(R.id.bottom_sheet_item_title)
        bottomSheetItemDate = sheetView.findViewById(R.id.bottom_sheet_item_date)
        bottomSheetItemEmptyName = sheetView.findViewById(R.id.bottom_sheet_item_empty_name)

        bottomSheetItemSave.setOnClickListener {
            if (bottomSheetItemName.isEmpty()) {
                bottomSheetItemEmptyName.text = getString(R.string.item_empty_name)
            } else {
                var item = Item()

                if (this.item == null) {
                    item.goalId = goal?.goalId!!
                    item.name = bottomSheetItemName.text.toString()
                    item.done = false
                    item.order = items?.size!! + 1
                    item.createdDate = getCurrentTime()
                } else {
                    item = this.item!!
                    item.name = bottomSheetItemName.text.toString()
                    item.updatedDate = getCurrentTime()
                }

                goalViewModel.saveItem(item)

                bottomSheetItemName.resetValue()
                closeBottomSheetItem()
            }
        }
    }

    private fun openBottomSheetItem(item: Item?) {
        bottomSheetItem.show()

        this.item = item

        if (item != null) {
            bottomSheetItemTitle.text = getString(R.string.item_title_edit)
            bottomSheetItemName.setText(item.name)

            if (item.done) {
                bottomSheetItemDate.text = String.format(
                    getString(R.string.item_date_format),
                    context?.let { item.doneDate?.convertDateToString(it) }
                )
                bottomSheetItemDate.visible()
            } else {
                bottomSheetItemDate.gone()
            }
        } else {
            bottomSheetItemTitle.text = getString(R.string.item_title_add)
            bottomSheetItemName.resetValue()
            bottomSheetItemDate.resetValue()
            bottomSheetItemDate.gone()
        }
    }

    private fun closeBottomSheetItem() {
        bottomSheetItem.hide()
    }

    private fun observeViewModel() {
        goalViewModel.getGoal()?.observe(this, Observer { goal ->
            this.goal = goal as Goal

            setupGoal()
        })

        goalViewModel.getItems()?.observe(this, Observer { items ->
            this.items = items.filter { it.goalId == goal?.goalId }

            if (!isFromDragAndDrop) {
                setupItems()
            }

            isFromDragAndDrop = false
        })

        goalViewModel.getHistory()?.observe(this, Observer { history ->
            this.history = history.filter { it.goalId == goal?.goalId }

            setupHistoric()
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        if (goal?.type == GoalType.GOAL_LIST) {
            inflater.inflate(R.menu.menu_add, menu)
        }

        inflater.inflate(R.menu.menu_edit, menu)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_add -> {
                openBottomSheetItem(null)

                return true
            }
            R.id.menu_edit -> {
                val action = GoalFragmentDirections.actionNavigationGoalToNavigationGoalForm()
                action.goalId =  goal?.goalId!!
                navController.navigate(action)
            }
            android.R.id.home -> {
                if (goalNew!!) {
                    navController.navigateUp()
                }
            }
        }

        return false
    }

    private fun setupButtons() {
        goal_btn_inc.setOnClickListener {
            count += goal?.incrementValue!!
            updateTextAndGoal(goal_inc_dec_total)

            goalViewModel.saveHistoric(
                Historic(
                    value = goal?.incrementValue!!,
                    date = Date(),
                    goalId = goal?.goalId!!
                )
            )
        }

        goal_btn_dec.setOnClickListener {
            count -= goal?.decrementValue!!
            updateTextAndGoal(goal_inc_dec_total)

            goalViewModel.saveHistoric(
                Historic(
                    value = goal?.decrementValue!! * -1,
                    date = Date(),
                    goalId = goal?.goalId!!
                )
            )
        }

        goal_button_save.setOnClickListener {
            if (goal_total_total.isNotEmpty()) {
                count = goal?.value!! + goal_total_total.toFloat()

                val oldDone = goal!!.done
                goal?.done = verifyIfGoalIsDone()

                updateTextAndGoal(goal_count)

                goalViewModel.saveHistoric(
                    Historic(
                        value = goal_total_total.toFloat(),
                        date = Date(),
                        goalId = goal?.goalId!!
                    )
                )

                goal_total_total.resetValue()

                if (!oldDone && goal!!.done)
                    showSnackBar(getString(R.string.goal_message_goal_done))
                else
                    showSnackBar(getString(R.string.goal_message_goal_value_updated))
            } else {
                showSnackBar(getString(R.string.goal_message_goal_value_invalid))
            }
        }
    }

    private fun onScoreFromList(done: Boolean) {
        if (done) count++ else count--
        updateTextAndGoal(goal_count)
    }

    private fun setupGoal() {
        count = goal?.value!!

        goal_title.text = goal?.name
        goal_count.text = count.getNumberInRightFormat()

        if (goal?.finalDate != null) {
            val myFormat = getString(R.string.date_format_dmy)
            val sdf = SimpleDateFormat(myFormat, Locale.US)

            goal_final_date.text = sdf.format(goal?.finalDate)
            goal_final_date.visible()
        }

        if (goal?.divideAndConquer!!) {
            goal_single.invisible()
            goal_mountains.visible()

            goal_mountain_bronze_text.text = goal?.bronzeValue?.getNumberInRightFormat()
            goal_mountain_silver_text.text = goal?.silverValue?.getNumberInRightFormat()
            goal_mountain_gold_text.text = goal?.goldValue?.getNumberInRightFormat()
        } else {
            goal_single_text.text = goal?.singleValue?.getNumberInRightFormat()
        }

        when (goal?.type) {
            GoalType.GOAL_LIST -> {
                goal_cl_list.visible()
                goal_cl_dec_inc.invisible()
                goal_cl_total.invisible()

                goal_historic_items_list.invisible()

                if ((activity as MainActivity).toolbar.menu.findItem(R.id.menu_add) == null)
                    (activity as MainActivity).toolbar.inflateMenu(R.menu.menu_add)
            }
            GoalType.GOAL_COUNTER -> {
                goal_cl_list.invisible()
                goal_cl_dec_inc.visible()
                goal_cl_total.invisible()

                goal_historic_items_list.visible()

                goal_inc_dec_total.text = count.getNumberInRightFormat()
            }
            GoalType.GOAL_FINAL -> {
                goal_cl_list.invisible()
                goal_cl_dec_inc.invisible()
                goal_cl_total.visible()

                goal_historic_items_list.visible()
            }
            GoalType.GOAL_NONE -> TODO()
        }

        if (isTheFirstTime()) {
            resetSingleOrMountains()
        }

        updateSingleOrMountains()

        setupButtons()
    }

    private fun isTheFirstTime() = seriesSingle == 0 && (seriesBronze == 0 || seriesSilver == 0 || seriesGold == 0)

    private fun updateTextAndGoal(textView: TextView) {
        updateText(textView)
        updateGoal()

        goalViewModel.saveGoal(goal!!)
    }

    private fun updateText(textView: TextView) {
        goal_count.text = count.getNumberInRightFormat()
        textView.text = count.getNumberInRightFormat()
    }

    private fun updateGoal() {
        goal?.value = count
        goal?.done = verifyIfGoalIsDone()

        updateSingleOrMountains()
    }

    private fun setupHistoric() {
        setHistory()
    }

    private fun setupItems() {
        if (goal?.type == GoalType.GOAL_LIST) {
            if (items?.any { it.goalId == goal?.goalId }!!) {
                setItems()

                goal_cl_list.visible()
                goal_items_placeholder.invisible()
            } else {
                goal_cl_list.invisible()
                goal_items_placeholder.visible()
            }
        }
    }

    private fun setItems() {
        items
            ?.sortedBy { it.order }
            ?.let { itemsAdapter.setItems(it) }

        itemsAdapter.clickListener = { openBottomSheetItem(it) }

        val swipeAndDragHelper = SwipeAndDragHelperItem(itemsAdapter)
        val touchHelper = ItemTouchHelper(swipeAndDragHelper)

        itemsAdapter.touchHelper = touchHelper

        goal_items_list.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        goal_items_list.adapter = itemsAdapter

        touchHelper.attachToRecyclerView(goal_items_list)
    }

    private fun setHistory() {
        history
            ?.reversed()
            ?.let { historicAdapter.setItems(it) }

        goal_historic_items_list.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        goal_historic_items_list.adapter = historicAdapter
    }

    private fun verifyIfGoalIsDone() =
        ((goal!!.divideAndConquer && goal!!.value >= goal!!.goldValue) ||
                (!goal!!.divideAndConquer && goal!!.value >= goal!!.singleValue))

    private fun updateSingleOrMountains() {
        if (goal?.divideAndConquer!!) {
            updateMountains()
        } else {
            updateSingle()
        }
    }

    private fun resetSingleOrMountains() {
        if (goal?.divideAndConquer!!) {
            resetMountains()
        } else {
            resetSingle()
        }
    }

    private fun updateSingle() {
        when {
            goal?.value!! == 0F -> {
                resetSingle()
            }
            goal?.value!! > 0 && goal?.value!! < goal?.singleValue!! -> {
                setSingleValue(goal?.value!!)
                disableIcon(goal_single_image, R.mipmap.ic_medal_dark)
            }
            goal?.value!! > 0 && goal?.value!! >= goal?.singleValue!! -> {
                setSingleValue(goal?.singleValue!!)
                enableIcon(goal_single_image, R.mipmap.ic_medal)
            }
        }
    }

    private fun updateMountains() {
        when {
            goal?.value!! == 0F || goal?.value!! < 0 -> {
                resetMountains()
            }
            goal?.value!! > 0 && goal?.value!! <= goal?.bronzeValue!! -> {
                setMountainBronzeValue(goal?.value!!)

                resetMountainSilver()

                if (goal?.value == goal?.bronzeValue!!) enableIcon(goal_mountain_bronze_image, R.mipmap.ic_bronze)
                else disableIcon(goal_mountain_bronze_image, R.mipmap.ic_bronze_dark)
            }
            goal?.value!! > 0 && goal?.value!! <= goal?.silverValue!! -> {
                setMountainBronzeValue(goal?.bronzeValue!!)
                setMountainSilverValue(goal?.value!!)

                enableIcon(goal_mountain_bronze_image, R.mipmap.ic_bronze)

                resetMountainGold()

                if (goal?.value == goal?.silverValue!!) enableIcon(goal_mountain_silver_image, R.mipmap.ic_silver)
                else disableIcon(goal_mountain_silver_image, R.mipmap.ic_silver_dark)
            }
            goal?.value!! > 0 && goal?.value!! <= goal?.goldValue!! -> {
                setMountainBronzeValue(goal?.bronzeValue!!)
                setMountainSilverValue(goal?.silverValue!!)
                setMountainGoldValue(goal?.value!!)

                enableIcon(goal_mountain_bronze_image, R.mipmap.ic_bronze)
                enableIcon(goal_mountain_silver_image, R.mipmap.ic_silver)

                if (goal?.value == goal?.goldValue!!) enableIcon(goal_mountain_gold_image, R.mipmap.ic_gold)
                else disableIcon(goal_mountain_gold_image, R.mipmap.ic_gold_dark)
            }
        }
    }

    private fun resetMountains() {
        resetMountainBronze()
        resetMountainSilver()
        resetMountainGold()
    }

    private fun resetMountainBronze() {
        seriesBronze = goal_bronze_arcView.resetValue(0F, goal?.bronzeValue!!, 0F)
    }

    private fun resetMountainSilver() {
        seriesSilver = goal_silver_arcView.resetValue(goal?.bronzeValue!!, goal?.silverValue!!, goal?.bronzeValue!!)
    }

    private fun resetMountainGold() {
        seriesGold = goal_gold_arcView.resetValue(goal?.silverValue!!, goal?.goldValue!!, goal?.silverValue!!)
    }

    private fun resetSingle() {
        seriesSingle = goal_single_arcView.resetValue(0F, goal?.singleValue!!, 0F)
    }

    private fun setMountainBronzeValue(value: Float) = goal_bronze_arcView.setup(value, seriesBronze)

    private fun setMountainSilverValue(value: Float) = goal_silver_arcView.setup(value, seriesSilver)

    private fun setMountainGoldValue(value: Float) = goal_gold_arcView.setup(value, seriesGold)

    private fun setSingleValue(value: Float) = goal_single_arcView.setup(value, seriesSingle)

    private fun enableIcon(image: ImageView, iconNormal: Int) {
        image.enableIcon(iconNormal, context!!)
    }

    private fun disableIcon(image: ImageView, iconDark: Int) {
        image.disableIcon(iconDark, context!!)
    }

    fun onViewMoved(
        oldPosition: Int, newPosition: Int, items: MutableList<Item>,
        function: (oldPosition: Int, newPosition: Int) -> Unit
    ) {
        val targetItem = items[oldPosition]
        val otherItem = items[newPosition]

        targetItem.order = newPosition
        otherItem.order = oldPosition

        isFromDragAndDrop = true

        goalViewModel.saveItem(targetItem)
        goalViewModel.saveItem(otherItem)

        items.removeAt(oldPosition)
        items.add(newPosition, targetItem)

        function(oldPosition, newPosition)
    }

    fun onViewSwiped(position: Int, direction: Int, holder: RecyclerView.ViewHolder, items: MutableList<Item>) {
        val item = items[position]

        when (direction) {
            ItemTouchHelper.RIGHT -> {
                if (item.done) {
                    item.done = false
                    item.undoneDate = getCurrentTime()

                    goalViewModel.saveItem(item)

                    onScoreFromList(false)
                } else {
                    item.done = true
                    item.doneDate = getCurrentTime()

                    goalViewModel.saveItem(item)

                    onScoreFromList(true)
                }
            }
            ItemTouchHelper.LEFT -> {
                item.deleteDate = getCurrentTime()

                goalViewModel.deleteItem(item)

                showSnackBarWithAction(holder.itemView, getString(R.string.habit_item_swiped_deleted), item, ::deleteItem)
            }
        }
    }

    private fun deleteItem(item: Any) {
        goalViewModel.saveItem(item as Item)
    }
}
