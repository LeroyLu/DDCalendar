package com.leroylu.struct.util

import android.app.Activity
import androidx.collection.ArraySet

/**
 * @author jiaj.lu
 * @date 2020/9/11
 * @description
 */
object ActivityManager {

    private val activitySet = ArraySet<Activity>()

    fun addActivity(activity: Activity) {
        activitySet.add(activity)
    }

    fun removeActivity(activity: Activity) {
        activitySet.remove(activity)
    }

    fun finishAll() {
        activitySet.forEach { it.finish() }
    }

}