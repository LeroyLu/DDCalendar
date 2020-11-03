package com.leroylu.calendar

import com.alibaba.android.arouter.launcher.ARouter
import com.leroylu.db.DatabaseUtil
import com.leroylu.struct.BaseApplication
import com.leroylu.struct.util.SharedPreferencesUtil
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.FormatStrategy
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy

/**
 * @author jiaj.lu
 * @date 2020/8/17
 * @description
 */
class App : BaseApplication() {

    companion object {
        const val APP_NAME = "DD_Calendar"
    }

    override fun onCreate() {
        super.onCreate()

        initARouter()
        initDatabase()
    }

    private fun initARouter() {
        if (BuildConfig.DEBUG) {
            ARouter.openLog()
            ARouter.openDebug()
        }
        ARouter.init(this)

    }

    private fun initDatabase() {
        DatabaseUtil.init(this, APP_NAME)
        SharedPreferencesUtil.init(ctx = this, name = APP_NAME)
    }

    override fun initLog() {
        val formatStrategy: FormatStrategy = PrettyFormatStrategy.newBuilder()
            .showThreadInfo(false)
            .methodCount(0)
            .tag(APP_NAME)
            .build()

        Logger.addLogAdapter(object : AndroidLogAdapter(formatStrategy) {
            override fun isLoggable(priority: Int, tag: String?): Boolean {
                return BuildConfig.DEBUG
            }
        })
    }

}