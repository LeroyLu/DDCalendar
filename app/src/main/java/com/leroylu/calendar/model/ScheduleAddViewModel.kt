package com.leroylu.calendar.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leroylu.calendar.repository.ScheduleDataSource
import com.leroylu.db.bean.calendar.CalendarItem
import com.leroylu.db.bean.calendar.Vtuber
import com.leroylu.struct.util.ToastUtil
import kotlinx.coroutines.launch
import java.util.*

/**
 * @author jiaj.lu
 * @date 2020/11/2
 * @description
 */
class ScheduleAddViewModel : ViewModel() {

    val id = MutableLiveData<Int>()
    val vtuber = MutableLiveData<Vtuber>()
    val isLimited = MutableLiveData<Boolean>(false)
    val info = MutableLiveData<String>("")
    private val dateAndTime = Calendar.getInstance()

    private val scheduleDataSource: ScheduleDataSource by lazy { ScheduleDataSource() }

    fun updateDate(year: Int, month: Int, day: Int) {
        dateAndTime.set(Calendar.YEAR, year)
        dateAndTime.set(Calendar.MONTH, month)
        dateAndTime.set(Calendar.DAY_OF_MONTH, day)
    }

    fun updateTime(hour: Int, minute: Int) {
        dateAndTime.set(Calendar.HOUR_OF_DAY, hour)
        dateAndTime.set(Calendar.MINUTE, minute)
    }

    fun addOrUpdateSchedule(onFinish: () -> Unit) {

        val vtuberV = vtuber.value
        if (vtuberV == null) {
            ToastUtil.show("直播用户未设置")
            return
        }

        val item = CalendarItem(
            id = id.value ?: 0,
            vtuber = vtuberV,
            limited = isLimited.value ?: false,
            year = dateAndTime.get(Calendar.YEAR),
            month = dateAndTime.get(Calendar.MONTH),
            day = dateAndTime.get(Calendar.DAY_OF_MONTH),
            hour = dateAndTime.get(Calendar.HOUR_OF_DAY),
            minute = dateAndTime.get(Calendar.MINUTE),
            info = info.value ?: ""
        )

        viewModelScope.launch {
            if (item.id == 0) {
                scheduleDataSource.addSchedule(item)
            } else {
                scheduleDataSource.updateSchedule(item)
            }
            onFinish()
        }

    }
}