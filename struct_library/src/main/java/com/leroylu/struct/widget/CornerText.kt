package com.leroylu.struct.widget

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.util.TypedValue
import androidx.appcompat.widget.AppCompatTextView
import com.leroylu.struct.R
import com.leroylu.struct.util.DensityUtil.dip2px

class CornerText : AppCompatTextView {

    private var radius = 0f
    private var rtl = 0f
    private var rtr = 0f
    private var rbl = 0f
    private var rbr = 0f
    private var hasStroke = false

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
        val array = context.obtainStyledAttributes(attrs, R.styleable.CornerText)
        radius = array.getDimension(R.styleable.CornerText_ctRadius, 0f)
        rtl = array.getDimension(R.styleable.CornerText_ctRadiusTopLeft, 0f)
        rtr = array.getDimension(R.styleable.CornerText_ctRadiusTopRight, 0f)
        rbl = array.getDimension(R.styleable.CornerText_ctRadiusBottomLeft, 0f)
        rbr = array.getDimension(R.styleable.CornerText_ctRadiusBottomRight, 0f)
        hasStroke = array.getBoolean(R.styleable.CornerText_ctStroke, false)
        array.recycle()

        setBackgroundCorner()
    }

    fun setRadius(r: Int) {
        radius = dip2px(context, r).toFloat()
        setBackgroundCorner()
    }

    fun setCornerBackground(drawable: Drawable?) {
        background = drawable
        setBackgroundCorner()
    }

    private fun setBackgroundCorner() {
        val drawable =
            if (background == null) {
                GradientDrawable()
            } else {
                background.mutate() as GradientDrawable
            }

        if (hasStroke) {
            val width = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                1f,
                resources.displayMetrics
            ).toInt()
            drawable.setStroke(width, Color.parseColor("#D9D9D9"))
        }
        if (radius != 0f) {
            drawable.cornerRadius = radius
        } else {
            val floats = floatArrayOf(rtl, rtl, rtr, rtr, rbl, rbl, rbr, rbr)
            drawable.cornerRadii = floats
        }
        background = drawable
    }
}