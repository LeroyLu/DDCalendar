package com.leroylu.calendar.ui

import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.leroylu.calendar.R
import com.leroylu.calendar.databinding.ActivityMainBinding
import com.leroylu.struct.ui.BaseActivity
import com.leroylu.struct.ui.BindingConfig

@Route(path = "/calendar/activity")
class CalendarActivity : BaseActivity<ActivityMainBinding>() {

    override fun setBindingConfig(): BindingConfig {
        return BindingConfig(R.layout.activity_main)
    }

    override fun initView() {
        val binding = getBinding()
        binding.toolbar.onActionClick(View.OnClickListener {
            ARouter.getInstance()
                .build("/vtuber/activity")
                .navigation()
        })

    }
}