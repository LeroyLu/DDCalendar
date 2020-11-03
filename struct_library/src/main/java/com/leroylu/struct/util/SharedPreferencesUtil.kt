package com.leroylu.struct.util

import android.content.Context

/**
 * @author jiaj.lu
 * @date 2020/9/9
 * @description
 */
object SharedPreferencesUtil {

    private lateinit var ctx: Context
    private lateinit var spName: String
    private var mode: Int = Context.MODE_PRIVATE

    fun init(ctx: Context, name: String, mode: Int = Context.MODE_PRIVATE) {
        this.ctx = ctx
        this.spName = name
        this.mode = mode
    }

    fun <T> put(name: String, value: T) {
        val edit = ctx.getSharedPreferences(spName, mode).edit()
        when (value) {
            is String -> edit.putString(name, value)
            is Int -> edit.putInt(name, value)
            is Long -> edit.putLong(name, value)
            is Float -> edit.putFloat(name, value)
            is Boolean -> edit.putBoolean(name, value)
            else -> throw IllegalArgumentException("This type is not supported")
        }
        edit.apply()
    }

    fun remove(name: String) {
        val edit = ctx.getSharedPreferences(spName, mode).edit()
        edit.remove(name)
        edit.apply()
    }

    fun <T> get(name: String, default: T): T {
        val sp = ctx.getSharedPreferences(spName, mode)
        val result = when (default) {
            is String -> sp.getString(name, default)
            is Int -> sp.getInt(name, default)
            is Long -> sp.getLong(name, default)
            is Float -> sp.getFloat(name, default)
            is Boolean -> sp.getBoolean(name, default)
            else -> throw IllegalArgumentException("This type is not supported")
        }
        return result as T
    }
}