package com.rafaelfelipeac.improov.features.goal.presentation.goal

import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputEditText
import com.rafaelfelipeac.improov.R
import com.rafaelfelipeac.improov.core.extension.*
import com.rafaelfelipeac.improov.core.platform.base.BaseFragment
import com.rafaelfelipeac.improov.features.commons.Goal
import com.rafaelfelipeac.improov.features.commons.GoalType
import com.rafaelfelipeac.improov.features.goal.Historic
import com.rafaelfelipeac.improov.features.goal.Item
import com.rafaelfelipeac.improov.features.main.MainActivity
import kotlinx.android.synthetic.main.fragment_goal.*
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

        resetFistTime()

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
                if (this.item == null) {
                    // new item
                    val item = Item(
                        goalId = goal?.goalId!!,
                        name = bottomSheetItemName.text.toString(),
                        done = false,
                        order = items?.size!! + 1,
                        createdDate = getCurrentTime()
                    )

                    goalViewModel.saveItem(item)
                } else {
                    // update item
                    val item = this.item!!
                    item.name = bottomSheetItemName.text.toString()
                    item.updatedDate = getCurrentTime()

                    goalViewModel.saveItem(item)
                }

                bottomSheetItemName.resetValue()
                closeBottomSheetItem()
            }
        }
    }

    private fun resetFistTime() {
        seriesSingle = 0
        seriesBronze = 0
        seriesSilver = 0
        seriesGold = 0
    }

    private fun openBottomSheetItem(item: Item?) {
        bottomSheetItem.show()

        bottomSheetItem.window?.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE or
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE
        )

        val bottomSheet = bottomSheetItem.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
        BottomSheetBehavior.from(bottomSheet!!).state = BottomSheetBehavior.STATE_EXPANDED

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

            if (goal.type == GoalType.GOAL_LIST) {
                goalViewModel.getItems()?.observe(this, Observer { items ->
                    this.items = items.filter { it.goalId == goal.goalId }

                    if (!isFromDragAndDrop) {
                        setupItems()
                    }

                    isFromDragAndDrop = false
                })
            }

            if (goal.type == GoalType.GOAL_COUNTER || goal.type == GoalType.GOAL_FINAL) {
                goalViewModel.getHistory()?.observe(this, Observer { history ->
                    this.history = history.filter { it.goalId == goal.goalId }

                    setupHistoric()
                })
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
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
        goal_btn_counter_inc.setOnClickListener {
            count += goal?.incrementValue!!

            val oldDone = goal!!.done
            goal?.done = verifyIfGoalIsDone()

            updateTextAndGoal(goal_counter_total)

            goalViewModel.saveHistoric(
                Historic(
                    value = goal?.incrementValue!!,
                    date = Date(),
                    goalId = goal?.goalId!!
                )
            )

            verifyIfWasDone(oldDone)
        }

        goal_btn_counter_dec.setOnClickListener {
            count -= goal?.decrementValue!!

            val oldDone = goal!!.done
            goal?.done = verifyIfGoalIsDone()

            updateTextAndGoal(goal_counter_total)

            goalViewModel.saveHistoric(
                Historic(
                    value = goal?.decrementValue!! * -1,
                    date = Date(),
                    goalId = goal?.goalId!!
                )
            )

            verifyIfWasDone(oldDone)
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

                verifyIfWasDone(oldDone)
            } else {
                showSnackBarLong(getString(R.string.goal_message_goal_value_invalid))
            }
        }
    }

    private fun onScoreFromList(done: Boolean) {
        if (done) count++ else count--

        val oldDone = goal!!.done
        goal?.done = verifyIfGoalIsDone()

        updateTextAndGoal(goal_count)

        verifyIfWasDone(oldDone)
    }

    private fun setupGoal() {
        count = goal?.value!!

        goal_title.text = goal?.name
        goal_count.text = count.getNumberInRightFormat()

//        if (goal?.date != null) {
//            val myFormat = getString(R.string.date_format_dmy)
//            val sdf = SimpleDateFormat(myFormat, Locale.US)
//
//            goal_date.text = sdf.format(goal?.date)
//            goal_date.visible()
//        }

        if (goal?.divideAndConquer!!) {
            goal_single.invisible()
            goal_divide_and_conquer.visible()

            goal_divide_and_conquer_bronze_text.text = goal?.bronzeValue?.getNumberInRightFormat()
            goal_divide_and_conquer_silver_text.text = goal?.silverValue?.getNumberInRightFormat()
            goal_divide_and_conquer_gold_text.text = goal?.goldValue?.getNumberInRightFormat()
        } else {
            goal_single_text.text = goal?.singleValue?.getNumberInRightFormat()
        }

        when (goal?.type) {
            GoalType.GOAL_LIST -> {
                goal_cl_list.visible()
                goal_cl_counter.invisible()
                goal_cl_total.invisible()

                goal_historic_items_list.invisible()

                if ((activity as MainActivity).toolbar.menu.findItem(R.id.menu_add) == null) {
                    (activity as MainActivity).toolbar.inflateMenu(R.menu.menu_add)
                }
            }
            GoalType.GOAL_COUNTER -> {
                goal_cl_list.invisible()
                goal_cl_counter.visible()
                goal_cl_total.invisible()

                goal_historic_items_list.visible()

                goal_counter_total.text = count.getNumberInRightFormat()
            }
            GoalType.GOAL_FINAL -> {
                goal_cl_list.invisible()
                goal_cl_counter.invisible()
                goal_cl_total.visible()

                goal_historic_items_list.visible()
            }
            GoalType.GOAL_NONE -> TODO()
        }

        if (isTheFirstTime()) {
            resetSingleOrDivideAndConquer()
        }

        updateSingleOrDivideAndConquer()

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

        updateSingleOrDivideAndConquer()
    }

    private fun setupHistoric() {
        if (goal?.type == GoalType.GOAL_FINAL || goal?.type == GoalType.GOAL_COUNTER) {
            if (history?.any { it.goalId == goal?.goalId }!!) {
                setHistory()

                goal_history_placeholder.invisible()
            } else {
                goal_history_placeholder.visible()
                goal_items_placeholder.gone()
            }
        }
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
                goal_history_placeholder.gone()
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

    private fun updateSingleOrDivideAndConquer() {
        if (goal?.divideAndConquer!!) {
            updateDivideAndConquer()
        } else {
            updateSingle()
        }
    }

    private fun resetSingleOrDivideAndConquer() {
        if (goal?.divideAndConquer!!) {
            resetDivideAndConquer()
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

    private fun updateDivideAndConquer() {
        when {
            goal?.value!! == 0F || goal?.value!! < 0 -> {
                resetDivideAndConquer()
            }
            goal?.value!! > 0 && goal?.value!! <= goal?.bronzeValue!! -> {
                setDivideAndConquerBronzeValue(goal?.value!!)

                resetDivideAndConquerSilver()

                if (goal?.value == goal?.bronzeValue!!) enableIcon(goal_divide_and_conquer_bronze_image, R.mipmap.ic_bronze)
                else disableIcon(goal_divide_and_conquer_bronze_image, R.mipmap.ic_bronze_dark)
            }
            goal?.value!! > 0 && goal?.value!! <= goal?.silverValue!! -> {
                setDivideAndConquerBronzeValue(goal?.bronzeValue!!)
                setDivideAndConquerSilverValue(goal?.value!!)

                enableIcon(goal_divide_and_conquer_bronze_image, R.mipmap.ic_bronze)

                resetDivideAndConquerGold()

                if (goal?.value == goal?.silverValue!!) enableIcon(goal_divide_and_conquer_silver_image, R.mipmap.ic_silver)
                else disableIcon(goal_divide_and_conquer_silver_image, R.mipmap.ic_silver_dark)
            }
            goal?.value!! > 0 && goal?.value!! <= goal?.goldValue!! -> {
                setDivideAndConquerBronzeValue(goal?.bronzeValue!!)
                setDivideAndConquerSilverValue(goal?.silverValue!!)
                setDivideAndConquerGoldValue(goal?.value!!)

                enableIcon(goal_divide_and_conquer_bronze_image, R.mipmap.ic_bronze)
                enableIcon(goal_divide_and_conquer_silver_image, R.mipmap.ic_silver)

                if (goal?.value == goal?.goldValue!!) enableIcon(goal_divide_and_conquer_gold_image, R.mipmap.ic_gold)
                else disableIcon(goal_divide_and_conquer_gold_image, R.mipmap.ic_gold_dark)
            }
        }
    }

    private fun resetDivideAndConquer() {
        resetDivideAndConquerBronze()
        resetDivideAndConquerSilver()
        resetDivideAndConquerGold()
    }

    private fun resetDivideAndConquerBronze() {
        seriesBronze = goal_bronze_arcView.resetValue(0F, goal?.bronzeValue!!, 0F)
    }

    private fun resetDivideAndConquerSilver() {
        seriesSilver = goal_silver_arcView.resetValue(goal?.bronzeValue!!, goal?.silverValue!!, goal?.bronzeValue!!)
    }

    private fun resetDivideAndConquerGold() {
        seriesGold = goal_gold_arcView.resetValue(goal?.silverValue!!, goal?.goldValue!!, goal?.silverValue!!)
    }

    private fun resetSingle() {
        seriesSingle = goal_single_arcView.resetValue(0F, goal?.singleValue!!, 0F)
    }

    private fun setDivideAndConquerBronzeValue(value: Float) = goal_bronze_arcView.setup(value, seriesBronze)

    private fun setDivideAndConquerSilverValue(value: Float) = goal_silver_arcView.setup(value, seriesSilver)

    private fun setDivideAndConquerGoldValue(value: Float) = goal_gold_arcView.setup(value, seriesGold)

    private fun setSingleValue(value: Float) = goal_single_arcView.setup(value, seriesSingle)

    private fun enableIcon(image: ImageView, iconNormal: Int) {
        image.enableIcon(iconNormal, context!!)
    }

    private fun disableIcon(image: ImageView, iconDark: Int) {
        image.disableIcon(iconDark, context!!)
    }

    fun onViewMoved(fromPosition: Int, toPosition: Int, items: MutableList<Item>,
                    function: (fromPosition: Int, toPosition: Int) -> Unit) {
        val targetItem = items[fromPosition]
        val otherItem = items[toPosition]

        targetItem.order = toPosition
        otherItem.order = fromPosition

        isFromDragAndDrop = true

        goalViewModel.saveItem(targetItem)
        goalViewModel.saveItem(otherItem)

        items.removeAt(fromPosition)
        items.add(toPosition, targetItem)

        function(fromPosition, toPosition)

        vibrate()
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
                setupItems()
//                item.deleteDate = getCurrentTime()
//
//                goalViewModel.deleteItem(item)
//
//                showSnackBarWithAction(holder.itemView, getString(R.string.habit_item_swiped_deleted), item, ::deleteItem)
            }
        }
    }

    private fun deleteItem(item: Any) {
        goalViewModel.saveItem(item as Item)
    }

    private fun verifyIfWasDone(oldDone: Boolean) {
        if (!oldDone && goal!!.done) {
            showSnackBar(getString(R.string.goal_message_goal_done))
        } else {
            showSnackBar(getString(R.string.goal_message_goal_value_updated))

            hideSoftKeyboard()
        }
    }
}
