package com.rafaelfelipeac.mountains.ui.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.rafaelfelipeac.mountains.app.App
import java.util.*

abstract class BaseAdapter<T> : RecyclerView.Adapter<BaseAdapter<T>.ViewHolder>() {

    protected val items: MutableList<T> = mutableListOf()

    var touchHelper: ItemTouchHelper? = null

    var goalDAO = App.database?.goalDAO()
    var itemDAO = App.database?.itemDAO()

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(parent)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    @LayoutRes
    abstract fun getLayoutRes(): Int

    abstract fun View.bindView(item: T, viewHolder: ViewHolder)

    inner class ViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(parent.inflate(getLayoutRes())) {
        fun bind(item: T) = itemView.bindView(item, this)
    }

    fun setItems(items: List<T>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    fun getCurrentTime() = Calendar.getInstance().time!!
}

@JvmOverloads
fun ViewGroup.inflate(@LayoutRes layoutResource: Int, attachToRoot: Boolean = false): View =
    LayoutInflater.from(context).inflate(layoutResource, this, attachToRoot)
