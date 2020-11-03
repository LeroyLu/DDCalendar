package com.leroylu.calendar.ui.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.CalendarView
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.leroylu.calendar.R
import com.leroylu.calendar.databinding.FragmentCalendarBinding
import com.leroylu.calendar.model.ScheduleViewModel
import com.leroylu.calendar.ui.adapter.CalendarDayAdapter
import com.leroylu.db.bean.calendar.CalendarItem
import com.leroylu.struct.ui.BaseFragment
import java.util.*


class CalendarFragment : BaseFragment<FragmentCalendarBinding>() {

    private lateinit var scheduleViewModel: ScheduleViewModel
    private val adapter = CalendarDayAdapter(
        onEdit = { edit(it) },
        onDelete = { delete(it) },
        onBrowse = { it ->
            val id = it.vtuber.streamRoomId
            if (id.isBlank()) return@CalendarDayAdapter
            try {
                val intent = Intent()
                intent.action = "android.intent.action.VIEW"
                intent.data = Uri.parse("bilibili://live/${id}")
                startActivity(intent)
            } catch (ignore: Exception) {

            }
        }
    )

    override fun getLayoutId(): Int {
        return R.layout.fragment_calendar
    }

    override fun bindingData(binding: FragmentCalendarBinding) {
        binding.operation = Operation()
    }

    override fun initViewModel() {
        scheduleViewModel = getActivityViewModel(ScheduleViewModel::class.java)
    }

    override fun initObserver() {
        scheduleViewModel.schedules.observe(this) { list ->
            val sorted = list.sortedBy { it.hour * 60 + it.minute }
            adapter.update(sorted)
        }
    }

    override fun initView() {

        getBinding().apply {
            recyclerView.let {
                it.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                it.adapter = adapter
            }

            refreshLayout.setOnRefreshListener {
                scheduleViewModel.getSchedule(getBinding().refreshLayout)
            }

            date.apply {
                date = scheduleViewModel.currentCalendar.time.time
                setOnDateChangeListener { _: CalendarView, year: Int, month: Int, dayOfMonth: Int ->

                    scheduleViewModel.currentCalendar.apply {
                        set(Calendar.YEAR, year)
                        set(Calendar.MONTH, month)
                        set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    }
                    updateDayLabel()
                    scheduleViewModel.getSchedule(refreshLayout)
                }
            }

            updateDayLabel()
        }
    }

    private fun updateDayLabel() {
        val calendar = scheduleViewModel.currentCalendar
        val dayOfWeek = getDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK))
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val label = "${month + 1}月${day}日 $dayOfWeek"
        getBinding().dayLabel.text = label
    }

    override fun init() {
        scheduleViewModel.getSchedule(getBinding().refreshLayout)
    }

    private fun edit(item: CalendarItem) {
        val data = Bundle().apply {
            putSerializable("mode", ScheduleAddFragment.Mode.Modify)
            putString("data", Gson().toJson(item))
        }
        findNavController().navigate(R.id.goToAddSchedule, data)
    }

    private fun delete(item: CalendarItem) {
        scheduleViewModel.deleteSchedule(item) {
            adapter.remove(item)
        }
    }

    private fun getDayOfWeek(index: Int): String {
        return when (index) {
            1 -> "星期日"
            2 -> "星期一"
            3 -> "星期二"
            4 -> "星期三"
            5 -> "星期四"
            6 -> "星期五"
            7 -> "星期六"
            else -> ""
        }
    }

    inner class Operation {

        fun add() {
            val data = Bundle().apply {
                putSerializable("mode", ScheduleAddFragment.Mode.Add)
                putSerializable("date", scheduleViewModel.currentCalendar.time.time)
            }
            findNavController().navigate(R.id.goToAddSchedule, data)
        }
    }
}