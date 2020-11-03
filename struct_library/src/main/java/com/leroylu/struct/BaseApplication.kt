package com.leroylu.struct

import android.app.Application
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import com.leroylu.struct.util.ToastUtil


/**
 * @author jiaj.lu
 * @date 2020/8/18
 * @description
 */
open class BaseApplication : Application(), ViewModelStoreOwner {

    private lateinit var vms: ViewModelStore

    override fun onCreate() {
        super.onCreate()
        vms = ViewModelStore()
        ToastUtil.init(this)

        initLog()
        initRetrofit()
    }

    override fun getViewModelStore(): ViewModelStore = vms

    protected open fun initLog() {

    }

    protected open fun initRetrofit() {

    }

}