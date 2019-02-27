package com.rafaelfelipeac.domore.ui.helper

import android.graphics.Canvas
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ItemTouchHelper
import com.rafaelfelipeac.domore.R

class SwipeAndDragHelperGoal(private val contract: ActionCompletionContract) : ItemTouchHelper.Callback() {

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        val swipeFlags = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT

        return ItemTouchHelper.Callback.makeMovementFlags(dragFlags, swipeFlags)
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                        target: RecyclerView.ViewHolder): Boolean {

        contract.onViewMoved(viewHolder.adapterPosition, target.adapterPosition)
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        contract.onViewSwiped(viewHolder.adapterPosition, direction, viewHolder)
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        ItemTouchHelper.Callback.getDefaultUIUtil()
            .onSelected(viewHolder?.itemView?.findViewById<ConstraintLayout>(R.id.goal_normal_view))
    }

    override fun onChildDrawOver(c: Canvas, recyclerView: RecyclerView,
                                 viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float,
                                 actionState: Int, isCurrentlyActive: Boolean) {

        val foregroundView = viewHolder.itemView.findViewById<ConstraintLayout>(R.id.goal_normal_view)

        ItemTouchHelper.Callback.getDefaultUIUtil()
            .onDrawOver(c, recyclerView, foregroundView, dX, dY, actionState, isCurrentlyActive)
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        ItemTouchHelper.Callback.getDefaultUIUtil()
            .clearView(viewHolder.itemView.findViewById<ConstraintLayout>(R.id.goal_normal_view))
    }

    override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                             dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {

        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            val alpha = 1 - Math.abs(dX) / recyclerView.width
            viewHolder.itemView.alpha = alpha
        }

        val foregroundView = viewHolder.itemView.findViewById<ConstraintLayout>(R.id.goal_normal_view)

        ItemTouchHelper.Callback.getDefaultUIUtil()
            .onDraw(c, recyclerView, foregroundView, dX, 0f, actionState, isCurrentlyActive)

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    override fun isLongPressDragEnabled() = false

    override fun isItemViewSwipeEnabled() = true
}
