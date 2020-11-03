package com.leroylu.calendar.ui.widget

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.leroylu.calendar.R

/**
 * @author jiaj.lu
 * @date 2020/10/28
 * @description
 */
class CustomToolBar : LinearLayout {

    private lateinit var root: View
    private lateinit var back: ImageView
    private lateinit var action: ImageView
    private lateinit var title: TextView

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
        root = LayoutInflater.from(context).inflate(R.layout.customer_toolbar_layout, this)
        title = root.findViewById(R.id.title)
        back = root.findViewById<ImageView>(R.id.back).apply {
            setOnClickListener {
                if (context is Activity) {
                    context.onBackPressed()
                }
            }
        }
        action = root.findViewById<ImageView>(R.id.action).apply {
            setOnClickListener {

            }
        }

        val array = context.obtainStyledAttributes(attrs, R.styleable.CustomToolBar)

        //背景颜色
        array.getColor(
            R.styleable.CustomToolBar_ctbBackground,
            ContextCompat.getColor(context, R.color.white)
        ).let {
            root.setBackgroundColor(it)
        }
        //标题内容
        array.getString(R.styleable.CustomToolBar_ctbTitle)?.let {
            title.text = it
        }
        //标题颜色
        array.getColor(
            R.styleable.CustomToolBar_ctbTitleColor,
            ContextCompat.getColor(context, R.color.white)
        ).let {
            title.setTextColor(it)
        }
        //标题大小
        array.getDimension(R.styleable.CustomToolBar_ctbTitleSize, 17f).let {
            title.textSize = it
        }
        array.getResourceId(R.styleable.CustomToolBar_ctbActionIcon, -1).let {
            if (it != -1) {
                action.setImageResource(it)
                action.isVisible = true
            } else {
                action.isVisible = false
            }
        }

        array.recycle()
    }

    public fun onBackClick(onClick: OnClickListener) {
        back.setOnClickListener(onClick)
    }

    public fun onActionClick(onClick: OnClickListener) {
        action.setOnClickListener(onClick)
    }

    public fun setTitle(title: String) {
        this.title.text = title
    }
}