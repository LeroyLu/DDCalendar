package com.leroylu.struct.util

import android.content.Context
import android.widget.Toast

/**
 * @author jiaj.lu
 * @date 2020/8/18
 * @description
 */
object ToastUtil {

    private var ctx: Context? = null

    fun init(ctx: Context) {
        this.ctx = ctx
    }

    fun show(str: String?, duration: Int = Toast.LENGTH_SHORT) {
        ctx?.let {
            Toast.makeText(it, str, duration).show()
        }
    }
}