package com.leroylu.calendar.model.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.leroylu.calendar.model.BilibiliJumpModel
import com.leroylu.calendar.model.PushModel
import com.leroylu.calendar.repository.FallowingDataSource
import com.leroylu.db.bean.calendar.Vtuber
import kotlinx.coroutines.launch

/**
 * @author jiaj.lu
 * @date 2020/10/29
 * @description
 */
class FallowingViewModel : ViewModel() {

    private val dataSource by lazy { FallowingDataSource() }
    lateinit var jumpModel: BilibiliJumpModel
    lateinit var pushModel: PushModel

    fun getFallowing() = Pager(
        PagingConfig(pageSize = 30),
        pagingSourceFactory = dataSource.getAllFallowing()
    ).liveData

    fun deleteFallowing(vtuber: Vtuber, success: () -> Unit) {
        viewModelScope.launch {
            dataSource.deleteFallowing(vtuber, pushModel)
            success()
        }
    }
}