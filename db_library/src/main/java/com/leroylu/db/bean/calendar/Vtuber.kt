package com.leroylu.db.bean.calendar

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @author jiaj.lu
 * @date 2020/10/29
 * @description
 */
@Entity(tableName = "vtuber")
data class Vtuber(
    @PrimaryKey(autoGenerate = true)
    var vid: Int = 0,
    var icon: String,
    var name: String,
    var streamRoomId: String,
    var description: String
)