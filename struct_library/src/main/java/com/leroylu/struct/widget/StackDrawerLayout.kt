package com.leroylu.struct.widget

import android.content.Context
import android.transition.ChangeBounds
import android.transition.TransitionManager
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.core.view.MotionEventCompat
import androidx.customview.widget.ViewDragHelper
import kotlin.math.roundToInt

class StackDrawerLayout(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
    FrameLayout(context, attrs, defStyleAttr) {

    private var mainLayout: View? = null
    private var screenWidth = 0
    private var screenHeight = 0
    private var menuWidth = 0
    private var targetWidth = 0
    private var isOpen = false
    private var mViewDragHelper: ViewDragHelper? = null
    private var canDrag = true

    constructor(context: Context) : this(context, null) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0) {
        init()
    }

    private fun init() {
        screenSize
        mViewDragHelper = ViewDragHelper.create(this, object : ViewDragHelper.Callback() {
            private var top = 0
            override fun tryCaptureView(child: View, pointerId: Int): Boolean {
                return mainLayout === child && canDrag
            }

            override fun onViewCaptured(capturedChild: View, activePointerId: Int) {
                super.onViewCaptured(capturedChild, activePointerId)
                top = capturedChild.top
            }

            override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int {
                return 0
            }

            override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int): Int {
                return if (left > 0) {
                    if (left > menuWidth) menuWidth else left
                } else {
                    0
                }
            }

            override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
                super.onViewReleased(releasedChild, xvel, yvel)
                if (mainLayout!!.left >= targetWidth && xvel >= 0) {
                    isOpen = true
                    mViewDragHelper!!.settleCapturedViewAt(menuWidth, mainLayout!!.top)
                } else {
                    isOpen = false
                    mViewDragHelper!!.settleCapturedViewAt(0, top)
                }
                invalidate()
            }

            override fun onViewPositionChanged(
                changedView: View,
                left: Int,
                top: Int,
                dx: Int,
                dy: Int
            ) {
                mainLayout!!.offsetTopAndBottom(left - top)
            }

            override fun getViewHorizontalDragRange(child: View): Int {
                return 1
            }

            override fun getViewVerticalDragRange(child: View): Int {
                return 0
            }
        })

    }

    private val screenSize: Unit
        private get() {
            try {
                val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
                screenWidth = wm.defaultDisplay.width
                screenHeight = wm.defaultDisplay.height
                menuWidth = (screenWidth.toFloat() * 2 / 3).roundToInt()
                targetWidth = (screenWidth.toFloat() / 4).roundToInt()
            } catch (e: NullPointerException) {
                e.printStackTrace()
            }
        }

    fun open() {
        if (!isOpen) {
            isOpen = true
            TransitionManager.beginDelayedTransition(this, ChangeBounds())
            mainLayout!!.offsetLeftAndRight(menuWidth)
            mainLayout!!.offsetTopAndBottom(menuWidth)
        }
    }

    fun close() {
        if (isOpen) {
            isOpen = false
            TransitionManager.beginDelayedTransition(this, ChangeBounds())
            mainLayout!!.offsetLeftAndRight(-menuWidth)
            mainLayout!!.offsetTopAndBottom(-menuWidth)
        }
    }

    fun setCanDrag(can: Boolean) {
        canDrag = can
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        mainLayout = getChildAt(1)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        when (MotionEventCompat.getActionMasked(ev)) {
            MotionEvent.ACTION_DOWN -> {
                if (isOpen) {
                    return ev.x > mainLayout!!.left && ev.y > mainLayout!!.top
                }
            }
        }
        return mViewDragHelper!!.shouldInterceptTouchEvent(ev)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        mViewDragHelper!!.processTouchEvent(event)
        return true
    }

    override fun computeScroll() {
        if (mViewDragHelper != null && mViewDragHelper!!.continueSettling(true)) {
            invalidate()
        }
    }

    init {
        init()
    }
}