package com.leroylu.struct.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

/**
 * @author jiaj.lu
 * @date 2020/9/14
 * @description
 */
abstract class BaseAdapter<T, Binding : ViewDataBinding>(
    private val ctx: Context,
    private val layoutId: Int,
    private val brId: Int,
) : RecyclerView.Adapter<BaseAdapter.ItemHolder<Binding>>() {

    val data = ArrayList<T>()

    fun getDataSet(): List<T> = data

    fun add(item: T) {
        data.add(item)
        notifyItemInserted(data.size - 1)
    }

    fun addAll(items: List<T>) {
        val start = data.size - 1
        data.addAll(items)
        notifyItemRangeInserted(start, items.size)
    }

    open fun remove(index: Int) {
        data.removeAt(index)
        notifyItemRemoved(index)
    }

    open fun removeAll(items: List<T>) {
        data.removeAll(items)
        notifyDataSetChanged()
    }

    fun clear() {
        val size = data.size
        data.clear()
        notifyItemRangeRemoved(0, size)
    }

    var itemOperator: ItemOperator<T>? = null

    class ItemHolder<Binding>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding: Binding? = null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder<Binding> {
        val binding =
            DataBindingUtil.inflate<Binding>(LayoutInflater.from(ctx), layoutId, parent, false)
        return ItemHolder<Binding>(
            binding.root
        ).apply {
            this.binding = binding
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ItemHolder<Binding>, position: Int) {
        holder.binding?.setVariable(brId, data[position])
        initView(holder, data[position])
        holder.itemView.setOnClickListener {
            itemOperator?.onItemClick(
                position,
                data[position]
            )
        }
        holder.binding?.executePendingBindings()
    }

    abstract class ItemOperator<T> {
        open fun onItemClick(index: Int, item: T) {}
    }

    open fun initView(holder: ItemHolder<Binding>, item: T) {}
}