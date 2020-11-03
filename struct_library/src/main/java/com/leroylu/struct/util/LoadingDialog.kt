package com.leroylu.struct.util

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.leroylu.struct.R
import com.leroylu.struct.databinding.DialogLoadingBinding

/**
 * @author jiaj.lu
 * @date 2020/8/17
 * @description
 */
class LoadingDialog(private val ctx: Context) : LifecycleObserver {

    private var dialog: CustomDialog? = null
    private var text: String? = null

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private fun destroy() {
        dialog?.apply {
            dismiss()
        }
    }

    fun show(content: String = "加载中...") {
        if (dialog == null || content != text) {
            text = content
            dialog?.apply { dismiss() }
            dialog = CustomDialog(ctx, content)
            dialog?.apply {
//                setCancelable(false)
                show()
            }
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
        val msg: String
    ) : Dialog(context) {

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            val root = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null)
            DataBindingUtil.bind<DialogLoadingBinding>(root)?.apply {
                text = msg
            }
            setContentView(root)
            setCanceledOnTouchOutside(false)

        }

        override fun onAttachedToWindow() {
            window?.apply {
                setDimAmount(0f)
                setWindowAnimations(R.style.LoadingDialog)
            }
        }

    }
}