package com.leroylu.calendar.repository

import com.leroylu.db.DatabaseUtil
import com.leroylu.db.bean.calendar.CalendarItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * @author jiaj.lu
 * @date 2020/10/30
 * @description
 */
class ScheduleDataSource {

    private val dao = DatabaseUtil.db.calendarItem()

    suspend fun getSelectDayData(
        year: Int, month: Int, day: Int
    ): List<CalendarItem> = withContext(Dispatchers.IO) {
        dao.selectAllInDay(year, month, day)
    }

    suspend fun addSchedule(calendarItem: CalendarItem) = withContext(Dispatchers.IO) {
        dao.insertAll(calendarItem)
    }

    suspend fun updateSchedule(calendarItem: CalendarItem) = withContext(Dispatchers.IO) {
        dao.updateAll(calendarItem)
    }

    suspend fun deleteSchedule(calendarItem: CalendarItem) = withContext(Dispatchers.IO) {
        dao.delete(calendarItem)
    }
}