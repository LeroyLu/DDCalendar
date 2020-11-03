package com.leroylu.struct.ui.adapter

import android.content.Context
import androidx.databinding.ViewDataBinding

/**
 * @author jiaj.lu
 * @date 2020/9/15
 * @description
 */
abstract class SelectableAdapter<T, Binding : ViewDataBinding>(
    ctx: Context,
    layoutId: Int,
    brId: Int,
    private val singleSelect: Boolean = true
) : BaseAdapter<T, Binding>(ctx, layoutId, brId) {

    private val selected = ArrayList<T>()

    init {
        itemOperator = object : ItemOperator<T>() {
            override fun onItemClick(index: Int, item: T) {
                select(index, item)
            }
        }
    }

    private fun select(index: Int, item: T) {
        if (selected.contains(item)) {
            selected.remove(item)
            notifyItemChanged(index)
        } else {
            if (singleSelect)
                selected.clear()
            selected.add(item)
            notifyDataSetChanged()
        }
        onItemSelectedChange(selected)
        onItemClick()
    }

    fun setSelected(items: List<T>) {
        selected.apply {
            clear()
            addAll(items)
        }
        notifyDataSetChanged()
    }

    override fun remove(index: Int) {
        selected.remove(data[index])
        super.remove(index)
    }

    override fun removeAll(items: List<T>) {
        selected.removeAll(items)
        super.removeAll(items)
    }

    open fun onItemClick() {

    }

    open fun onItemSelectedChange(items: List<T>) {

    }

    fun getSelectedItem() = selected

    abstract fun bindItemSelected(binding: Binding?, item: T)
    abstract fun bindItemSelectDefault(binding: Binding?, item: T)

    override fun onBindViewHolder(holder: ItemHolder<Binding>, position: Int) {
        super.onBindViewHolder(holder, position)
        val item = data[position]
        if (selected.contains(item)) {
            bindItemSelected(holder.binding, item)
        } else {
            bindItemSelectDefault(holder.binding, item)
        }
    }
}