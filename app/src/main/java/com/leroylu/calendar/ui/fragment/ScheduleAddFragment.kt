package com.leroylu.calendar.ui.fragment

import android.os.Build
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.map
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.google.gson.Gson
import com.leroylu.calendar.R
import com.leroylu.calendar.databinding.FragmentScheduleAddBinding
import com.leroylu.calendar.model.PushModel
import com.leroylu.calendar.model.viewmodel.FallowingViewModel
import com.leroylu.calendar.model.viewmodel.ScheduleAddViewModel
import com.leroylu.calendar.ui.adapter.item.VtuberItem
import com.leroylu.calendar.ui.widget.FallowingSelectDialog
import com.leroylu.db.bean.calendar.CalendarItem
import com.leroylu.db.bean.calendar.Vtuber
import com.leroylu.struct.ui.BaseFragment
import com.leroylu.struct.ui.adapter.PageAdapter
import java.util.*

class ScheduleAddFragment : BaseFragment<FragmentScheduleAddBinding>() {

    enum class Mode {
        Add, Modify
    }

    private lateinit var scheduleAddViewModel: ScheduleAddViewModel
    private lateinit var fallowingViewModel: FallowingViewModel

    private lateinit var fallowAdapter: PageAdapter<Vtuber>
    private lateinit var fallowingSelectDialog: FallowingSelectDialog

    private lateinit var mode: Mode

    override fun getLayoutId(): Int {
        return R.layout.fragment_schedule_add
    }

    override fun initViewModel() {
        scheduleAddViewModel = getViewModel(ScheduleAddViewModel::class.java).apply {
            pushModel = PushModel(requireContext())
        }
        fallowingViewModel = getViewModel(FallowingViewModel::class.java).apply {
            pushModel = PushModel(requireContext())
        }
    }

    override fun bindingData(binding: FragmentScheduleAddBinding) {
        mode = (arguments?.getSerializable("mode") ?: Mode.Add) as Mode
        binding.operation = Operation(mode)
        binding.vm = scheduleAddViewModel
    }

    override fun initObserver() {
        fallowingViewModel.getFallowing()
            .observe(this, Observer {
                lifecycleScope.launchWhenCreated {
                    fallowAdapter.submitData(lifecycle, it.map {
                        VtuberItem(it).apply {
                            onSelect = { v ->
                                fallowingSelectDialog.hide()
                                scheduleAddViewModel.vtuber.postValue(v)
                            }
                        }
                    })
                }
            })

        scheduleAddViewModel.vtuber.observe(this) {
            Glide.with(requireContext()).load(it.icon)
                .transform(CircleCrop())
                .into(getBinding().vtuber)
        }
    }

    override fun init() {
        if (mode == Mode.Modify) {
            val dataS = arguments?.getString("data")
            val calendarItem = Gson().fromJson<CalendarItem>(dataS, CalendarItem::class.java)

            scheduleAddViewModel.id.postValue(calendarItem.id)

            scheduleAddViewModel.updateDate(calendarItem.year, calendarItem.month, calendarItem.day)
            scheduleAddViewModel.updateTime(calendarItem.hour, calendarItem.minute)
            scheduleAddViewModel.vtuber.postValue(calendarItem.vtuber)
            scheduleAddViewModel.info.postValue(calendarItem.info)
            scheduleAddViewModel.isLimited.postValue(calendarItem.limited)
            scheduleAddViewModel.pushId.postValue(calendarItem.notifyRequestId)

            getBinding().apply {

                date.date = Calendar.getInstance().apply {
                    set(Calendar.YEAR, calendarItem.year)
                    set(Calendar.MONTH, calendarItem.month)
                    set(Calendar.DAY_OF_MONTH, calendarItem.day)
                }.time.time

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    time.hour = calendarItem.hour
                    time.minute = calendarItem.minute
                }
            }

        } else {
            arguments?.getLong("date")?.let {
                getBinding().date.date = it

                val canceler = Calendar.getInstance().apply { time = Date(it) }
                scheduleAddViewModel.updateDate(
                    canceler.get(Calendar.YEAR),
                    canceler.get(Calendar.MONTH),
                    canceler.get(Calendar.DAY_OF_MONTH)
                )
            }
        }
    }

    override fun initView() {
        getBinding().apply {
            date.let {
                it.setOnDateChangeListener { _, year, month, dayOfMonth ->
                    scheduleAddViewModel.updateDate(year, month, dayOfMonth)
                }
            }

            time.let {
                it.setIs24HourView(true)
                it.setOnTimeChangedListener { _, hourOfDay, minute ->
                    scheduleAddViewModel.updateTime(hourOfDay, minute)
                }
            }

            commit.text = when (mode) {
                Mode.Add -> "添加"
                Mode.Modify -> "保存"
            }
        }

        fallowAdapter = PageAdapter<Vtuber>(R.layout.adapter_fallowing)
        fallowingSelectDialog = FallowingSelectDialog(requireContext())
        lifecycle.addObserver(fallowingSelectDialog)

    }

    inner class Operation(
        private val mode: Mode
    ) {

        fun commit() {
            scheduleAddViewModel.addOrUpdateSchedule {
                findNavController().popBackStack()
            }
        }

        fun selectFallowing() {
            fallowingSelectDialog.show(fallowAdapter)
        }
    }
}