package com.leroylu.struct.widget

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.util.TypedValue
import androidx.constraintlayout.widget.ConstraintLayout
import com.leroylu.struct.R
import com.leroylu.struct.util.DensityUtil.dip2px

/**
 * @author jiaj.lu
 * @date 2020/9/15
 * @description
 */
class CornerLayout : ConstraintLayout {

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
        val array = context.obtainStyledAttributes(attrs, R.styleable.CornerLayout)
        radius = array.getDimension(R.styleable.CornerLayout_clRadius, 0f)
        rtl = array.getDimension(R.styleable.CornerLayout_clRadiusTopLeft, 0f)
        rtr = array.getDimension(R.styleable.CornerLayout_clRadiusTopRight, 0f)
        rbl = array.getDimension(R.styleable.CornerLayout_clRadiusBottomLeft, 0f)
        rbr = array.getDimension(R.styleable.CornerLayout_clRadiusBottomRight, 0f)
        hasStroke = array.getBoolean(R.styleable.CornerLayout_clStroke, false)
        array.recycle()

        setBackgroundDrawable(background)
    }

    fun setRadius(r: Int) {
        radius = dip2px(context, r).toFloat()
        setCornerBackground(background)
    }

    fun setCornerBackground(drawable: Drawable?) {

        if (drawable == null) {
            background = null
            return
        }

        if (drawable is GradientDrawable) {
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
}
