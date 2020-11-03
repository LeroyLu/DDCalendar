package com.leroylu.struct.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.leroylu.struct.R

/**
 * @author jiaj.lu
 * @date 2020/8/26
 * @description
 */
class LimitLinearLayout : LinearLayout {
    private var maxHeight = 0f

    constructor(context: Context?) : super(context)
    constructor(
        context: Context,
        attrs: AttributeSet?
    ) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var height = MeasureSpec.getSize(heightMeasureSpec)
        if (maxHeight != 0f) {
            height = if (height > maxHeight) maxHeight.toInt() else height
        }
        super.onMeasure(widthMeasureSpec, height)
    }

    private fun init(
        context: Context,
        attrs: AttributeSet?
    ) {
        val array = context.obtainStyledAttributes(attrs, R.styleable.LimitLinearLayout)
        maxHeight = array.getDimension(R.styleable.LimitLinearLayout_lllMaxHeight, 0f)
        array.recycle()
    }
}