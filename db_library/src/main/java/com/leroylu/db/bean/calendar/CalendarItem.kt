package com.leroylu.db.bean.calendar

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @author jiaj.lu
 * @date 2020/10/29
 * @description
 */
@Entity(tableName = "calendar_item")
data class CalendarItem(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    @Embedded
    var vtuber: Vtuber,
    var limited: Boolean,
    var year: Int,
    var month: Int,
    var day: Int,
    var hour: Int,
    var minute: Int,
    var info: String,
    var notifyRequestId: String
)