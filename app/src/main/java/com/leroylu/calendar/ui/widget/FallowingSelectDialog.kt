package com.leroylu.calendar.ui.widget

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.leroylu.calendar.R
import com.leroylu.calendar.databinding.DialogFallowingSelectBinding
import com.leroylu.db.bean.calendar.Vtuber
import com.leroylu.struct.ui.adapter.LoadingStateAdapter
import com.leroylu.struct.ui.adapter.PageAdapter

/**
 * @author jiaj.lu
 * @date 2020/10/30
 * @description
 */
class FallowingSelectDialog(private val ctx: Context) : LifecycleObserver {

    private var dialog: CustomDialog? = null

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private fun destroy() {
        dialog?.apply {
            dismiss()
        }
    }

    fun show(fallowAdapter: PageAdapter<Vtuber>) {
        if (dialog == null) {
            dialog = CustomDialog(ctx, fallowAdapter)
            dialog?.show()
        } else {
            dialog?.show()
        }
    }

    fun hide() {
        dialog?.hide()
    }

    fun onCancel(listener: DialogInterface.OnCancelListener) {
        dialog?.setOnCancelListener(listener)
    }

    private class CustomDialog(
        context: Context,
        val fallowAdapter: PageAdapter<Vtuber>
    ) : Dialog(context) {

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            val root = LayoutInflater.from(context).inflate(R.layout.dialog_fallowing_select, null)
            val binding = DataBindingUtil.bind<DialogFallowingSelectBinding>(root)

            fallowAdapter.apply {
                addLoadStateListener {
                    when (it.refresh) {
                        is LoadState.Loading -> {
                            binding?.refreshLayout?.isRefreshing = true
                        }
                        is LoadState.NotLoading -> {
                            binding?.refreshLayout?.isRefreshing = false
                        }
                    }
                }
                withLoadStateFooter(
                    LoadingStateAdapter(
                        this
                    )
                )
            }

            binding?.apply {
                recyclerView.apply {
                    layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                    adapter = fallowAdapter
                }
                refreshLayout.setOnRefreshListener { fallowAdapter.refresh() }

            }

            setContentView(root)
            setCanceledOnTouchOutside(true)

        }

        override fun onAttachedToWindow() {
            window?.apply {
                setDimAmount(0f)
                setWindowAnimations(R.style.LoadingDialog)
            }
        }

    }
}