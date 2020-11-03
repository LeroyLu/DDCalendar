package com.leroylu.struct.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.view.isVisible
import com.leroylu.struct.R

/**
 * @author jiaj.lu
 * @date 2020/8/19
 * @description
 */
class ItemIcon : FrameLayout {

    lateinit var srcImage: ImageView
    lateinit var frame: ImageView

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(
        context: Context,
        attrs: AttributeSet?
    ) : super(context, attrs) {
        init(context)
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        init(context)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val padding = w.div(12)
        srcImage.setPadding(padding, padding, padding, padding)
    }

    private fun init(context: Context) {
        val root = LayoutInflater.from(context).inflate(R.layout.item_icon, this)
        srcImage = root.findViewById(R.id.srcImage)
        frame = root.findViewById(R.id.frame)
    }

    fun getImageView() = srcImage

    fun setFrameViaable(isVisible: Boolean) {
        frame.isVisible = isVisible
    }
}