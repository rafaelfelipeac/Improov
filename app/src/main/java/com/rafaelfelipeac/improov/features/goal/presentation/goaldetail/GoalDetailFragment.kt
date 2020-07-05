package com.rafaelfelipeac.improov.features.goal.presentation.goaldetail

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.MenuInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rafaelfelipeac.improov.R
import com.rafaelfelipeac.improov.core.extension.observe
import com.rafaelfelipeac.improov.core.extension.visible
import com.rafaelfelipeac.improov.core.extension.invisible
import com.rafaelfelipeac.improov.core.extension.setup
import com.rafaelfelipeac.improov.core.extension.getNumberInRightFormat
import com.rafaelfelipeac.improov.core.extension.toFloat
import com.rafaelfelipeac.improov.core.extension.resetValue
import com.rafaelfelipeac.improov.core.extension.isNotEmpty
import com.rafaelfelipeac.improov.core.extension.enableIcon
import com.rafaelfelipeac.improov.core.extension.disableIcon
import com.rafaelfelipeac.improov.core.extension.vibrate
import com.rafaelfelipeac.improov.core.extension.isVisible
import com.rafaelfelipeac.improov.core.platform.base.BaseFragment
import com.rafaelfelipeac.improov.features.goal.data.enums.GoalType
import com.rafaelfelipeac.improov.features.goal.domain.model.Goal
import com.rafaelfelipeac.improov.features.goal.domain.model.Historic
import com.rafaelfelipeac.improov.features.goal.domain.model.Item
import kotlinx.android.synthetic.main.fragment_goal.*
import java.util.Date

@Suppress("TooManyFunctions")
class GoalDetailFragment : BaseFragment() {

    private var itemsAdapter = ItemsAdapter(this)
    private var historicAdapter = HistoricAdapter()

    private var seriesSingle: Int = 0
    private var seriesBronze: Int = 0
    private var seriesSilver: Int = 0
    private var seriesGold: Int = 0

    private var goalNew: Boolean? = null
    private var goalId: Long? = null
    private var goal: Goal? = null
    private var item: Item? = null
    private var itemsSize: Int? = null

    private var count: Float = 0F

    private val viewModel by lazy { viewModelFactory.get<GoalDetailViewModel>(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        injector.inject(this)

        setHasOptionsMenu(true)

        main.openToolbar()

        goalId = arguments?.let { GoalDetailFragmentArgs.fromBundle(it).goalId }
        goalNew = arguments?.let { GoalDetailFragmentArgs.fromBundle(it).goalNew }
    }

    override fun onResume() {
        super.onResume()

        resetFistTime()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        main.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        main.supportActionBar?.title = getString(R.string.goal_title)

        hideNavigation()

        return inflater.inflate(R.layout.fragment_goal, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        goalId.let {
            if (it!! > 0L) {
                viewModel.setGoalId(goalId!!)
            }
        }
        viewModel.loadData()

        observeViewModel()

        setupBottomSheetItem(::newItem, ::updateItem)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_edit, menu)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_add -> {
                showBottomSheetItem(null)

                return true
            }
            R.id.menu_edit -> {
                val action = GoalDetailFragmentDirections.actionNavigationGoalToNavigationGoalForm()
                action.goalId = goal?.goalId!!
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

    private fun observeViewModel() {
        viewModel.savedGoal.observe(this) {
            // ???
        }

        viewModel.goal.observe(this) {
            goal = it
            setupGoal()
        }

        viewModel.savedItem.observe(this) {
            // ???
        }

        viewModel.items.observe(this) {
            itemsSize = it.size

            it.let { itemsAdapter.setItems(it) }
            setupItems(it.isNotEmpty())
        }

        viewModel.savedHistoric.observe(this) {
            // ???
        }

        viewModel.historics.observe(this) {
            it.let { historicAdapter.setItems(it) }
            setupHistoric(it.isNotEmpty())
        }
    }

    private fun resetFistTime() {
        seriesSingle = 0
        seriesBronze = 0
        seriesSilver = 0
        seriesGold = 0
    }

    private fun setupButtons() {
        goal_btn_counter_inc.setOnClickListener {
            count += goal?.incrementValue!!

            val oldDone = goal!!.done
            goal?.done = verifyIfGoalIsDone()

            updateTextAndGoal(goal_counter_total)

            viewModel.saveHistoric(
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

            viewModel.saveHistoric(
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

                viewModel.saveHistoric(
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

                goal_historics_list.invisible()

                if (main.toolbar.menu.findItem(R.id.menu_add) == null) {
                    main.toolbar.inflateMenu(R.menu.menu_add)
                }
            }
            GoalType.GOAL_COUNTER -> {
                goal_cl_list.invisible()
                goal_cl_counter.visible()
                goal_cl_total.invisible()

                goal_historics_list.visible()

                goal_counter_total.text = count.getNumberInRightFormat()
            }
            GoalType.GOAL_FINAL -> {
                goal_cl_list.invisible()
                goal_cl_counter.invisible()
                goal_cl_total.visible()

                goal_historics_list.visible()
            }
            GoalType.GOAL_NONE -> { }
        }

        if (isTheFirstTime()) {
            resetSingleOrDivideAndConquer()
        }

        updateSingleOrDivideAndConquer()

        setupButtons()
    }

    private fun isTheFirstTime() =
        seriesSingle == 0 && (seriesBronze == 0 || seriesSilver == 0 || seriesGold == 0)

    private fun updateTextAndGoal(textView: TextView) {
        updateText(textView)
        updateGoal()

        viewModel.saveGoal(goal!!)
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

    private fun setupItems(visible: Boolean) {
        if (goal?.type == GoalType.GOAL_LIST) {
            setItems()

            goal_items_list.isVisible(visible)
            goal_items_placeholder.isVisible(!visible)
        }
    }

    private fun setupHistoric(visible: Boolean) {
        if (goal?.type == GoalType.GOAL_FINAL || goal?.type == GoalType.GOAL_COUNTER) {
            setHistory()

            goal_historics_list.isVisible(visible)
            goal_historics_placeholder.isVisible(!visible)
        }
    }

    private fun setItems() {
        itemsAdapter.clickListener = {
            showBottomSheetItem(it)
            this.item = it
        }

        val swipeAndDragHelper = SwipeAndDragHelperItem(itemsAdapter)
        val touchHelper = ItemTouchHelper(swipeAndDragHelper)

        itemsAdapter.touchHelper = touchHelper

        goal_items_list.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        goal_items_list.adapter = itemsAdapter

        touchHelper.attachToRecyclerView(goal_items_list)
    }

    private fun setHistory() {
        goal_historics_list.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        goal_historics_list.adapter = historicAdapter
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

                if (goal?.value == goal?.bronzeValue!!) {
                    enableIcon(goal_divide_and_conquer_bronze_image, R.mipmap.ic_bronze)
                } else {
                    disableIcon(goal_divide_and_conquer_bronze_image, R.mipmap.ic_bronze_dark)
                }
            }
            goal?.value!! > 0 && goal?.value!! <= goal?.silverValue!! -> {
                setDivideAndConquerBronzeValue(goal?.bronzeValue!!)
                setDivideAndConquerSilverValue(goal?.value!!)

                enableIcon(goal_divide_and_conquer_bronze_image, R.mipmap.ic_bronze)

                resetDivideAndConquerGold()

                if (goal?.value == goal?.silverValue!!) {
                    enableIcon(goal_divide_and_conquer_silver_image, R.mipmap.ic_silver)
                } else {
                    disableIcon(goal_divide_and_conquer_silver_image, R.mipmap.ic_silver_dark)
                }
            }
            goal?.value!! > 0 && goal?.value!! <= goal?.goldValue!! -> {
                setDivideAndConquerBronzeValue(goal?.bronzeValue!!)
                setDivideAndConquerSilverValue(goal?.silverValue!!)
                setDivideAndConquerGoldValue(goal?.value!!)

                enableIcon(goal_divide_and_conquer_bronze_image, R.mipmap.ic_bronze)
                enableIcon(goal_divide_and_conquer_silver_image, R.mipmap.ic_silver)

                if (goal?.value == goal?.goldValue!!) {
                    enableIcon(goal_divide_and_conquer_gold_image, R.mipmap.ic_gold)
                } else {
                    disableIcon(goal_divide_and_conquer_gold_image, R.mipmap.ic_gold_dark)
                }
            }
        }
    }

    private fun resetDivideAndConquer() {
        resetDivideAndConquerBronze()
        resetDivideAndConquerSilver()
        resetDivideAndConquerGold()
    }

    private fun resetDivideAndConquerBronze() {
        seriesBronze = goal_bronze_arcView.resetValue(
            minValue = 0F,
            maxValue = goal?.bronzeValue!!,
            initialValue = 0F,
            screenMultiplier = getScreenMultiplier()
        )
    }

    private fun resetDivideAndConquerSilver() {
        seriesSilver = goal_silver_arcView.resetValue(
            minValue = goal?.bronzeValue!!,
            maxValue = goal?.silverValue!!,
            initialValue = goal?.bronzeValue!!,
            screenMultiplier = getScreenMultiplier()
        )
    }

    private fun resetDivideAndConquerGold() {
        seriesGold = goal_gold_arcView.resetValue(
            minValue = goal?.silverValue!!,
            maxValue = goal?.goldValue!!,
            initialValue = goal?.silverValue!!,
            screenMultiplier = getScreenMultiplier()
        )
    }

    private fun resetSingle() {
        seriesSingle = goal_single_arcView.resetValue(
            minValue = 0F,
            maxValue = goal?.singleValue!!,
            initialValue = 0F,
            screenMultiplier = getScreenMultiplier()
        )
    }

    private fun setDivideAndConquerBronzeValue(value: Float) =
        goal_bronze_arcView.setup(value, seriesBronze)

    private fun setDivideAndConquerSilverValue(value: Float) =
        goal_silver_arcView.setup(value, seriesSilver)

    private fun setDivideAndConquerGoldValue(value: Float) =
        goal_gold_arcView.setup(value, seriesGold)

    private fun setSingleValue(value: Float) = goal_single_arcView.setup(value, seriesSingle)

    private fun enableIcon(image: ImageView, iconNormal: Int) {
        image.enableIcon(iconNormal, requireContext())
    }

    private fun disableIcon(image: ImageView, iconDark: Int) {
        image.disableIcon(iconDark, requireContext())
    }

    fun onViewMoved(
        fromPosition: Int,
        toPosition: Int,
        items: MutableList<Item>,
        function: (fromPosition: Int, toPosition: Int) -> Unit
    ) {
        val targetItem = items[fromPosition]
        val otherItem = items[toPosition]

        targetItem.order = toPosition
        otherItem.order = fromPosition

        viewModel.saveItem(targetItem, isFromDragOnDrop = true)
        viewModel.saveItem(otherItem, isFromDragOnDrop = true)

        items.removeAt(fromPosition)
        items.add(toPosition, targetItem)

        function(fromPosition, toPosition)

        vibrate()
    }

    fun onViewSwiped(
        position: Int,
        direction: Int,
        // holder: RecyclerView.ViewHolder,
        items: MutableList<Item>
    ) {
        val item = items[position]

        when (direction) {
            ItemTouchHelper.RIGHT -> {
                if (item.done) {
                    item.done = false
                    item.undoneDate = getCurrentTime()

                    viewModel.saveItem(item)

                    onScoreFromList(false)
                } else {
                    item.done = true
                    item.doneDate = getCurrentTime()

                    viewModel.saveItem(item)

                    onScoreFromList(true)
                }
            }
            ItemTouchHelper.LEFT -> {
//                setupItems()
//                item.deleteDate = getCurrentTime()
//
//                goalViewModel.deleteItem(item)
//
//                showSnackBarWithAction(
//                    holder.itemView, getString(R.string.habit_item_swiped_deleted), item, ::deleteItem)
            }
        }
    }

    private fun deleteItem(item: Any) {
        viewModel.saveItem(item as Item)
    }

    private fun verifyIfWasDone(oldDone: Boolean) {
        if (!oldDone && goal!!.done) {
            showSnackBar(getString(R.string.goal_message_goal_done))
        } else {
            showSnackBar(getString(R.string.goal_message_goal_value_updated))

            hideSoftKeyboard()
        }
    }

    private fun newItem(name: String) {
        val item =
            Item(
                goalId = goal?.goalId!!,
                name = name,
                done = false,
                order = itemsSize!! + 1,
                createdDate = getCurrentTime()
            )

        viewModel.saveItem(item)
    }

    private fun updateItem(name: String) {
        val item = this.item!!
        item.name = name
        item.updatedDate = getCurrentTime()

        viewModel.saveItem(item)
    }

    private fun getScreenMultiplier(): Float {
        val metrics = DisplayMetrics()
        activity?.windowManager?.defaultDisplay?.getMetrics(metrics)

        return metrics.density
    }
}
