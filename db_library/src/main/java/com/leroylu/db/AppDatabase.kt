package com.leroylu.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.leroylu.db.bean.calendar.CalendarItem
import com.leroylu.db.bean.calendar.Vtuber
import com.leroylu.db.dao.CalendarItemDao
import com.leroylu.db.dao.VtuberDao

/**
 * @author jiaj.lu
 * @date 2020/9/8
 * @description
 */
@Database(entities = [Vtuber::class, CalendarItem::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun vtuber(): VtuberDao

    abstract fun calendarItem(): CalendarItemDao

}