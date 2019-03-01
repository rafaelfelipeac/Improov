package com.rafaelfelipeac.domore.ui.adapter

import android.annotation.SuppressLint
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.rafaelfelipeac.domore.R
import com.rafaelfelipeac.domore.models.Historic
import com.rafaelfelipeac.domore.ui.base.BaseAdapter
import java.text.SimpleDateFormat

class HistoricAdapter(private val fragment: Fragment) : BaseAdapter<Historic>() {

    var clickListener: (historic: Historic) -> Unit = { }

    override fun getLayoutRes(): Int = R.layout.list_item_historic

    override fun View.bindView(historic: Historic, viewHolder: ViewHolder) {
        setOnClickListener { clickListener(historic) }

        val date = viewHolder.itemView.findViewById<TextView>(R.id.historic_date)
        val value = viewHolder.itemView.findViewById<TextView>(R.id.historic_value)

        val sdf = SimpleDateFormat("dd/M/yyyy")
        val currentDate = sdf.format(historic.date)

        date.text = currentDate
        value.text = historic.value.toString()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)

        holder.setIsRecyclable(false)
    }
}