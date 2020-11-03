package com.leroylu.struct.util

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.leroylu.struct.R
import com.leroylu.struct.databinding.DialogListBinding


/**
 * @author jiaj.lu
 * @date 2020/7/30
 * @description
 */
class CustomDialogBuilder {

    private lateinit var mContext: Context
    private var mLayout = 0
    private lateinit var mRoot: View
    private var mGravity = Gravity.CENTER
    private var mCancelable = true
    private var animId = -1
    private var width = LinearLayout.LayoutParams.WRAP_CONTENT
    private var height = LinearLayout.LayoutParams.WRAP_CONTENT

    fun context(context: Context): CustomDialogBuilder {
        mContext = context
        return this
    }

    fun layout(id: Int): CustomDialogBuilder {
        mLayout = id
        return this
    }

    fun <T : ViewDataBinding> initView(initializer: ViewInitializer<T>): CustomDialogBuilder {
        mRoot = LayoutInflater.from(mContext).inflate(mLayout, null)
        val binding: T = DataBindingUtil.bind(mRoot)!!
        if (mContext is LifecycleOwner)
            binding.lifecycleOwner = mContext as LifecycleOwner
        initializer.init(binding)
        return this
    }

    fun gravity(gravity: Int): CustomDialogBuilder {
        mGravity = gravity
        return this
    }

    fun cancelable(cancelable: Boolean): CustomDialogBuilder {
        mCancelable = cancelable
        return this
    }

    fun setAnim(id: Int): CustomDialogBuilder {
        animId = id
        return this
    }

    fun layout(width: Int, height: Int): CustomDialogBuilder {
        this.width = width
        this.height = height
        return this
    }

    fun build(): Dialog? {
        val builder = AlertDialog.Builder(mContext)
        builder.setView(mRoot)
        val dialog: Dialog = builder.create()
        dialog.setCancelable(mCancelable)
        val window = dialog.window ?: return null
        window.decorView.setPadding(0, 0, 0, 0)
        window.setBackgroundDrawable(
            mContext!!.resources.getDrawable(R.drawable.shape_transparent)
        )
        window.setLayout(width, height)
        window.setGravity(mGravity or Gravity.CENTER_HORIZONTAL)
        if (animId != -1) {
            window.setWindowAnimations(animId)
        }
        return dialog
    }

    interface ViewInitializer<T : ViewDataBinding> {
        fun init(binding: T)
    }

    companion object {

        fun commonListDialog(
            ctx: Context,
            listAdapter: RecyclerView.Adapter<*>
        ): Dialog? {
            return CustomDialogBuilder()
                .context(ctx)
                .layout(R.layout.dialog_list)
                .initView(object : ViewInitializer<DialogListBinding> {
                    override fun init(binding: DialogListBinding) {
                        binding.recyclerView.apply {
                            adapter = listAdapter
                            layoutManager = LinearLayoutManager(ctx, RecyclerView.VERTICAL, false)
                        }
                    }
                })
                .gravity(Gravity.BOTTOM)
                .cancelable(true)
                .layout(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                .setAnim(R.style.Animation_Bottom_Dialog)
                .build()
        }
    }

}