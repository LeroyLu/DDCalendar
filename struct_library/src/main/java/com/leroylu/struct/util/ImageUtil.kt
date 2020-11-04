package com.leroylu.struct.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition

/**
 * @author jiaj.lu
 * @date 2020/11/4
 * @description
 */
object ImageUtil {

    fun loadBitmap(
        ctx: Context,
        url: String,
        transformation: BitmapTransformation? = null,
        onResult: (bitmap: Bitmap) -> Unit
    ) {
        Glide.with(ctx)
            .asBitmap()
            .apply {
                if (transformation != null) {
                    transform(transformation)
                }
            }
            .load(url)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    onResult(resource)
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    TODO("Not yet implemented")
                }
            })
    }
}