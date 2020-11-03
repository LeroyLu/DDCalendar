package com.leroylu.struct.ui.binding

import android.net.Uri
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.jakewharton.rxbinding2.view.RxView
import com.leroylu.struct.util.net.Client
import com.leroylu.struct.widget.ItemIcon
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import java.util.concurrent.TimeUnit


/**
 * @author jiaj.lu
 * @date 2020/8/19
 * @description
 */
object Common {

    @BindingAdapter("app:srcUrl", requireAll = false)
    @JvmStatic
    fun setImageUrl(view: ItemIcon, url: String?) {
        Glide.with(view.context).load(Client.host + url).into(view.getImageView())
    }

    @BindingAdapter("app:srcUrl", requireAll = false)
    @JvmStatic
    fun setImageUrl(view: ImageView, url: String?) {
        Glide.with(view.context).load(url).into(view)
    }

    @BindingAdapter("app:srcUri", requireAll = false)
    @JvmStatic
    fun setImageUri(view: ImageView, s: String?) {
        val uri = Uri.parse(s)
        Glide.with(view.context).load(uri)
            .transform(CircleCrop())
            .into(view)
    }

    @BindingAdapter("app:srcCircleCropUrl", requireAll = false)
    @JvmStatic
    fun setCircleCropImageUrl(view: ImageView, s: String?) {
        Glide.with(view.context).load(s)
            .transform(CircleCrop())
            .into(view)
    }

    @BindingAdapter("app:complete", requireAll = false)
    @JvmStatic
    fun isCompleted(view: SmartRefreshLayout, isCompleted: Boolean) {
        if (isCompleted) {
            view.finishLoadMore()
            view.finishRefresh()
        }
    }

    @BindingAdapter("app:visible", requireAll = false)
    @JvmStatic
    fun isVisible(view: View, visibility: Boolean?) {
        if (visibility == true) {
            view.visibility = View.VISIBLE
        } else {
            view.visibility = View.GONE
        }
    }

    @BindingAdapter("app:adapter", requireAll = false)
    @JvmStatic
    fun setAdapter(view: RecyclerView, adapter: RecyclerView.Adapter<*>?) {
        adapter?.let {
            view.adapter = it
        }
    }

    @BindingAdapter("android:onClick", requireAll = false)
    @JvmStatic
    fun filterFastClick(view: View, listener: View.OnClickListener) {
        RxView.clicks(view) //两秒钟之内只取一个点击事件，防抖操作
            .throttleFirst(1, TimeUnit.SECONDS)
            .subscribe { listener.onClick(view) }
    }
}