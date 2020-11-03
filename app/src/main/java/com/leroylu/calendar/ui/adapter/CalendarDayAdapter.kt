package com.leroylu.calendar.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.leroylu.calendar.R
import com.leroylu.calendar.databinding.AdapterCalendarItemBinding
import com.leroylu.db.bean.calendar.CalendarItem
import com.leroylu.struct.widget.CornerText

/**
 * @author jiaj.lu
 * @date 2020/10/28
 * @description
 */
class CalendarDayAdapter(
    private val onItemClick: ((CalendarItem) -> Unit)? = null,
    private val onLongTouch: ((View, CalendarItem) -> Unit)? = null,
    private val onBrowse: ((CalendarItem) -> Unit)? = null,
    private val onEdit: ((CalendarItem) -> Unit)? = null,
    private val onDelete: ((CalendarItem) -> Unit)? = null,
) : RecyclerView.Adapter<CalendarDayAdapter.DayHolder>() {

    private val items = ArrayList<CalendarItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayHolder {
        val root = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_calendar_item, parent, false)
        return DayHolder(root)
    }

    override fun onBindViewHolder(holder: DayHolder, position: Int) {
        holder.onBind(items[position], position)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun update(list: List<CalendarItem>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    fun remove(item: CalendarItem) {
        val index = items.indexOf(item)
        items.remove(item)
        notifyItemRemoved(index)
    }

    inner class DayHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val time = itemView.findViewById<TextView>(R.id.time)
        private val description = itemView.findViewById<TextView>(R.id.description)
        private val edit = itemView.findViewById<ImageView>(R.id.edit)
        private val del = itemView.findViewById<ImageView>(R.id.del)
        private val type = itemView.findViewById<CornerText>(R.id.type)

        fun onBind(item: CalendarItem, index: Int) {
            val timeS = String.format("%02d", item.hour) + ":" + String.format("%02d", item.minute)

            DataBindingUtil.bind<AdapterCalendarItemBinding>(itemView)?.let {
                it.vtuber = item.vtuber
                it.time.text = timeS
                it.description.text = item.info
                it.type.isVisible = item.limited
                it.icon.setOnClickListener { onBrowse?.invoke(item) }
                it.edit.setOnClickListener { onEdit?.invoke(item) }
                it.del.setOnClickListener { onDelete?.invoke(item) }
            }

            itemView.setOnClickListener { onItemClick?.invoke(item) }
            itemView.setOnLongClickListener {
                onLongTouch?.invoke(it, item)
                true
            }
        }
    }

}