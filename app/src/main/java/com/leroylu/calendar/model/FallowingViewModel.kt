package com.leroylu.calendar.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.leroylu.calendar.repository.FallowingDataSource
import com.leroylu.db.bean.calendar.Vtuber
import com.leroylu.struct.util.ToastUtil
import kotlinx.coroutines.launch

/**
 * @author jiaj.lu
 * @date 2020/10/29
 * @description
 */
class FallowingViewModel : ViewModel() {

    val id = MutableLiveData<Int>()
    val icon = MutableLiveData<String>()
    val name = MutableLiveData<String>()
    val description = MutableLiveData<String>()
    val stream = MutableLiveData<String>()

    private val dataSource by lazy { FallowingDataSource() }

    fun getFallowing() = Pager(
        PagingConfig(pageSize = 30),
        pagingSourceFactory = dataSource.getAllFallowing()
    ).liveData

    fun addOrUpdateFallowing(finish: () -> Unit) {

        val nameS = name.value
        if (nameS.isNullOrBlank()) {
            ToastUtil.show("姓名不可为空")
            return
        }

        viewModelScope.launch {
            val vtuber = Vtuber(
                vid = id.value ?: 0,
                icon = icon.value ?: "",
                name = name.value ?: "",
                description = description.value ?: "",
                streamRoomId = stream.value ?: ""
            )

            if (vtuber.vid == 0)
                dataSource.addFallowing(vtuber)
            else
                dataSource.updateFallowing(vtuber)
            finish()
        }
    }

    fun deleteFallowing(vtuber: Vtuber, success: () -> Unit) {
        viewModelScope.launch {
            dataSource.deleteFallowing(vtuber)
            success()
        }
    }
}