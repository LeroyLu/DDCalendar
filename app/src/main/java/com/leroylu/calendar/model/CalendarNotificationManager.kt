package com.leroylu.calendar.model

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.leroylu.db.bean.calendar.CalendarItem
import com.leroylu.struct.R
import com.leroylu.struct.util.ImageUtil

/**
 * @author jiaj.lu
 * @date 2020/11/4
 * @description
 */
class CalendarNotificationManager(
    private val ctx: Context
) {

    companion object {
        const val CHANNEL_ID = "DDCalendar@com.leroylu.calendar"

        fun createNotificationChannel(context: Context) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val name = "DD Calendar"
                val descriptionText = "For all DD"
                val importance = NotificationManager.IMPORTANCE_DEFAULT
                val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                    description = descriptionText
                }
                context.getSystemService(NotificationManager::class.java).apply {
                    createNotificationChannel(channel)
                }
            }
        }
    }

    private val jumpModel = BilibiliJumpModel(ctx)

    fun notify(item: CalendarItem) {
        ImageUtil.loadBitmap(ctx, item.vtuber.icon, CircleCrop()) {
            val builder = NotificationCompat.Builder(ctx, CHANNEL_ID)
                .setSmallIcon(R.drawable.calendar)
                .setLargeIcon(it)
                .setContentTitle(item.vtuber.name)
                .setContentText(String.format("%02d:%02d %s", item.hour, item.minute, item.info))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setAutoCancel(true)

            if (item.vtuber.streamRoomId.isNotBlank()) {
                val notifyPendingIntent = jumpModel.getLivePendingIntent(item.vtuber)
                builder.setContentIntent(notifyPendingIntent)
            }

            NotificationManagerCompat.from(ctx).notify(item.id, builder.build())
        }
    }
}