package com.leroylu.struct.ui

import android.util.ArrayMap

/**
 * @author jiaj.lu
 * @date 2020/8/18
 * @description
 */
class BindingConfig(val layoutId: Int) {

    private val params: ArrayMap<Int, Any> = ArrayMap()

    companion object {
        fun build(layoutId: Int) = BindingConfig(layoutId)
    }

    fun addParam(name: Int, obj: Any): BindingConfig {
        params[name] = obj
        return this
    }

    fun getParams() = params
}