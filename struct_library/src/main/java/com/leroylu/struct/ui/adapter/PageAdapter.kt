package com.leroylu.struct.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

/**
 * @author jiaj.lu
 * @date 2020/8/26
 * @description
 */
open class PageAdapter<T>(
    private val layoutId: Int
) :
    PagingDataAdapter<PageAdapter.PagingItem<T>, PageAdapter.PagingItemHolder>(object :
        DiffUtil.ItemCallback<PagingItem<T>>() {
        override fun areItemsTheSame(oldItem: PagingItem<T>, newItem: PagingItem<T>): Boolean {
            return oldItem.areItemsTheSame(newItem.self)
        }

        override fun areContentsTheSame(oldItem: PagingItem<T>, newItem: PagingItem<T>): Boolean {
            return oldItem.areContentsTheSame(newItem.self)
        }

    }) {

    abstract class PagingItem<T>(open val self: T) {
        private lateinit var holder: PagingItemHolder

        open fun onBindView(holder: PagingItemHolder, position: Int) {
            this.holder = holder
        }

        abstract fun areItemsTheSame(data: T): Boolean

        abstract fun areContentsTheSame(data: T): Boolean

    }

    class PagingItemHolder(
        itemView: View,
        val ctx: Context,
        val binding: ViewDataBinding?
    ) :
        RecyclerView.ViewHolder(itemView)

    override fun onBindViewHolder(holder: PagingItemHolder, position: Int) {
        getItem(position)?.onBindView(holder, position)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagingItemHolder {
        val view = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        val binding = DataBindingUtil.bind<ViewDataBinding>(view)
        return PagingItemHolder(
            view,
            parent.context,
            binding
        )
    }
}