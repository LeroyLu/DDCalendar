package com.leroylu.db.dao

import androidx.room.*
import com.leroylu.db.bean.calendar.CalendarItem

/**
 * @author jiaj.lu
 * @date 2020/10/29
 * @description
 */
@Dao
interface CalendarItemDao {

    @Query("SELECT * FROM calendar_item WHERE year = :year AND month = :month")
    fun selectAllInMonth(year: Int, month: Int): List<CalendarItem>

    @Query("SELECT * FROM calendar_item WHERE year = :year AND month = :month AND day = :day")
    fun selectAllInDay(year: Int, month: Int, day: Int): List<CalendarItem>

    @Query("SELECT * FROM calendar_item WHERE vid = :vid")
    fun selectAllByVid(vid: Int): List<CalendarItem>

    @Query("SELECT COUNT(*) FROM calendar_item")
    fun getDataCount(): Int

    @Insert
    fun insertAll(vararg list: CalendarItem)

    @Insert
    fun insertAll(list: List<CalendarItem>)

    @Update
    fun updateAll(vararg calendarItem: CalendarItem)

    @Update
    fun updateAll(calendarItem: List<CalendarItem>)

    @Delete
    fun delete(vararg calendarItem: CalendarItem)

    @Delete
    fun delete(items: List<CalendarItem>)

    @Query("DELETE FROM calendar_item")
    fun deleteAll()
}