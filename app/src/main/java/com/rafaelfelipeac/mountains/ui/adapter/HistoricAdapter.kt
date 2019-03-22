package com.rafaelfelipeac.mountains.ui.adapter

import android.annotation.SuppressLint
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.rafaelfelipeac.mountains.R
import com.rafaelfelipeac.mountains.extension.getValueWithSymbol
import com.rafaelfelipeac.mountains.models.Historic
import com.rafaelfelipeac.mountains.ui.base.BaseAdapter
import java.text.SimpleDateFormat

class HistoricAdapter(private val fragment: Fragment) : BaseAdapter<Historic>() {

    var clickListener: (historic: Historic) -> Unit = { }

    override fun getLayoutRes(): Int = R.layout.list_item_historic

    override fun View.bindView(item: Historic, viewHolder: ViewHolder) {
        setOnClickListener { clickListener(item) }

        val date = viewHolder.itemView.findViewById<TextView>(R.id.historic_date)
        val value = viewHolder.itemView.findViewById<TextView>(R.id.historic_value)

        val sdf = SimpleDateFormat("dd/MM/yy - HH:mm:ss")
        val currentDate = sdf.format(item.date)

        date.text = currentDate
        value.text = item.value.getValueWithSymbol()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)

        holder.setIsRecyclable(false)
    }
}