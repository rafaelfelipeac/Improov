package com.rafaelfelipeac.domore.ui.base

import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rafaelfelipeac.domore.app.App

abstract class BaseAdapter<T> : RecyclerView.Adapter<BaseAdapter<T>.ViewHolder>() {

    protected val items: MutableList<T> = mutableListOf()

    var goalDAO = App.database?.goalDAO()
    var itemDAO = App.database?.itemDAO()

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(parent)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun setItems(items: List<T>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    @LayoutRes
    abstract fun getLayoutRes(): Int

    abstract fun View.bindView(item: T, viewHolder: ViewHolder)

    inner class ViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(parent.inflate(getLayoutRes())) {
        fun bind(item: T) = itemView.bindView(item, this)
    }
}

@JvmOverloads
fun ViewGroup.inflate(@LayoutRes layoutResource: Int, attachToRoot: Boolean = false): View =
    LayoutInflater.from(context).inflate(layoutResource, this, attachToRoot)
