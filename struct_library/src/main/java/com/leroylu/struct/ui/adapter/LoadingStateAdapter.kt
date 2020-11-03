package com.leroylu.struct.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.leroylu.struct.R
import com.leroylu.struct.databinding.AdapterLoadingFootBinding

/**
 * @author jiaj.lu
 * @date 2020/8/26
 * @description
 */
class LoadingStateAdapter(
    private val dataAdapter: PageAdapter<*>
) : LoadStateAdapter<LoadingStateAdapter.LoadingStateHolder>() {

    class LoadingStateHolder(
        itemView: View,
        val binding: AdapterLoadingFootBinding?
    ) : RecyclerView.ViewHolder(itemView)

    override fun onBindViewHolder(holder: LoadingStateHolder, loadState: LoadState) {
        holder.binding?.progress?.isVisible = loadState is LoadState.Loading
        holder.binding?.btn?.apply {
            isVisible = loadState is LoadState.Error
            setOnClickListener { dataAdapter.retry() }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadingStateHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_loading_foot, parent, false)
        return LoadingStateHolder(
            view,
            DataBindingUtil.bind(view)
        )
    }
}