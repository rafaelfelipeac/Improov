package com.rafaelfelipeac.improov.features.goal.presentation.goaldetail

import android.view.View
import com.rafaelfelipeac.improov.R
import com.rafaelfelipeac.improov.core.extension.formatToDateTime
import com.rafaelfelipeac.improov.core.extension.getValueWithSymbol
import com.rafaelfelipeac.improov.core.platform.base.BaseAdapter
import com.rafaelfelipeac.improov.databinding.ListItemHistoricBinding
import com.rafaelfelipeac.improov.features.commons.domain.model.Historic

class HistoricAdapter : BaseAdapter<Historic>() {

    var clickListener: (historic: Historic) -> Unit = { }

    override fun getLayoutRes(): Int = R.layout.list_item_historic

    override fun View.bindView(item: Historic, viewHolder: ViewHolder) {
        val binding = ListItemHistoricBinding.bind(this)

        setOnClickListener { clickListener(item) }

        binding.itemHistoricDate.text = item.date?.formatToDateTime(context)
        binding.itemHistoricValue.text = item.value.getValueWithSymbol()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)

        holder.setIsRecyclable(false)
    }
}
