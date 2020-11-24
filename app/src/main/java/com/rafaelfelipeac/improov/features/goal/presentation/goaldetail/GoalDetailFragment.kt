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
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rafaelfelipeac.improov.R
import com.rafaelfelipeac.improov.core.extension.visible
import com.rafaelfelipeac.improov.core.extension.gone
import com.rafaelfelipeac.improov.core.extension.invisible
import com.rafaelfelipeac.improov.core.extension.setup
import com.rafaelfelipeac.improov.core.extension.getNumberInExhibitionFormat
import com.rafaelfelipeac.improov.core.extension.toFloat
import com.rafaelfelipeac.improov.core.extension.resetValue
import com.rafaelfelipeac.improov.core.extension.isNotEmpty
import com.rafaelfelipeac.improov.core.extension.viewBinding
import com.rafaelfelipeac.improov.core.extension.enableIcon
import com.rafaelfelipeac.improov.core.extension.disableIcon
import com.rafaelfelipeac.improov.core.extension.vibrate
import com.rafaelfelipeac.improov.core.platform.base.BaseFragment
import com.rafaelfelipeac.improov.databinding.FragmentGoalDetailBinding
import com.rafaelfelipeac.improov.features.commons.data.enums.GoalType
import com.rafaelfelipeac.improov.features.commons.domain.model.Goal
import com.rafaelfelipeac.improov.features.commons.domain.model.Historic
import com.rafaelfelipeac.improov.features.commons.domain.model.Item
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
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

    private var goalNew: Boolean = false
    private var goalId: Long = 0L

    private var goal: Goal? = null
    private var item: Item? = null

    private var itemsSize: Int = 0
    private var historicsSize: Int = 0

    private var count: Float = 0F

    private val viewModel by lazy { viewModelProvider.goalDetailViewModel() }

    private var binding by viewBinding<FragmentGoalDetailBinding>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        goalId = arguments?.let { GoalDetailFragmentArgs.fromBundle(it).goalId } ?: 0L
        goalNew = arguments?.let { GoalDetailFragmentArgs.fromBundle(it).goalNew } ?: false
    }

    override fun onResume() {
        super.onResume()

        resetFistTime()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        setScreen()

        return FragmentGoalDetailBinding.inflate(inflater, container, false).run {
            binding = this
            binding.root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        goalId.let {
            if (it > 0L) {
                viewModel.setGoalId(goalId)
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
                action.goalId = goal?.goalId ?: 0L
                navController.navigate(action)
            }
            android.R.id.home -> {
                if (goalNew) {
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
        lifecycleScope.launch {
            viewModel.savedGoal.collect {
                updateProgress()
            }
        }

        lifecycleScope.launch {
            viewModel.goal.collect {
                goal = it
                setupGoal()
            }
        }

        lifecycleScope.launch {
            viewModel.savedItem.collect {
                viewModel.getItems()

                updateTextAndProgress()
            }
        }

        lifecycleScope.launch {
            viewModel.items.collect {
                itemsSize = it.size

                it.let { itemsAdapter.setItems(it) }
                setupItems()
            }
        }

        lifecycleScope.launch {
            viewModel.savedHistoric.collect {
                viewModel.getHistorics()

                updateTextAndProgress()
            }
        }

        lifecycleScope.launch {
            viewModel.historics.collect {
                historicsSize = it.size

                it.let { historicAdapter.setItems(it) }
                setupHistoric()
            }
        }
    }

    private fun resetFistTime() {
        seriesSingle = 0
        seriesBronze = 0
        seriesSilver = 0
        seriesGold = 0
    }

    private fun setupButtons() {
        binding.goalDetailBtnCounterInc.setOnClickListener {
            count += goal?.incrementValue ?: 0F

            viewModel.saveHistoric(
                Historic(
                    value = goal?.incrementValue ?: 0F,
                    date = Date(),
                    goalId = goal?.goalId ?: goalId
                )
            )

            updateGoal()
        }

        binding.goalDetailBtnCounterDec.setOnClickListener {
            count -= goal?.decrementValue ?: 0F

            viewModel.saveHistoric(
                Historic(
                    value = goal?.decrementValue?.times(-1) ?: 0F,
                    date = Date(),
                    goalId = goal?.goalId ?: goalId
                )
            )

            updateGoal()
        }

        binding.goalDetailButtonSave.setOnClickListener {
            if (binding.goalDetailTotalValue.isNotEmpty()) {
                count = goal?.value?.plus(binding.goalDetailTotalValue.toFloat()) ?: 0F

                viewModel.saveHistoric(
                    Historic(
                        value = binding.goalDetailTotalValue.toFloat(),
                        date = Date(),
                        goalId = goal?.goalId ?: goalId
                    )
                )

                updateGoal()

                binding.goalDetailTotalValue.resetValue()
            } else {
                showSnackBar(getString(R.string.goal_message_goal_value_invalid))
            }
        }
    }

    private fun setupGoal() {
        count = goal?.value ?: 0F

        binding.goalDetailTitle.text = goal?.name
        binding.goalDetailCount.text = count.getNumberInExhibitionFormat()

        goal?.divideAndConquer?.let {
            if (it) {
                binding.goalDetailSingle.invisible()
                binding.goalDetailDivideAndConquer.visible()

                binding.goalDetailDivideAndConquerBronzeText.text =
                    goal?.bronzeValue?.getNumberInExhibitionFormat()
                binding.goalDetailDivideAndConquerSilverText.text =
                    goal?.silverValue?.getNumberInExhibitionFormat()
                binding.goalDetailDivideAndConquerGoldText.text =
                    goal?.goldValue?.getNumberInExhibitionFormat()
            } else {
                binding.goalDetailSingleText.text = goal?.singleValue?.getNumberInExhibitionFormat()
            }
        }

        when (goal?.type) {
            GoalType.GOAL_LIST -> {
                binding.goalDetailCLList.visible()
                binding.goalDetailCLCounter.invisible()
                binding.goalDetailCLTotal.invisible()

                binding.goalDetailHistoricsList.invisible()

                if (main.toolbar.menu.findItem(R.id.menuAdd) == null) {
                    main.toolbar.inflateMenu(R.menu.menu_add)
                }
            }
            GoalType.GOAL_COUNTER -> {
                binding.goalDetailCLList.invisible()
                binding.goalDetailCLCounter.visible()
                binding.goalDetailCLTotal.invisible()

                binding.goalDetailHistoricsList.visible()

                binding.goalDetailCounterTotal.text = count.getNumberInExhibitionFormat()
            }
            GoalType.GOAL_FINAL -> {
                binding.goalDetailCLList.invisible()
                binding.goalDetailCLCounter.invisible()
                binding.goalDetailCLTotal.visible()

                binding.goalDetailHistoricsList.visible()
            }
            GoalType.GOAL_NONE -> {
            }
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

    private fun updateTextAndProgress() {
        updateText(getTextViewFromGoalType())
        updateProgress()

        goal?.let { viewModel.saveGoal(it) }
    }

    private fun updateText(textView: TextView) {
        binding.goalDetailCount.text = count.getNumberInExhibitionFormat()
        textView.text = count.getNumberInExhibitionFormat()
    }

    private fun updateGoal() {
        goal?.value = count

        val oldDone = goal?.done ?: false
        goal?.done = verifyIfGoalIsDone()
        verifyIfWasDone(oldDone)
    }

    private fun getTextViewFromGoalType(): TextView {
        return when (goal?.type) {
            GoalType.GOAL_LIST, GoalType.GOAL_FINAL -> {
                binding.goalDetailCount
            }
            GoalType.GOAL_COUNTER -> {
                binding.goalDetailCounterTotal
            }
            else -> binding.goalDetailCount
        }
    }

    private fun setupItems() {
        if (goal?.type == GoalType.GOAL_LIST) {
            if (itemsSize > 0) {
                setItems()
                binding.goalDetailItemsPlaceholder.invisible()
            } else {
                binding.goalDetailItemsPlaceholder.visible()
            }

            binding.goalDetailLoading.gone()
        }
    }

    private fun setupHistoric() {
        if (goal?.type == GoalType.GOAL_FINAL || goal?.type == GoalType.GOAL_COUNTER) {
            if (historicsSize > 0) {
                setHistory()
                binding.goalDetailHistoricsPlaceholder.invisible()
            } else {
                binding.goalDetailHistoricsPlaceholder.visible()
            }

            binding.goalDetailLoading.gone()
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

        binding.goalDetailItemsList.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.goalDetailItemsList.adapter = itemsAdapter

        touchHelper.attachToRecyclerView(binding.goalDetailItemsList)
    }

    private fun setHistory() {
        binding.goalDetailHistoricsList.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.goalDetailHistoricsList.adapter = historicAdapter
    }

    private fun verifyIfGoalIsDone(): Boolean {
        val value = goal?.value?.let { it } ?: 0F

        return goal?.divideAndConquer?.let { divideAndConquer ->
            if (divideAndConquer) {
                goal?.goldValue?.let { goldValue ->
                    value >= goldValue
                }
            } else {
                goal?.singleValue?.let { singleValue ->
                    value >= singleValue
                }
            }
        } ?: false
    }

    private fun updateProgress() {
        goal?.divideAndConquer?.let {
            if (it) updateDivideAndConquer() else updateSingle()
        }
    }

    private fun resetSingleOrDivideAndConquer() {
        goal?.divideAndConquer?.let {
            if (it) resetDivideAndConquer() else resetSingle()
        }
    }

    private fun updateSingle() {
        val value = goal?.value?.let { it } ?: 0F

        if (value <= 0F) {
            resetSingle()
        } else {
            goal?.singleValue?.let { singleValue ->
                if (value < singleValue) {
                    setSingleValue(value)
                    disableIcon(binding.goalDetailSingleImage, R.mipmap.ic_medal_dark)
                } else {
                    setSingleValue(singleValue)
                    enableIcon(binding.goalDetailSingleImage, R.mipmap.ic_medal)
                }
            }
        }
    }

    private fun updateDivideAndConquer() {
        goal?.value?.let { value ->
            if (value <= 0) {
                resetDivideAndConquer()
            } else {
                val bronzeValue = goal?.bronzeValue?.let { it } ?: 0F
                val silverValue = goal?.silverValue?.let { it } ?: 0F
                val goldValue = goal?.goldValue?.let { it } ?: 0F

                when {
                    value <= bronzeValue -> {
                        updateBronzeValue(value, bronzeValue)
                    }
                    value > bronzeValue && value <= silverValue -> {
                        updateSilverValue(value, bronzeValue, silverValue)
                    }
                    value > silverValue -> {
                        updateGoldValue(value, bronzeValue, silverValue, goldValue)
                    }
                }
            }
        }
    }

    private fun updateBronzeValue(value: Float, bronzeValue: Float) {
        setDivideAndConquerBronzeValue(value)

        resetDivideAndConquerSilver()

        if (value >= bronzeValue) {
            enableIcon(
                binding.goalDetailDivideAndConquerBronzeImage,
                R.mipmap.ic_trophy_bronze
            )
        } else {
            disableIcon(
                binding.goalDetailDivideAndConquerBronzeImage,
                R.mipmap.ic_trophy_bronze_dark
            )
        }
    }

    private fun updateSilverValue(
        value: Float,
        bronzeValue: Float,
        silverValue: Float
    ) {
        setDivideAndConquerBronzeValue(bronzeValue)
        setDivideAndConquerSilverValue(value)

        enableIcon(
            binding.goalDetailDivideAndConquerBronzeImage,
            R.mipmap.ic_trophy_bronze
        )

        resetDivideAndConquerGold()

        if (value >= silverValue) {
            enableIcon(
                binding.goalDetailDivideAndConquerSilverImage,
                R.mipmap.ic_trophy_silver
            )
        } else {
            disableIcon(
                binding.goalDetailDivideAndConquerSilverImage,
                R.mipmap.ic_trophy_silver_dark
            )
        }
    }

    private fun updateGoldValue(
        value: Float,
        bronzeValue: Float,
        silverValue: Float,
        goldValue: Float
    ) {
        setDivideAndConquerBronzeValue(bronzeValue)
        setDivideAndConquerSilverValue(silverValue)

        enableIcon(
            binding.goalDetailDivideAndConquerBronzeImage,
            R.mipmap.ic_trophy_bronze
        )
        enableIcon(
            binding.goalDetailDivideAndConquerSilverImage,
            R.mipmap.ic_trophy_silver
        )

        if (value >= goldValue) {
            setDivideAndConquerGoldValue(goldValue)
            enableIcon(
                binding.goalDetailDivideAndConquerGoldImage,
                R.mipmap.ic_trophy_gold
            )
        } else {
            setDivideAndConquerGoldValue(value)
            disableIcon(
                binding.goalDetailDivideAndConquerGoldImage,
                R.mipmap.ic_trophy_gold_dark
            )
        }
    }

    private fun resetDivideAndConquer() {
        disableIcon(
            binding.goalDetailDivideAndConquerBronzeImage,
            R.mipmap.ic_trophy_bronze_dark
        )

        disableIcon(
            binding.goalDetailDivideAndConquerSilverImage,
            R.mipmap.ic_trophy_silver_dark
        )

        disableIcon(
            binding.goalDetailDivideAndConquerGoldImage,
            R.mipmap.ic_trophy_gold_dark
        )

        resetDivideAndConquerBronze()
        resetDivideAndConquerSilver()
        resetDivideAndConquerGold()
    }

    private fun resetDivideAndConquerBronze() {
        seriesBronze = binding.goalDetailBronzeArcView.resetValue(
            minValue = 0F,
            maxValue = goal?.bronzeValue,
            initialValue = 0F,
            screenMultiplier = getScreenMultiplier()
        )
    }

    private fun resetDivideAndConquerSilver() {
        seriesSilver = binding.goalDetailSilverArcView.resetValue(
            minValue = goal?.bronzeValue,
            maxValue = goal?.silverValue,
            initialValue = goal?.bronzeValue,
            screenMultiplier = getScreenMultiplier()
        )
    }

    private fun resetDivideAndConquerGold() {
        seriesGold = binding.goalDetailGoldArcView.resetValue(
            minValue = goal?.silverValue,
            maxValue = goal?.goldValue,
            initialValue = goal?.silverValue,
            screenMultiplier = getScreenMultiplier()
        )
    }

    private fun resetSingle() {
        seriesSingle = binding.goalDetailSingleArcView.resetValue(
            minValue = 0F,
            maxValue = goal?.singleValue,
            initialValue = 0F,
            screenMultiplier = getScreenMultiplier()
        )
    }

    private fun setDivideAndConquerBronzeValue(value: Float?) =
        binding.goalDetailBronzeArcView.setup(value, seriesBronze)

    private fun setDivideAndConquerSilverValue(value: Float?) =
        binding.goalDetailSilverArcView.setup(value, seriesSilver)

    private fun setDivideAndConquerGoldValue(value: Float?) =
        binding.goalDetailGoldArcView.setup(value, seriesGold)

    private fun setSingleValue(value: Float) =
        binding.goalDetailSingleArcView.setup(value, seriesSingle)

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

        if (item.done) {
            item.doneDate = getCurrentTime()
        } else {
            item.undoneDate = getCurrentTime()
        }

        reloadItemAfterSwipe()
        updateTextAndProgress()

        viewModel.saveItem(item)
    }

    private fun reloadItemAfterSwipe() {
        itemsAdapter.updateItem(swipedPosition)
    }

    private fun deleteItem(item: Any) {
        viewModel.saveItem(item as Item)
    }

    private fun verifyIfWasDone(oldDone: Boolean) {
        goal?.done?.let {
            if (!oldDone && it) {
                showSnackBar(getString(R.string.goal_message_goal_done))
            } else {
                showSnackBar(getString(R.string.goal_message_goal_value_updated))

                hideSoftKeyboard()
            }
        }
    }

    private fun newItem(name: String) {
        val item =
            Item(
                goalId = goal?.goalId ?: goalId,
                name = name,
                done = false,
                order = itemsSize.plus(1),
                createdDate = getCurrentTime()
            )

        viewModel.saveItem(item)
    }

    private fun updateItem(name: String) {
        val item = this.item
        item?.name = name
        item?.updatedDate = getCurrentTime()

        item?.let { viewModel.saveItem(it) }
    }

    private fun getScreenMultiplier(): Float {
        val metrics = DisplayMetrics()
        activity?.windowManager?.defaultDisplay?.getMetrics(metrics)

        return metrics.density
    }
}
