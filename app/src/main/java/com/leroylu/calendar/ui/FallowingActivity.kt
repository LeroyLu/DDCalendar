package com.leroylu.calendar.ui

import com.alibaba.android.arouter.facade.annotation.Route
import com.leroylu.calendar.R
import com.leroylu.calendar.databinding.ActivityVtuberBinding
import com.leroylu.struct.ui.BaseActivity
import com.leroylu.struct.ui.BindingConfig

@Route(path = "/vtuber/activity")
class FallowingActivity : BaseActivity<ActivityVtuberBinding>() {

    override fun setBindingConfig(): BindingConfig {
        return BindingConfig(R.layout.activity_vtuber)
    }

}