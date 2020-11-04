package com.leroylu.calendar.model

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.leroylu.db.bean.calendar.Vtuber
import com.leroylu.struct.util.ToastUtil

/**
 * @author jiaj.lu
 * @date 2020/11/4
 * @description
 */
class BilibiliJumpModel(
    private val ctx: Context
) {

    fun jumpToLive(vtuber: Vtuber) {
        val id = vtuber.streamRoomId
        if (id.isBlank()) {
            ToastUtil.show("未设置房间号")
            return
        }
        try {
            val intent = Intent()
            intent.action = "android.intent.action.VIEW"
            intent.data = Uri.parse("bilibili://live/${id}")
            ctx.startActivity(intent)
        } catch (ignore: Exception) {
            ToastUtil.show("未安装bilibili或房间号错误")
        }
    }

    fun jumpToSpace(vtuber: Vtuber) {
        val id = vtuber.uid
        if (id.isBlank()) {
            ToastUtil.show("未设置uid")
            return
        }
        try {
            val intent = Intent()
            intent.action = "android.intent.action.VIEW"
            intent.data = Uri.parse("bilibili://space/${id}")
            ctx.startActivity(intent)
        } catch (ignore: Exception) {
            ToastUtil.show("未安装bilibili或uid错误")
        }
    }

    fun getLivePendingIntent(vtuber: Vtuber): PendingIntent {
        val intent = Intent().apply {
            action = "android.intent.action.VIEW"
            data = Uri.parse("bilibili://live/${vtuber.streamRoomId}")
        }
        return PendingIntent.getActivity(ctx, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }
}