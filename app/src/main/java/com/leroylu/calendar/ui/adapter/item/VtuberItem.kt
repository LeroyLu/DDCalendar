package com.leroylu.calendar.ui.adapter.item

import com.daimajia.swipe.SwipeLayout
import com.leroylu.calendar.BR
import com.leroylu.calendar.R
import com.leroylu.db.bean.calendar.Vtuber
import com.leroylu.struct.ui.adapter.PageAdapter

/**
 * @author jiaj.lu
 * @date 2020/10/29
 * @description
 */
class VtuberItem(
    self: Vtuber,
    private val isSwipe: Boolean = false
) : PageAdapter.PagingItem<Vtuber>(self) {

    var onSelect: ((Vtuber) -> Unit)? = null
    var onEdit: ((Vtuber) -> Unit)? = null
    var onDelete: ((Vtuber) -> Unit)? = null
    var onBrowse: ((Vtuber) -> Unit)? = null

    override fun areItemsTheSame(data: Vtuber): Boolean {
        return self.vid == data.vid
    }

    override fun areContentsTheSame(data: Vtuber): Boolean {
        return self.vid == data.vid
    }

    override fun onBindView(holder: PageAdapter.PagingItemHolder, position: Int) {
        holder.binding?.apply {
            setVariable(BR.data, self)
            setVariable(BR.holder, this@VtuberItem)

            root.findViewById<SwipeLayout>(R.id.swipe).isSwipeEnabled = isSwipe
        }
    }

    fun onSelectCallback() {
        onSelect?.invoke(self)
    }

    fun onEditCallback() {
        onEdit?.invoke(self)
    }

    fun onDeleteCallback() {
        onDelete?.invoke(self)
    }

    fun onBrowseCallback() {
        onBrowse?.invoke(self)
    }
}