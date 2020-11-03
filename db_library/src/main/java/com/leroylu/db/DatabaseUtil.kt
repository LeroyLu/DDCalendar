package com.leroylu.db

import android.content.Context
import androidx.room.Room

/**
 * @author jiaj.lu
 * @date 2020/9/8
 * @description
 */
object DatabaseUtil {

    private lateinit var ctx: Context
    private lateinit var name: String

    fun init(ctx: Context, name: String) {
        this.ctx = ctx
        this.name = name
    }

    val db: AppDatabase by lazy {
        Room.databaseBuilder(ctx, AppDatabase::class.java, name).build()
    }


}