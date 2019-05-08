package com.rafaelfelipeac.mountains.ui.fragments.goal

import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.textfield.TextInputEditText
import com.rafaelfelipeac.mountains.R
import com.rafaelfelipeac.mountains.extension.*
import com.rafaelfelipeac.mountains.models.Goal
import com.rafaelfelipeac.mountains.models.Historic
import com.rafaelfelipeac.mountains.models.Item
import com.rafaelfelipeac.mountains.ui.activities.MainActivity
import com.rafaelfelipeac.mountains.ui.adapter.HistoricAdapter
import com.rafaelfelipeac.mountains.ui.adapter.ItemsAdapter
import com.rafaelfelipeac.mountains.ui.base.BaseFragment
import com.rafaelfelipeac.mountains.ui.fragments.goalForm.GoalFormFragmentArgs
import com.rafaelfelipeac.mountains.ui.helper.SwipeAndDragHelperItem
import kotlinx.android.synthetic.main.fragment_goal.*
import java.util.*

class GoalFragment : BaseFragment() {

    private var itemsAdapter = ItemsAdapter(this)
    private var historicAdapter = HistoricAdapter()

    private var seriesSingle: Int = 0
    private var seriesBronze: Int = 0
    private var seriesSilver: Int = 0
    private var seriesGold: Int = 0

    private var bottomSheetItem: BottomSheetBehavior<*>? = null
    private var bottomSheetItemClose: ImageView? = null
    private var bottomSheetItemSave: Button? = null
    private var bottomSheetItemName: TextInputEditText? = null

    var goalId: Long? = null
    var goal: Goal? = null
    var goals: List<Goal>? = null
    var items: List<Item>? = null
    var history: List<Historic>? = null

    private var count: Float = 0F

    private lateinit var viewModel: GoalViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        injector.inject(this)

        setHasOptionsMenu(true)

        (activity as MainActivity).bottomNavigationVisible(View.GONE)

        goalId = arguments?.let { GoalFragmentArgs.fromBundle(it).goalId }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        (activity as MainActivity).supportActionBar?.title = getString(R.string.fragment_title_goal)

        viewModel = ViewModelProviders.of(this).get(GoalViewModel::class.java)
        viewModel.init(goalId!!)

        return inflater.inflate(R.layout.fragment_goal, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.getGoal()?.observe(this, Observer { goal ->
            this.goal = goal

            setupGoal()
            setupBottomSheet()
        })

        viewModel.getGoals()?.observe(this, Observer { goals ->
            this.goals = goals
        })

        viewModel.getItems()?.observe(this, Observer { items ->
            this.items = items

            setupItems()
        })

        viewModel.getHistory()?.observe(this, Observer { history ->
            this.history = history

            setHistory()
        })
    }

    override fun onStart() {
        super.onStart()

        hideNavigation()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        if (goal?.type == 1) {
            inflater.inflate(R.menu.menu_add, menu)
        }

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
                navController.navigate(GoalFragmentDirections.actionNavigationGoalToNavigationGoalForm(goal?.goalId!!))
            }
//            android.R.id.home -> {
//                navController.navigateUp()
//            }
        }

        return false
    }

    private fun setupButtons() {
        goal_btn_inc.setOnClickListener {
            count += goal?.incrementValue!!
            updateTextAndGoal(goal_inc_dec_total)

            viewModel.saveHistoric(Historic(value = goal?.incrementValue!!, date = Date(), goalId = goal?.goalId!!))
        }

        goal_btn_dec.setOnClickListener {
            count -= goal?.decrementValue!!
            updateTextAndGoal(goal_inc_dec_total)

            viewModel.saveHistoric(Historic(value = goal?.decrementValue!! * -1, date = Date(), goalId = goal?.goalId!!))
        }

        goal_btn_save.setOnClickListener {
            if (goal_total_total.isNotEmpty()) {
                count = goal?.value!! + goal_total_total.toFloat()

                goal_total_total.resetValue()

                val oldDone = goal!!.done
                goal?.done = verifyIfGoalIsDone()

                updateTextAndGoal(goal_count)

                viewModel.saveHistoric(Historic(value = goal_total_total.toFloat(), date = Date(), goalId = goal?.goalId!!))

                if (!oldDone && goal!!.done)
                    showSnackBar(getString(R.string.message_goal_done))
                else
                    showSnackBar(getString(R.string.message_goal_value_updated))
            } else {
                showSnackBar(getString(R.string.message_goal_value_invalid))
            }
        }
    }

    private fun onScoreFromList(done: Boolean) {
        if (done) count++ else count--
        updateTextAndGoal(goal_count)
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
            bottomSheetItemName?.resetValue()
            (activity as MainActivity).closeBottomSheetItem()
        }

        bottomSheetItemSave?.setOnClickListener {
            if (bottomSheetItemName?.isEmpty()!!) {
                showSnackBar(getString(R.string.bottom_sheet_empty_item_name))
            } else {
                var item = Item()

                if ((activity as MainActivity).item == null) {
                    item.goalId = goal?.goalId!!
                    item.name = bottomSheetItemName?.text.toString()
                    item.done = false
                    item.order = items?.filter { it.goalId == goal?.goalId }!!.size + 1
                    item.createdDate = getCurrentTime()
                } else {
                    item = (activity as MainActivity).item!!
                    item.name = bottomSheetItemName?.text.toString()
                    item.updatedDate = getCurrentTime()
                }

                viewModel.saveItem(item)

                bottomSheetItemName?.resetValue()
                (activity as MainActivity).closeBottomSheetItem()
            }
        }
    }

    private fun setupGoal() {
        count = goal?.value!!

        goal_title.text = goal?.name
        goal_count.text = count.getNumberInRightFormat()

        if (goal?.mountains!!) {
            goal_single.invisible()
            goal_mountains.visible()

            goal_mountain_bronze_text.text = goal?.bronzeValue?.getNumberInRightFormat()
            goal_mountain_silver_text.text = goal?.silverValue?.getNumberInRightFormat()
            goal_mountain_gold_text.text = goal?.goldValue?.getNumberInRightFormat()
        } else {
            goal_single_text.text = goal?.singleValue?.getNumberInRightFormat()
        }

        when (goal?.type) {
            1 -> {
                goal_cl_list.visible()
                goal_cl_dec_inc.invisible()
                goal_cl_total.invisible()

                if ((activity as MainActivity).toolbar.menu.findItem(R.id.menu_goal_add) == null)
                    (activity as MainActivity).toolbar.inflateMenu(R.menu.menu_add)
            }
            2 -> {
                goal_cl_list.invisible()
                goal_cl_dec_inc.visible()
                goal_cl_total.invisible()

                goal_inc_dec_total.text = count.getNumberInRightFormat()
            }
            3 -> {
                goal_cl_list.invisible()
                goal_cl_dec_inc.invisible()
                goal_cl_total.visible()
            }
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

        viewModel.saveGoal(goal!!)
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

    private fun setupItems() {
        if (goal?.type == 1) {
            if (items?.any { it.goalId == goal?.goalId }!!) {
                setItems()

                goal_cl_list.visible()
                items_placeholder.invisible()
            } else {
                goal_cl_list.invisible()
                items_placeholder.visible()
            }
        }
    }

    private fun setItems() {
        items
            ?.filter { it.goalId == goal?.goalId }
            ?.sortedBy { it.order }
            ?.let { itemsAdapter.setItems(it) }

        itemsAdapter.clickListener = { (activity as MainActivity).openBottomSheetItem(it) }

        val swipeAndDragHelper = SwipeAndDragHelperItem(itemsAdapter)
        val touchHelper = ItemTouchHelper(swipeAndDragHelper)

        itemsAdapter.touchHelper = touchHelper

        goal_items_list.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        goal_items_list.adapter = itemsAdapter

        touchHelper.attachToRecyclerView(goal_items_list)
    }

    private fun setHistory() {
        history
            ?.filter { it.goalId == goal?.goalId }
            ?.reversed()
            ?.let { historicAdapter.setItems(it) }

        historic_items_list.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        historic_items_list.adapter = historicAdapter
    }

    private fun verifyIfGoalIsDone() =
        ((goal!!.mountains && goal!!.value >= goal!!.goldValue) ||
                (!goal!!.mountains && goal!!.value >= goal!!.singleValue))

    private fun updateSingleOrMountains() {
        if (goal?.mountains!!) {
            updateMountains()
        } else {
            updateSingle()
        }
    }

    private fun resetSingleOrMountains() {
        if (goal?.mountains!!) {
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
                disableIcon(goal_single_image, R.mipmap.ic_single_dark)
            }
            goal?.value!! > 0 && goal?.value!! >= goal?.singleValue!! -> {
                setSingleValue(goal?.singleValue!!)
                enableIcon(goal_single_image, R.mipmap.ic_single)
            }
        }
    }

    private fun updateMountains() {
        when {
            goal?.value!! == 0F -> {
                resetMountains()
            }
            goal?.value!! > 0 && goal?.value!! <= goal?.bronzeValue!! -> {
                setMountainBronzeValue(goal?.value!!)

                if (goal?.value == goal?.bronzeValue!!) enableIcon(goal_mountain_bronze_image, R.mipmap.ic_mountain_bronze)
                else disableIcon(goal_mountain_bronze_image, R.mipmap.ic_mountain_bronze_dark)

                resetMountainSilver()
            }
            goal?.value!! > 0 && goal?.value!! <= goal?.silverValue!! -> {
                setMountainBronzeValue(goal?.bronzeValue!!)
                setMountainSilverValue(goal?.value!!)

                enableIcon(goal_mountain_bronze_image, R.mipmap.ic_mountain_bronze)

                if (goal?.value == goal?.silverValue!!) enableIcon(goal_mountain_silver_image, R.mipmap.ic_mountain_silver)
                else disableIcon(goal_mountain_silver_image, R.mipmap.ic_mountain_silver_dark)

                resetMountainGold()
            }
            goal?.value!! > 0 && goal?.value!! <= goal?.goldValue!! -> {
                setMountainBronzeValue(goal?.bronzeValue!!)
                setMountainSilverValue(goal?.silverValue!!)
                setMountainGoldValue(goal?.value!!)

                enableIcon(goal_mountain_bronze_image, R.mipmap.ic_mountain_bronze)
                enableIcon(goal_mountain_silver_image, R.mipmap.ic_mountain_silver)

                if (goal?.value == goal?.goldValue!!) enableIcon(goal_mountain_gold_image, R.mipmap.ic_mountain_gold)
                else disableIcon(goal_mountain_gold_image, R.mipmap.ic_mountain_gold_dark)
            }
            else -> {
                setMountainBronzeValue(goal?.bronzeValue!!)
                setMountainSilverValue(goal?.silverValue!!)
                setMountainGoldValue(goal?.goldValue!!)

                enableIcon(goal_mountain_bronze_image, R.mipmap.ic_mountain_bronze)
                enableIcon(goal_mountain_silver_image, R.mipmap.ic_mountain_silver)
                enableIcon(goal_mountain_gold_image, R.mipmap.ic_mountain_gold)
            }
        }
    }

    private fun resetMountains() {
        resetMountainBronze()
        resetMountainSilver()
        resetMountainGold()
    }

    private fun resetMountainBronze() {
        seriesBronze = arcViewBronze.resetValue(0F, goal?.bronzeValue!!, 0F)
    }

    private fun resetMountainSilver() {
        seriesSilver = arcViewSilver.resetValue(goal?.bronzeValue!!, goal?.silverValue!!, goal?.bronzeValue!!)
    }

    private fun resetMountainGold() {
        seriesGold = arcViewGold.resetValue(goal?.silverValue!!, goal?.goldValue!!, goal?.silverValue!!)
    }

    private fun resetSingle() {
        seriesSingle = arcViewSingle.resetValue(0F, goal?.singleValue!!, 0F)
    }

    private fun setMountainBronzeValue(value: Float) = arcViewBronze.setup(value, seriesBronze)

    private fun setMountainSilverValue(value: Float) = arcViewSilver.setup(value, seriesSilver)

    private fun setMountainGoldValue(value: Float) = arcViewGold.setup(value, seriesGold)

    private fun setSingleValue(value: Float) = arcViewSingle.setup(value, seriesSingle)

    private fun enableIcon(image: ImageView, iconNormal: Int) {
        image.enableIcon(iconNormal, context!!)
    }

    private fun disableIcon(image: ImageView, iconDark: Int) {
        image.disableIcon( iconDark, context!!)
    }

    fun onViewMoved(oldPosition: Int, newPosition: Int, items: MutableList<Item>,
                    function: (oldPosition: Int, newPosition: Int) -> Unit) {
        val targetItem = items[oldPosition]
        val otherItem = items[newPosition]

        targetItem.order = newPosition
        otherItem.order = oldPosition

        viewModel.saveItem(targetItem)
        viewModel.saveItem(otherItem)

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
                item.deleteDate = getCurrentTime()

                viewModel.deleteItem(item)

                showSnackBarWithAction(holder.itemView, getString(R.string.item_swiped_deleted), item, ::deleteItem)
            }
        }
    }

    private fun deleteItem(item: Any) {
        viewModel.saveItem(item as Item)
    }
}