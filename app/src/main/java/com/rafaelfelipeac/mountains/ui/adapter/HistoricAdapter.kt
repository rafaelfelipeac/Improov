package com.rafaelfelipeac.mountains.ui.adapter

import android.view.View
import android.widget.TextView
import com.rafaelfelipeac.mountains.R
import com.rafaelfelipeac.mountains.extension.convertDateToString
import com.rafaelfelipeac.mountains.extension.getValueWithSymbol
import com.rafaelfelipeac.mountains.models.Historic
import com.rafaelfelipeac.mountains.ui.base.BaseAdapter

class HistoricAdapter : BaseAdapter<Historic>() {

    var clickListener: (historic: Historic) -> Unit = { }

    override fun getLayoutRes(): Int = R.layout.list_item_historic

    override fun View.bindView(item: Historic, viewHolder: ViewHolder) {
        setOnClickListener { clickListener(item) }

        val date = viewHolder.itemView.findViewById<TextView>(R.id.historic_date)
        val value = viewHolder.itemView.findViewById<TextView>(R.id.historic_value)

        date.text = item.date?.convertDateToString()
        value.text = item.value.getValueWithSymbol()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)

        holder.setIsRecyclable(false)
    }
}