package com.leroylu.struct.util

import android.content.Context

/**
 * @author jiaj.lu
 * @date 2020/8/24
 * @description dp, px转换
 */
object DensityUtil {

    fun dip2px(context: Context, dpValue: Int): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    fun px2dip(context: Context, pxValue: Int): Int {
        val scale = context.resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }
}