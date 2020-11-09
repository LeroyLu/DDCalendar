package com.leroylu.calendar.model.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.leroylu.calendar.model.PushModel
import com.leroylu.calendar.repository.FallowingDataSource
import com.leroylu.calendar.repository.ScheduleDataSource
import com.leroylu.db.bean.calendar.Vtuber
import com.leroylu.struct.util.ToastUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

/**
 * @author jiaj.lu
 * @date 2020/11/3
 * @description
 */
class FallowingAddViewModel : ViewModel() {

    val id = MutableLiveData<Int>()
    val uid = MutableLiveData<String>()
    val icon = MutableLiveData<String>()
    val name = MutableLiveData<String>()
    val description = MutableLiveData<String>()
    val stream = MutableLiveData<String>()

    private val fallowingDataSource by lazy { FallowingDataSource() }
    private val scheduleDataSource by lazy { ScheduleDataSource() }

    lateinit var pushModel: PushModel

    fun addOrUpdateFallowing(finish: () -> Unit) {

        val nameS = name.value
        if (nameS.isNullOrBlank()) {
            ToastUtil.show("姓名不可为空")
            return
        }

        viewModelScope.launch {
            val vtuber = Vtuber(
                vid = id.value ?: 0,
                uid = uid.value ?: "",
                icon = icon.value ?: "",
                name = name.value ?: "",
                description = description.value ?: "",
                streamRoomId = stream.value ?: ""
            )

            if (vtuber.vid == 0)
                fallowingDataSource.addFallowing(vtuber)
            else {
                fallowingDataSource.updateFallowing(vtuber)
                async { updateSchedules(vtuber) }
            }
            finish()
        }
    }

    private suspend fun updateSchedules(vtuber: Vtuber) {
        withContext(Dispatchers.IO) {
            val list = scheduleDataSource.getSelectDayVid(vtuber.vid)
            val gson = Gson()
            list.forEach {

                it.vtuber = vtuber

                val itemTime = Calendar.getInstance().apply {
                    set(Calendar.YEAR, it.year)
                    set(Calendar.MONTH, it.month)
                    set(Calendar.DAY_OF_MONTH, it.day)
                    set(Calendar.HOUR_OF_DAY, it.hour)
                    set(Calendar.MINUTE, it.minute)
                }.time.time

                if (System.currentTimeMillis() < itemTime) {
                    pushModel.cancelRequest(it.notifyRequestId)
                    val request = pushModel.build(it)
                    it.notifyRequestId = gson.toJson(request.id)
                    pushModel.sendRequest(request)
                }
            }
            scheduleDataSource.updateSchedules(list)
        }
    }
}