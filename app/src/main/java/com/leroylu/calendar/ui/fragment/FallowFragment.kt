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
import com.leroylu.calendar.model.BilibiliJumpModel
import com.leroylu.calendar.model.PushModel
import com.leroylu.calendar.model.viewmodel.FallowingViewModel
import com.leroylu.calendar.ui.adapter.item.VtuberItem
import com.leroylu.db.bean.calendar.Vtuber
import com.leroylu.struct.ui.BaseFragment
import com.leroylu.struct.ui.adapter.LoadingStateAdapter
import com.leroylu.struct.ui.adapter.PageAdapter


class FallowFragment : BaseFragment<FragmentFallowBinding>() {

    private lateinit var fallowViewModel: FallowingViewModel
    private val fallowAdapter: PageAdapter<Vtuber> by lazy {
        PageAdapter<Vtuber>(R.layout.adapter_fallowing).apply {
            addLoadStateListener {
                when (it.refresh) {
                    is LoadState.Loading -> {
                        getBinding().refreshLayout.isRefreshing = true
                    }
                    is LoadState.NotLoading -> {
                        getBinding().refreshLayout.isRefreshing = false
                    }
                }
            }
            withLoadStateFooter(
                LoadingStateAdapter(this)
            )
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_fallow
    }

    override fun initViewModel() {
        fallowViewModel = getActivityViewModel(FallowingViewModel::class.java).apply {
            jumpModel = BilibiliJumpModel(requireContext())
            pushModel = PushModel(requireContext())
        }
    }

    override fun initView() {
        getBinding().apply {

            toolbar.onActionClick(View.OnClickListener { findNavController().navigate(R.id.addFallowing) })

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
                    fallowAdapter.submitData(lifecycle, it.map { vtuber ->
                        VtuberItem(vtuber, true).apply {
                            onEdit = {
                                val data = Bundle().apply {
                                    putString("data", Gson().toJson(it))
                                    putSerializable("mode", FallowingAddFragment.Mode.Modify)
                                }
                                findNavController().navigate(R.id.addFallowing, data)
                            }
                            onDelete = {
                                fallowViewModel.deleteFallowing(it) {

                                }
                            }
                            onBrowse = { fallowViewModel.jumpModel.jumpToSpace(it) }
                        }
                    })
                }
            })
    }
}