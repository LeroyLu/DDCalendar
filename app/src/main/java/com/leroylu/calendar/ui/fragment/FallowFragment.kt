package com.leroylu.calendar.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.map
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.leroylu.calendar.R
import com.leroylu.calendar.databinding.FragmentFallowBinding
import com.leroylu.calendar.model.FallowingViewModel
import com.leroylu.calendar.ui.adapter.item.VtuberItem
import com.leroylu.db.bean.calendar.Vtuber
import com.leroylu.struct.ui.BaseFragment
import com.leroylu.struct.ui.adapter.LoadingStateAdapter
import com.leroylu.struct.ui.adapter.PageAdapter


class FallowFragment : BaseFragment<FragmentFallowBinding>() {

    private lateinit var fallowViewModel: FallowingViewModel
    private lateinit var fallowAdapter: PageAdapter<Vtuber>

    override fun getLayoutId(): Int {
        return R.layout.fragment_fallow
    }

    override fun initViewModel() {
        fallowViewModel = getActivityViewModel(FallowingViewModel::class.java)
    }

    override fun initView() {
        getBinding().apply {

            toolbar.onActionClick(View.OnClickListener { findNavController().navigate(R.id.addFallowing) })

            fallowAdapter = PageAdapter<Vtuber>(R.layout.adapter_fallowing).apply {
                addLoadStateListener {
                    when (it.refresh) {
                        is LoadState.Loading -> {
                            refreshLayout.isRefreshing = true
                        }
                        is LoadState.NotLoading -> {
                            refreshLayout.isRefreshing = false
                        }
                    }
                }
                withLoadStateFooter(
                    LoadingStateAdapter(this)
                )
            }

            recyclerView.apply {
                layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                adapter = fallowAdapter
            }

            refreshLayout.setOnRefreshListener { fallowAdapter.refresh() }
        }
    }

    override fun initObserver() {
        fallowViewModel.getFallowing()
            .observe(this, Observer {
                lifecycleScope.launchWhenCreated {
                    fallowAdapter.submitData(lifecycle, it.map {
                        VtuberItem(it, true).apply {
                            onEdit = { v ->
                                val data = Bundle().apply {
                                    putString("data", Gson().toJson(v))
                                    putSerializable("mode", FallowingAddFragment.Mode.Modify)
                                }
                                findNavController().navigate(R.id.addFallowing, data)
                            }
                            onDelete = { v ->
                                fallowViewModel.deleteFallowing(v) {

                                }
                            }
                        }
                    })
                }
            })
    }
}