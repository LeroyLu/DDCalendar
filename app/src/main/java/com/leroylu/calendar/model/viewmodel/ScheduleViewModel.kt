package com.leroylu.calendar.model.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.leroylu.calendar.model.BilibiliJumpModel
import com.leroylu.calendar.model.PushModel
import com.leroylu.calendar.repository.ScheduleDataSource
import com.leroylu.db.bean.calendar.CalendarItem
import kotlinx.coroutines.launch
import java.util.*

/**
 * @author jiaj.lu
 * @date 2020/10/30
 * @description
 */
class ScheduleViewModel : ViewModel() {

    val schedules = MutableLiveData<List<CalendarItem>>()
    val currentCalendar = Calendar.getInstance()

    private val scheduleDataSource: ScheduleDataSource by lazy { ScheduleDataSource() }
    lateinit var jumpModel: BilibiliJumpModel
    lateinit var pushModel: PushModel

    fun getSchedule(
        refresh: SwipeRefreshLayout
    ) {
        viewModelScope.launch {
            refresh.isRefreshing = true

            val year = currentCalendar.get(Calendar.YEAR)
            val month = currentCalendar.get(Calendar.MONTH)
            val day = currentCalendar.get(Calendar.DAY_OF_MONTH)

            val list = scheduleDataSource.getSelectDayData(year, month, day)
            schedules.postValue(list)
            refresh.isRefreshing = false
        }
    }

    fun deleteSchedule(item: CalendarItem, success: () -> Unit) {
        viewModelScope.launch {
            scheduleDataSource.deleteSchedule(item)
            pushModel.cancelRequest(item.notifyRequestId)
            success()
        }
    }

}