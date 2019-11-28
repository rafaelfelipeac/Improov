package com.rafaelfelipeac.improov.features.goal.presentation.goal

import android.view.View
import android.widget.TextView
import com.rafaelfelipeac.improov.R
import com.rafaelfelipeac.improov.core.extension.convertDateToString
import com.rafaelfelipeac.improov.core.extension.getValueWithSymbol
import com.rafaelfelipeac.improov.features.goal.Historic
import com.rafaelfelipeac.improov.core.platform.base.BaseAdapter

class HistoricAdapter : BaseAdapter<Historic>() {

    var clickListener: (historic: Historic) -> Unit = { }

    override fun getLayoutRes(): Int = R.layout.list_item_historic

    override fun View.bindView(item: Historic, viewHolder: ViewHolder) {
        setOnClickListener { clickListener(item) }

        val date = viewHolder.itemView.findViewById<TextView>(R.id.historic_date)
        val value = viewHolder.itemView.findViewById<TextView>(R.id.historic_value)

        date.text = item.date?.convertDateToString(context)
        value.text = item.value.getValueWithSymbol()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)

        holder.setIsRecyclable(false)
    }
}
