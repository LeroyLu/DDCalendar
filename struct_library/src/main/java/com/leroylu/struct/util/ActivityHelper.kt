package com.leroylu.struct.util

import android.app.Activity
import android.content.Intent
import androidx.collection.ArrayMap
import com.alibaba.android.arouter.facade.Postcard

/**
 * @author jiaj.lu
 * @date 2020/9/11
 * @description
 */
class ActivityHelper {

    private val callbackSet = ArrayMap<Int, (data: Intent?, resultCode: Int) -> Unit>()

    private lateinit var activity: Activity

    fun init(activity: Activity) {
        this.activity = activity
    }

    fun startActivityWithResult(
        postcard: Postcard?,
        requestCode: Int,
        callback: (data: Intent?, resultCode: Int) -> Unit
    ) {
        postcard?.let {
            callbackSet[requestCode] = callback
            it.navigation(activity, requestCode)
        }
    }

    fun startActivityWithResult(
        intent: Intent,
        requestCode: Int,
        callback: (data: Intent?, resultCode: Int) -> Unit
    ) {
        callbackSet[requestCode] = callback
        activity.startActivityForResult(intent, requestCode)
    }

    fun dispatchActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackSet[requestCode]?.invoke(data, resultCode)
    }
}