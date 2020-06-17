package com.rafaelfelipeac.improov.core.platform

import androidx.recyclerview.widget.RecyclerView

interface ActionCompleteContract {
    fun onViewMoved(fromPosition: Int, toPosition: Int)

    fun onViewSwiped(position: Int, direction: Int, holder: RecyclerView.ViewHolder)
}
