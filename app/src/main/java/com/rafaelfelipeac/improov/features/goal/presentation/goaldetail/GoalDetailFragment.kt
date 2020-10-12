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
import com.rafaelfelipeac.improov.core.extension.gone
import com.rafaelfelipeac.improov.core.extension.invisible
import com.rafaelfelipeac.improov.core.extension.setup
import com.rafaelfelipeac.improov.core.extension.getNumberInExhibitionFormat
import com.rafaelfelipeac.improov.core.extension.toFloat
import com.rafaelfelipeac.improov.core.extension.resetValue
import com.rafaelfelipeac.improov.core.extension.isNotEmpty
import com.rafaelfelipeac.improov.core.extension.enableIcon
import com.rafaelfelipeac.improov.core.extension.disableIcon
import com.rafaelfelipeac.improov.core.extension.vibrate
import com.rafaelfelipeac.improov.core.platform.base.BaseFragment
import com.rafaelfelipeac.improov.features.commons.data.enums.GoalType
import com.rafaelfelipeac.improov.features.commons.domain.model.Goal
import com.rafaelfelipeac.improov.features.commons.domain.model.Historic
import com.rafaelfelipeac.improov.features.commons.domain.model.Item
import kotlinx.android.synthetic.main.fragment_goal_detail.*
import java.util.Date

@Suppress("TooManyFunctions")
class GoalDetailFragment : BaseFragment() {

    private var itemsAdapter = ItemsAdapter(this)
    private var historicAdapter = HistoricAdapter()

    private var swipedPosition: Int = 0

    private var seriesSingle: Int = 0
    private var seriesBronze: Int = 0
    private var seriesSilver: Int = 0
    private var seriesGold: Int = 0

    private var goalNew: Boolean? = null
    private var goalId: Long? = null
    private var goal: Goal? = null
    private var item: Item? = null
    private var itemsSize: Int? = null
    private var historicsSize: Int? = null

    private var count: Float = 0F

    private val viewModel by lazy { viewModelProvider.goalDetailViewModel() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

        setScreen()

        return inflater.inflate(R.layout.fragment_goal_detail, container, false)
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
            R.id.menuAdd -> {
                showBottomSheetItem(null)

                return true
            }
            R.id.menuEdit -> {
                val action = GoalDetailFragmentDirections.goalToGoalForm()
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

    private fun setScreen() {
        setTitle(getString(R.string.goal_title))
        showBackArrow()
        hasMenu()
        hideNavigation()
    }

    private fun observeViewModel() {
        viewModel.savedGoal.observe(this) {
            updateProgress()
        }

        viewModel.goal.observe(this) {
            goal = it
            setupGoal()
        }

        viewModel.savedItem.observe(this) {
            viewModel.getItems()

            updateTextAndGoal()

            reloadItemAfterSwipe()
        }

        viewModel.items.observe(this) {
            itemsSize = it.size

            it.let { itemsAdapter.setItems(it) }
            setupItems()
        }

        viewModel.savedHistoric.observe(this) {
            viewModel.getHistorics()

            updateTextAndGoal()
        }

        viewModel.historics.observe(this) {
            historicsSize = it.size

            it.let { historicAdapter.setItems(it) }
            setupHistoric()
        }
    }

    private fun resetFistTime() {
        seriesSingle = 0
        seriesBronze = 0
        seriesSilver = 0
        seriesGold = 0
    }

    private fun setupButtons() {
        goalDetailBtnCounterInc.setOnClickListener {
            count += goal?.incrementValue!!

            viewModel.saveHistoric(
                Historic(
                    value = goal?.incrementValue!!,
                    date = Date(),
                    goalId = goal?.goalId!!
                )
            )

            updateGoal()
        }

        goalDetailBtnCounterDec.setOnClickListener {
            count -= goal?.decrementValue!!

            viewModel.saveHistoric(
                Historic(
                    value = goal?.decrementValue!! * -1,
                    date = Date(),
                    goalId = goal?.goalId!!
                )
            )

            updateGoal()
        }

        goalDetailButtonSave.setOnClickListener {
            if (goalDetailTotalValue.isNotEmpty()) {
                count = goal?.value!! + goalDetailTotalValue.toFloat()

                viewModel.saveHistoric(
                    Historic(
                        value = goalDetailTotalValue.toFloat(),
                        date = Date(),
                        goalId = goal?.goalId!!
                    )
                )

                updateGoal()

                goalDetailTotalValue.resetValue()
            } else {
                showSnackBar(getString(R.string.goal_message_goal_value_invalid))
            }
        }
    }

    private fun setupGoal() {
        count = goal?.value!!

        goalDetailTitle.text = goal?.name
        goalDetailCount.text = count.getNumberInExhibitionFormat()

        if (goal?.divideAndConquer!!) {
            goalDetailSingle.invisible()
            goalDetailDivideAndConquer.visible()

            goalDetailDivideAndConquerBronzeText.text = goal?.bronzeValue?.getNumberInExhibitionFormat()
            goalDetailDivideAndConquerSilverText.text = goal?.silverValue?.getNumberInExhibitionFormat()
            goalDetailDivideAndConquerGoldText.text = goal?.goldValue?.getNumberInExhibitionFormat()
        } else {
            goalDetailSingleText.text = goal?.singleValue?.getNumberInExhibitionFormat()
        }

        when (goal?.type) {
            GoalType.GOAL_LIST -> {
                goalDetailCLList.visible()
                goalDetailCLCounter.invisible()
                goalDetailCLTotal.invisible()

                goalDetailHistoricsList.invisible()

                if (main.toolbar.menu.findItem(R.id.menuAdd) == null) {
                    main.toolbar.inflateMenu(R.menu.menu_add)
                }
            }
            GoalType.GOAL_COUNTER -> {
                goalDetailCLList.invisible()
                goalDetailCLCounter.visible()
                goalDetailCLTotal.invisible()

                goalDetailHistoricsList.visible()

                goalDetailCounterTotal.text = count.getNumberInExhibitionFormat()
            }
            GoalType.GOAL_FINAL -> {
                goalDetailCLList.invisible()
                goalDetailCLCounter.invisible()
                goalDetailCLTotal.visible()

                goalDetailHistoricsList.visible()
            }
            GoalType.GOAL_NONE -> { }
        }

        setupItems()
        setupHistoric()

        if (isTheFirstTime()) {
            resetSingleOrDivideAndConquer()
        }

        updateProgress()

        setupButtons()
    }

    private fun isTheFirstTime() =
        seriesSingle == 0 && (seriesBronze == 0 || seriesSilver == 0 || seriesGold == 0)

    private fun updateTextAndGoal() {
        updateText(getTextViewFromGoalType())

        viewModel.saveGoal(goal!!)
    }

    private fun updateText(textView: TextView) {
        goalDetailCount.text = count.getNumberInExhibitionFormat()
        textView.text = count.getNumberInExhibitionFormat()
    }

    private fun updateGoal() {
        goal?.value = count

        val oldDone = goal!!.done
        goal?.done = verifyIfGoalIsDone()
        verifyIfWasDone(oldDone)
    }

    private fun getTextViewFromGoalType(): TextView {
        return when (goal?.type) {
            GoalType.GOAL_LIST, GoalType.GOAL_FINAL -> {
                goalDetailCount
            }
            GoalType.GOAL_COUNTER -> {
                goalDetailCounterTotal
            }
            else -> goalDetailCount
        }
    }

    private fun setupItems() {
        if (goal?.type == GoalType.GOAL_LIST) {
            if (itemsSize != null && itemsSize!! > 0) {
                setItems()
                goalDetailItemsPlaceholder.invisible()
            } else {
                goalDetailItemsPlaceholder.visible()
            }

            goalDetailLoading.gone()
        }
    }

    private fun setupHistoric() {
        if (goal?.type == GoalType.GOAL_FINAL || goal?.type == GoalType.GOAL_COUNTER) {
            if (historicsSize != null && historicsSize!! > 0) {
                setHistory()
                goalDetailHistoricsPlaceholder.invisible()
            } else {
                goalDetailHistoricsPlaceholder.visible()
            }

            goalDetailLoading.gone()
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

        goalDetailItemsList.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        goalDetailItemsList.adapter = itemsAdapter

        touchHelper.attachToRecyclerView(goalDetailItemsList)
    }

    private fun setHistory() {
        goalDetailHistoricsList.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        goalDetailHistoricsList.adapter = historicAdapter
    }

    private fun verifyIfGoalIsDone() =
        ((goal!!.divideAndConquer && goal!!.value >= goal!!.goldValue) ||
                (!goal!!.divideAndConquer && goal!!.value >= goal!!.singleValue))

    private fun updateProgress() {
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
            goal?.value!! == 0F || goal?.value!! < 0 -> {
                resetSingle()
            }
            goal?.value!! > 0 && goal?.value!! < goal?.singleValue!! -> {
                setSingleValue(goal?.value!!)
                disableIcon(goalDetailSingleImage, R.mipmap.ic_medal_dark)
            }
            goal?.value!! > 0 && goal?.value!! >= goal?.singleValue!! -> {
                setSingleValue(goal?.singleValue!!)
                enableIcon(goalDetailSingleImage, R.mipmap.ic_medal)
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
                    enableIcon(goalDetailDivideAndConquerBronzeImage, R.mipmap.ic_trophy_bronze)
                } else {
                    disableIcon(goalDetailDivideAndConquerBronzeImage, R.mipmap.ic_trophy_bronze_dark)
                }
            }
            goal?.value!! > 0 && goal?.value!! <= goal?.silverValue!! -> {
                setDivideAndConquerBronzeValue(goal?.bronzeValue!!)
                setDivideAndConquerSilverValue(goal?.value!!)

                enableIcon(goalDetailDivideAndConquerBronzeImage, R.mipmap.ic_trophy_bronze)

                resetDivideAndConquerGold()

                if (goal?.value == goal?.silverValue!!) {
                    enableIcon(goalDetailDivideAndConquerSilverImage, R.mipmap.ic_trophy_silver)
                } else {
                    disableIcon(goalDetailDivideAndConquerSilverImage, R.mipmap.ic_trophy_silver_dark)
                }
            }
            goal?.value!! > 0 && goal?.value!! <= goal?.goldValue!! -> {
                setDivideAndConquerBronzeValue(goal?.bronzeValue!!)
                setDivideAndConquerSilverValue(goal?.silverValue!!)
                setDivideAndConquerGoldValue(goal?.value!!)

                enableIcon(goalDetailDivideAndConquerBronzeImage, R.mipmap.ic_trophy_bronze)
                enableIcon(goalDetailDivideAndConquerSilverImage, R.mipmap.ic_trophy_silver)

                if (goal?.value == goal?.goldValue!!) {
                    enableIcon(goalDetailDivideAndConquerGoldImage, R.mipmap.ic_trophy_gold)
                } else {
                    disableIcon(goalDetailDivideAndConquerGoldImage, R.mipmap.ic_trophy_gold_dark)
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
        seriesBronze = goalDetailBronzeArcView.resetValue(
            minValue = 0F,
            maxValue = goal?.bronzeValue!!,
            initialValue = 0F,
            screenMultiplier = getScreenMultiplier()
        )
    }

    private fun resetDivideAndConquerSilver() {
        seriesSilver = goalDetailSilverArcView.resetValue(
            minValue = goal?.bronzeValue!!,
            maxValue = goal?.silverValue!!,
            initialValue = goal?.bronzeValue!!,
            screenMultiplier = getScreenMultiplier()
        )
    }

    private fun resetDivideAndConquerGold() {
        seriesGold = goalDetailGoldArcView.resetValue(
            minValue = goal?.silverValue!!,
            maxValue = goal?.goldValue!!,
            initialValue = goal?.silverValue!!,
            screenMultiplier = getScreenMultiplier()
        )
    }

    private fun resetSingle() {
        seriesSingle = goalDetailSingleArcView.resetValue(
            minValue = 0F,
            maxValue = goal?.singleValue!!,
            initialValue = 0F,
            screenMultiplier = getScreenMultiplier()
        )
    }

    private fun setDivideAndConquerBronzeValue(value: Float) =
        goalDetailBronzeArcView.setup(value, seriesBronze)

    private fun setDivideAndConquerSilverValue(value: Float) =
        goalDetailSilverArcView.setup(value, seriesSilver)

    private fun setDivideAndConquerGoldValue(value: Float) =
        goalDetailGoldArcView.setup(value, seriesGold)

    private fun setSingleValue(value: Float) =
        goalDetailSingleArcView.setup(value, seriesSingle)

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
        items: MutableList<Item>
    ) {
        val item = items[position]

        swipedPosition = position

        when (direction) {
            ItemTouchHelper.RIGHT -> {
                if (item.done) {
                    doneOrUndoneItem(item)

                    count--

                    updateGoal()
                } else {
                    doneOrUndoneItem(item)

                    count++

                    updateGoal()
                }
            }
            ItemTouchHelper.LEFT -> {
                reloadItemAfterSwipe()
            }
        }
    }

    private fun doneOrUndoneItem(item: Item) {
        item.done = !item.done
        item.undoneDate = getCurrentTime()

        viewModel.saveItem(item)
    }

    private fun reloadItemAfterSwipe() {
        itemsAdapter.updateItem(swipedPosition)
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
