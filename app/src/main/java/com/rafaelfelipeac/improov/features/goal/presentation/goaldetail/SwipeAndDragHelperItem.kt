package com.rafaelfelipeac.improov.features.goal.presentation.goaldetail

import android.graphics.Canvas
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ItemTouchHelper
import com.rafaelfelipeac.improov.R
import com.rafaelfelipeac.improov.core.platform.ActionCompleteContract
import kotlin.math.abs

class SwipeAndDragHelperItem(private val contract: ActionCompleteContract) :
    ItemTouchHelper.Callback() {

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        val swipeFlags = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT

        return makeMovementFlags(dragFlags, swipeFlags)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {

        contract.onViewMoved(viewHolder.adapterPosition, target.adapterPosition)

        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        contract.onViewSwiped(viewHolder.adapterPosition, direction, viewHolder)
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        val foregroundView =
            viewHolder?.itemView?.findViewById<ConstraintLayout>(R.id.itemItemNormalView)

        if (foregroundView != null) {
            getDefaultUIUtil()
                .onSelected(foregroundView)
        }
    }

    override fun onChildDrawOver(
        canvas: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {

        val foregroundView =
            viewHolder.itemView.findViewById<ConstraintLayout>(R.id.itemItemNormalView)

        if (foregroundView != null) {
            getDefaultUIUtil()
                .onDrawOver(canvas, recyclerView, foregroundView, dX, dY, actionState, isCurrentlyActive)
        }
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        val foregroundView =
            viewHolder.itemView.findViewById<ConstraintLayout>(R.id.itemItemNormalView)

        if (foregroundView != null) {
            getDefaultUIUtil().clearView(foregroundView)
        }
    }

    override fun onChildDraw(
        canvas: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {

        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            val alpha = 1 - abs(dX) / recyclerView.width
            viewHolder.itemView.alpha = alpha
        }

        val foregroundView =
            viewHolder.itemView.findViewById<ConstraintLayout>(R.id.itemItemNormalView)

        if (foregroundView != null) {
            getDefaultUIUtil()
                .onDraw(canvas, recyclerView, foregroundView, dX, 0f, actionState, isCurrentlyActive)

            super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }
    }

    override fun isLongPressDragEnabled() = false

    override fun isItemViewSwipeEnabled() = true
}
