package com.leroylu.struct.util

import android.app.Activity
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.collection.ArrayMap
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleObserver

/**
 * @author jiaj.lu
 * @date 2020/9/10
 * @description
 */
class PermissionHelper : LifecycleObserver {

    private val successSet = ArrayMap<Int, () -> Unit>()
    private val failSet = ArrayMap<Int, (permissions: List<String>) -> Unit>()

    private lateinit var activity: Activity

    fun init(activity: AppCompatActivity) {
        this.activity = activity
    }

    fun checkAndRequestPermissions(
        permissions: Array<String>,
        requestCode: Int,
        success: () -> Unit,
        fail: ((permissions: List<String>) -> Unit)? = null
    ) {
        val ban = permissions.toList().filter {
            !isPermissible(it) && shouldRequest(it)
        }

        if (ban.isEmpty()) {
            success()
        } else {
            successSet[requestCode] = success
            failSet[requestCode] = fail
            ActivityCompat.requestPermissions(activity, ban.toTypedArray(), requestCode)
        }
    }

    fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (grantResults.none { it == PackageManager.PERMISSION_DENIED }) {
            successSet[requestCode]?.invoke()
        } else {
            failSet[requestCode]?.invoke(
                permissions.filter {
                    grantResults[permissions.indexOf(it)] == PackageManager.PERMISSION_DENIED
                }
            )
        }
    }

    private fun isPermissible(permission: String) =
        ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED

    private fun shouldRequest(permission: String) =
        ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)
}