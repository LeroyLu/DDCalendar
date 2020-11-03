package com.leroylu.struct.util

import android.app.Activity
import android.content.Context
import android.os.IBinder
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

/**
 * @author jiaj.lu
 * @date 2020/9/10
 * @description
 */
object KeyboardHelper {

    fun checkAndHideKeyboard(activity: Activity, event: MotionEvent) {
        if (event.action == MotionEvent.ACTION_DOWN) {  //把操作放在用户点击的时候
            val view = activity.currentFocus //得到当前页面的焦点,ps:有输入框的页面焦点一般会被输入框占据
            if (isShouldHideKeyboard(view, event)) { //判断用户点击的是否是输入框以外的区域
                hideKeyboard(view!!.windowToken, activity) //收起键盘
            }
        }
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private fun isShouldHideKeyboard(v: View?, event: MotionEvent): Boolean {
        if (v is EditText) {  //判断得到的焦点控件是否包含EditText
            val l = intArrayOf(0, 0)
            v.getLocationInWindow(l)
            val left = l[0]
            //得到输入框在屏幕中上下左右的位置
            val top = l[1]
            val bottom = top + v.getHeight()
            val right = left + v.getWidth()
            // 点击位置如果是EditText的区域，忽略它，不收起键盘。
            return (event.x <= left || event.x >= right
                    || event.y <= top || event.y >= bottom)
        }
        // 如果焦点不是EditText则忽略
        return false
    }

    /**
     * 获取InputMethodManager，隐藏软键盘
     * @param token
     */
    private fun hideKeyboard(token: IBinder?, ctx: Context) {
        if (token != null) {
            val im: InputMethodManager =
                ctx.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }
}