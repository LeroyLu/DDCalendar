package com.leroylu.calendar.model

import android.content.Context
import androidx.work.*
import com.google.gson.Gson
import com.leroylu.db.bean.calendar.CalendarItem
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * @author jiaj.lu
 * @date 2020/11/4
 * @description
 */
class PushModel(private val ctx: Context) {

    fun build(item: CalendarItem): OneTimeWorkRequest {

        val target = Calendar.getInstance().apply {
            set(Calendar.YEAR, item.year)
            set(Calendar.MONTH, item.month)
            set(Calendar.DAY_OF_MONTH, item.day)
            set(Calendar.HOUR_OF_DAY, item.hour)
            set(Calendar.MINUTE, item.minute)
        }.time.time

        val delay = target - System.currentTimeMillis() - 5 * 60 * 1000

        return OneTimeWorkRequestBuilder<NotificationPushWork>()
            .setInputData(workDataOf("data" to Gson().toJson(item)))
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .build()
    }

    fun cancelRequest(id: UUID) {
        WorkManager.getInstance(ctx).cancelWorkById(id)
    }

    fun sendRequest(request: OneTimeWorkRequest) {
        WorkManager.getInstance(ctx).enqueue(request)
    }

    class NotificationPushWork(
        private val appContext: Context,
        workerParams: WorkerParameters
    ) :
        Worker(appContext, workerParams) {

        private val notificationManager = CalendarNotificationManager(appContext)

        override fun doWork(): Result {
            val item: CalendarItem =
                Gson().fromJson(inputData.keyValueMap["data"] as String, CalendarItem::class.java)
            CalendarNotificationManager.createNotificationChannel(appContext)
            notificationManager.notify(item)
            return Result.success()
        }
    }
}