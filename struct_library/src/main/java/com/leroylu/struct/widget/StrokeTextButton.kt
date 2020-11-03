package com.leroylu.struct.widget

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.Gravity
import androidx.appcompat.widget.AppCompatTextView
import com.leroylu.struct.R
import com.leroylu.struct.util.DensityUtil

/**
 * @author jiaj.lu
 * @date 2020/8/27
 * @description
 */
class StrokeTextButton : AppCompatTextView {

    constructor(context: Context?) : super(context!!)
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

    private fun init(
        context: Context,
        attrs: AttributeSet?
    ) {
        val array = context.obtainStyledAttributes(attrs, R.styleable.StrokeTextButton)
        val radius = array.getDimension(R.styleable.StrokeTextButton_stbRadius, 0f)
        val rtl = array.getDimension(R.styleable.StrokeTextButton_stbRadiusTopLeft, 0f)
        val rtr = array.getDimension(R.styleable.StrokeTextButton_stbRadiusTopRight, 0f)
        val rbl = array.getDimension(R.styleable.StrokeTextButton_stbRadiusBottomLeft, 0f)
        val rbr = array.getDimension(R.styleable.StrokeTextButton_stbRadiusBottomRight, 0f)
        val width = array.getDimension(R.styleable.StrokeTextButton_stbStrokeWidth, 0f)
        val color =
            array.getColor(R.styleable.StrokeTextButton_stbColor, Color.parseColor("#D9D9D9"))
        array.recycle()

        val drawable = GradientDrawable()
        if (width != 0f) {
            val widthPX = DensityUtil.dip2px(context, width.toInt())
            drawable.setStroke(widthPX, color)
        }
        if (radius != 0f) {
            drawable.cornerRadius = radius
        } else {
            val floats = floatArrayOf(rtl, rtl, rtr, rtr, rbl, rbl, rbr, rbr)
            drawable.cornerRadii = floats
        }
        background = drawable
        setTextColor(color)

        gravity = Gravity.CENTER
    }
}