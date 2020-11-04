package com.leroylu.calendar.repository

import androidx.paging.PagingSource
import com.google.gson.Gson
import com.leroylu.calendar.model.PushModel
import com.leroylu.db.DatabaseUtil
import com.leroylu.db.bean.calendar.Vtuber
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

/**
 * @author jiaj.lu
 * @date 2020/10/29
 * @description
 */
class FallowingDataSource {

    private val vtuberDao = DatabaseUtil.db.vtuber()
    private val calendarDao = DatabaseUtil.db.calendarItem()

    fun getAllFallowing(): () -> PagingSource<Int, Vtuber> {
        return vtuberDao.selectAllPaging().asPagingSourceFactory()
    }

    suspend fun addFallowing(v: Vtuber) {
        withContext(Dispatchers.IO) {
            vtuberDao.insertAll(v)
        }
    }

    suspend fun updateFallowing(v: Vtuber) {
        withContext(Dispatchers.IO) {
            vtuberDao.updateAll(v)
            val list = calendarDao.selectAllByVid(v.vid).apply {
                forEach {
                    it.vtuber = v
                }
            }
            calendarDao.updateAll(list)
        }
    }

    suspend fun deleteFallowing(v: Vtuber, pushModel: PushModel) {
        withContext(Dispatchers.IO) {
            vtuberDao.delete(v)
            val items = calendarDao.selectAllByVid(v.vid).apply {
                val gson = Gson()
                forEach {
                    pushModel.cancelRequest(gson.fromJson(it.notifyRequestId, UUID::class.java))
                }
            }
            calendarDao.delete(items)
        }
    }
}